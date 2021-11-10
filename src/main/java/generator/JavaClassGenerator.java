package generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import parser.TypeManager;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
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
@Slf4j
public final class JavaClassGenerator extends StdDeserializer<Object> {

    private static final Set<String> TYPES = new HashSet<>(16);

    private JavaClassGenerator(Class<?> vc) {
        super(vc);
    }

    public JavaClassGenerator() {
        this(null);
    }

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        List<String> temp = new ArrayList<>();
        root.fieldNames().forEachRemaining(temp::add);
        String name = temp.get(0);
        JsonNode nodes = root.get(name);
        JavaHeaderGenerator.setPackageName(name);
        nodes.forEach(this::customizedType);
        log.info("Finish parse model JSON file: ");
        return null;
    }

    private void customizedType(JsonNode node) {
        Map<String, String> fields2type = new HashMap<>(8);
        Map<String, String> fields2value = new HashMap<>(8);
        boolean isRequirement = node.get("?").asBoolean();
        String name = node.get("#").asText();
        TypeManager.add(name, isRequirement);
        node.fields().forEachRemaining(entry -> {
            if (!Objects.equals(entry.getKey(), "?") && !Objects.equals(entry.getKey(), "#")) {
                String fieldName = entry.getKey();
                JsonNode field = entry.getValue();
                fields2type.put(fieldName, parseType(field));
                fields2value.put(fieldName, parseValue(field));
            }
        });
        generateJavaFile(name, fields2type, fields2value);
    }

    static String parseType(JsonNode node) {
        if (node.isTextual()) {
            String type = node.asText();
            if (Objects.equals(type, "boolean")) {
                return "BoolEntity";
            }
            else if (Objects.equals(type, "integer")) {
                return "IntEntity";
            }
            else if (Objects.equals(type, "float")) {
                return "FloatEntity";
            }
            else if (Objects.equals(type, "string")) {
                return "StringEntity";
            }
            else {
                return "c_" + type;
            }
        }
        else if (node.has("()")) {
            return "SetEntity<" + parseType(node.get("()")) + ">";
        }
        else if (node.has("[]")) {
            return "ListEntity<" + parseType(node.get("[]")) + ">";
        }
        else if (node.has("K") && node.has("V")) {
            return "MapEntity<" + parseType(node.get("K")) + ", " + parseType(node.get("V")) + ">";
        }
        else if (node.has("boolean")) {
            return "BoolEntity";
        }
        else if (node.has("integer")) {
            return "IntEntity";
        }
        else if (node.has("float")) {
            return "FloatEntity";
        }
        else if (node.has("string")) {
            return "StringEntity";
        }
        return null;
    }

    private String parseValue(JsonNode node) {
        if (node.has("boolean")) {
            String value = Boolean.toString(node.get("boolean").asBoolean(false));
            return "BoolEntity.valueOf(" + value + ")";
        }
        else if (node.has("integer")) {
            String value = Integer.toString(node.get("integer").asInt(0));
            return "IntEntity.valueOf(" + value + ")";
        }
        else if (node.has("float")) {
            String value = Double.toString(node.get("float").asDouble(0.0));
            return "FloatEntity.valueOf(" + value + ")";
        }
        else if (node.has("string")) {
            String value = node.get("string").asText("");
            return "StringEntity.valueOf(\"" + value + "\")";
        }
        else if (node.has("()")) {
            return "(" + parseType(node) + ") SetEntity.newInstance()";
        }
        else if (node.has("[]")) {
            return "(" + parseType(node) + ") ListEntity.newInstance()";
        }
        else if (node.has("K") && node.has("V")) {
            return "(" + parseType(node) + ") MapEntity.newInstance()";
        }
        else {
            String type = node.asText();
            return "c_" + type + ".NULL";
        }
    }

    private void generateJavaFile(String name, Map<String, String> fields2type, Map<String, String> fields2value) {
        String className = "c_" + name;
        StringBuilder content = new StringBuilder()
            .append(JavaHeaderGenerator.generateImports())
            .append(JavaHeaderGenerator.generateJavadoc(className + ".java"))
            .append(generateFileHead(className))
            .append(generateFields(fields2type, fields2value))
            .append(generateGetType(name))
            .append(generateIsNULL())
            .append(generateEquals(className, fields2type))
            .append(generateNewInstance(className))
            .append(generateFileEnd());
        String path = "/Users/gwz/Desktop/Code/R4DL/src/main/generated/basic/" + className + ".java";
        try {
            File file = new File(path);
            if (!file.createNewFile()) {
                log.warn(String.format("Replace existing file \"%s.java\".", className));
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            log.error("Can not write file: " + path, e);
        }
    }

    private String generateFileHead(String name) {
        return String.format("public final class %s extends BaseEntity {\n    public static %s NULL = new %s();\n\n", name, name, name);
    }

    private String generateFields(Map<String, String> fields2type, Map<String, String> fields2value) {
        StringBuilder content = new StringBuilder();
        for (Map.Entry<String, String> entry: fields2type.entrySet()) {
            String filed = entry.getKey();
            String fieldName = "f_" + filed;
            content.append("    public ")
                .append(entry.getValue())
                .append(' ')
                .append(fieldName)
                .append(" = ")
                .append(fields2value.getOrDefault(filed, "null"))
                .append(";\n");
        }
        return content.append('\n').toString();
    }

    private String generateGetType(String name) {
        return String.format("    @Override\n    public String getType() {\n        return \"%s\";\n    }\n\n", name);
    }

    private String generateIsNULL() {
        return "    @Override\n    public BoolEntity isNull() {\n        return this.equal(NULL);\n    }\n\n";
    }

    private String generateIsRequirement(boolean isRequirement) {
        return String.format("    @Override\n    public boolean isRequirement() {\n        return %b;\n    }\n\n", isRequirement);
    }

    private String generateEquals(String name, Map<String, String> fields2type) {
        String result = fields2type.keySet().stream()
            .map(field -> String.format("%s.equal(that.%s)", "f_" + field, "f_" + field))
            .collect(Collectors.joining(",\n            "));
        return "    @Override\n"
            + "    public BoolEntity equal(BaseEntity entity) {\n"
            + "        if (! getType().equals(entity.getType())) {\n"
            + "            return BoolEntity.FALSE;\n"
            + "        }\n"
            + String.format("        %s that = (%s)entity;\n", name, name)
            + "        if (this == NULL) {\n"
            + "            return BoolEntity.valueOf(that == NULL);\n"
            + "        }\n"
            + "        if (that == NULL) {\n"
            + "            return BoolEntity.valueOf(false);\n"
            + "        }\n"
            + "        return BoolEntity.and(\n"
            + String.format("            %s\n", result)
            + "        );\n"
            + "    }\n\n";
    }

    private String generateNewInstance(String name) {
        return String.format(
            "    public static %s newInstance() {\n        return new %s();\n    }\n\n",
            name, name);
    }

    private String generateFileEnd() {
        return "}\n";
    }

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Object.class, new JavaClassGenerator());
        mapper.registerModule(module);

        File file = new File("/Users/gwz/Desktop/Code/R4DL/src/main/resources/models/basic/model.json");
        long length = file.length();
        byte[] content = new byte[(int) length];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(content);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String value = new String(content);
        try {
            mapper.readValue(value, Object.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
