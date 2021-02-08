package process.definition.model;

import base.dynamics.TypeManager;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author gwz
 */
public final class ModelParser extends StdDeserializer<Object> {

    private static final String ENTITY_SIGNAL = "entity";
    private static final String REQUIREMENT_SIGNAL = "requirement";
    public static final String PROJECT_PATH = System.getProperty("user.dir");
    public static final String JAVA_FILE_PATH = PROJECT_PATH + "/src/main/resources/definitionJava/";

    protected ModelParser(Class<?> vc) {
        super(vc);
    }

    public ModelParser() {
        this(null);
    }

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        parseNode(root.get(ENTITY_SIGNAL), false);
        parseNode(root.get(REQUIREMENT_SIGNAL), true);
        return null;
    }

    private void parseNode(JsonNode node, boolean isRequirement) throws IOException {
        Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = iterator.next();
            String name = entry.getKey();
            Map<String, String> field2type = parseFields(entry.getValue());
            TypeManager.addType(name, field2type);
            generateJavaFile(name, field2type, isRequirement);
        }
    }

    private Map<String, String> parseFields(JsonNode node) {
        Map<String, String> fields2type = new HashMap<>(8);
        Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = iterator.next();
            String fieldName = entry.getKey();
            String type = entry.getValue().asText();
            TypeManager.checkType(type);
            fields2type.put(fieldName, type);
        }
        return fields2type;
    }

    private void generateJavaFile(String name, Map<String, String> fields2type, boolean isRequirement)
            throws IOException {
        String content = generateFileHead(name) +
                generateFields(fields2type) +
                generateGetType(name) +
                generateIsPrimitive() +
                generateIsRequirement(isRequirement) +
                generateEquals(name, fields2type) +
                generateFileEnd();

        File file = new File(JAVA_FILE_PATH + name + ".java");
        if (! file.createNewFile()) {
            System.out.println("Replace file " + name + ".java" + " before.");
        }
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(content);
        fileWriter.flush();
        fileWriter.close();
    }

    private String generateFileHead(String name) {
        return  "import base.type.BaseEntity;\n" +
                "import base.type.primitive.*;\n" +
                "import base.type.collection.*;\n" +
                "\npublic class " + name + " extends BaseEntity {\n";
    }

    private String generateFields(Map<String, String> fields2type) {
        StringBuilder content = new StringBuilder();
        for (Map.Entry<String, String> entry: fields2type.entrySet()) {
            content.append("    public ")
                    .append(TypeManager.type2class(entry.getValue()))
                    .append(" ")
                    .append(entry.getKey())
                    .append(";\n");
        }
        return content.toString();
    }

    private String generateGetType(String name) {
        return "\n    @Override\n" +
                "    public String getType() { return \"" + name + "\"; }\n";
    }

    private String generateIsPrimitive() {
        return "\n    @Override\n" +
                "    public boolean isPrimitive() { return false; }\n";
    }

    private String generateIsRequirement(boolean isRequirement) {
        return "\n    @Override\n" +
                "    public boolean isRequirement() { return " + isRequirement + "; }\n";
    }

    private String generateEquals(String name, Map<String, String> fields2type) {
        String result = fields2type.keySet().stream()
                .map(f -> f + ".equal(that." + f + ").getValue()")
                .collect(Collectors.joining(" &&\n               "));
        return  "\n    @Override\n" +
                "    public BoolEntity equal(BaseEntity entity) {\n" +
                "        if (! getType().equals(entity.getType())) {\n" +
                "            return new BoolEntity(false);\n" +
                "        }\n" +
                "        " + name + " that = (" + name + ") entity;\n" +
                "        return (" + result + ")\n" +
                "               ? (new BoolEntity(true)) : (new BoolEntity(false));\n" +
                "    }\n";
    }

    private String generateFileEnd() {
        return "}";
    }
}
