package process.definition.model;

import base.dynamics.TypeManager;
import util.Configs;
import util.Formats;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * Parse entity models from file.
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
public final class ModelJsonParser extends StdDeserializer<Object> {

    protected ModelJsonParser(Class<?> vc) {
        super(vc);
    }

    public ModelJsonParser() {
        this(null);
    }

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);

        Set<String> allTypeName = new HashSet<>();
        root.forEach(typeNode -> allTypeName.add(typeNode.get(Formats.MODEL_NAME_FIELD).asText()));
        TypeManager.initialization(allTypeName);

        for (JsonNode typeNode: root) {
            String name = typeNode.get(Formats.MODEL_NAME_FIELD).asText();
            String type = typeNode.get(Formats.MODEL_TYPE_FIELD).asText();
            Map<String, String> fields2type = parseFields(typeNode);
            boolean kind;
            if (type.equals(Formats.DEFINE_REQUIREMENT)) {
                kind = true;
            }
            else if (type.equals(Formats.DEFINE_ENTITY)) {
                kind = false;
            }
            else {
                throw new IllegalArgumentException();
            }

            generateJavaFile(name, fields2type, kind);
            System.out.println(type + " " + name);
        }
        return null;
    }

    private Map<String, String> parseFields(JsonNode node) {
        Map<String, String> fields2type = new HashMap<>(8);
        node.fields().forEachRemaining(entry -> {
            String fieldName = entry.getKey();
            if (!fieldName.equals(Formats.MODEL_TYPE_FIELD) && !fieldName.equals(Formats.MODEL_NAME_FIELD)) {
                String type = entry.getValue().asText();
                TypeManager.checkType(type);
                fields2type.put(fieldName, type);
            }
        });
        return fields2type;
    }

    private void generateJavaFile(String name, Map<String, String> fields2type, boolean isRequirement) {
        String content = generateFileHead(name)
                + generateFields(fields2type)
                + generateGetType(name)
                + generateIsPrimitive()
                + generateIsRequirement(isRequirement)
                + generateEquals(name, fields2type)
                + generateFileEnd();

        try {
            File file = new File(Configs.DYNAMICS_JAVA_CODE_PATH + name + ".java");
            if (!file.createNewFile()) {
                System.out.println("Replace existing file \"" + name + ".java\".");
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateFileHead(String name) {
        return  "import base.type.BaseEntity;\n" +
                "import base.type.primitive.*;\n" +
                "import base.type.collection.*;\n\n" +
                "public class " + name + " extends BaseEntity {\n";
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
                .collect(Collectors.joining(" &&\n                   "));
        return  "\n    @Override\n" +
                "    public BoolEntity equal(BaseEntity entity) {\n" +
                "        if (! getType().equals(entity.getType())) {\n" +
                "            return BoolEntity.FALSE;\n" +
                "        }\n" +
                "        " + name + " that = (" + name + ") entity;\n" +
                "        return BoolEntity.valueOf(\n" +
                "                   " + result + "\n" +
                "        );\n" +
                "    }\n";
    }

    private String generateFileEnd() {
        return "}";
    }
}
