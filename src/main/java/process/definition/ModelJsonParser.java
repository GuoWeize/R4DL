package process.definition;

import base.dynamics.TypeManager;
import exceptions.TokenInvalidException;
import lombok.extern.slf4j.Slf4j;
import util.PathConsts;
import util.FormatsConsts;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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
        Set<String> allTypeName = new HashSet<>(16);
        root.forEach(typeNode -> allTypeName.add(typeNode.get(FormatsConsts.MODEL_NAME_FIELD).asText()));
        TypeManager.initialization(allTypeName);

        for (JsonNode typeNode: root) {
            String name = typeNode.get(FormatsConsts.MODEL_NAME_FIELD).asText();
            String type = typeNode.get(FormatsConsts.MODEL_TYPE_FIELD).asText();
            Map<String, String> fields2type = parseFields(typeNode);
            boolean isRequirement;
            if (Objects.equals(type, FormatsConsts.DEFINE_REQUIREMENT)) {
                isRequirement = true;
            } else if (Objects.equals(type, FormatsConsts.DEFINE_ENTITY)) {
                isRequirement = false;
            } else {
                throw new TokenInvalidException(
                    type, Set.of(FormatsConsts.DEFINE_REQUIREMENT, FormatsConsts.DEFINE_ENTITY)
                );
            }
            generateJavaFile(name, fields2type, isRequirement);
        }
        log.info("Finish parse model JSON file: " + PathConsts.MODEL_JSON_FILE);
        return null;
    }

    private Map<String, String> parseFields(JsonNode node) {
        Map<String, String> fields2type = new HashMap<>(8);
        node.fields().forEachRemaining(entry -> {
            String fieldName = entry.getKey();
            if (!fieldName.equals(FormatsConsts.MODEL_TYPE_FIELD) && !fieldName.equals(FormatsConsts.MODEL_NAME_FIELD)) {
                String type = entry.getValue().asText();
                TypeManager.checkType(type);
                fields2type.put(fieldName, type);
            }
        });
        return fields2type;
    }

    private void generateJavaFile(String name, Map<String, String> fields2type, boolean isRequirement) {
        String content = generateImports()
            + GeneralJavaHeaderGenerator.generateJavadoc(name + ".java", PathConsts.MODEL_JSON_FILE)
            + generateFileHead(name)
            + generateFields(fields2type)
            + generateGetType(name)
            + generateIsPrimitive()
            + generateIsRequirement(isRequirement)
            + generateEquals(name, fields2type)
            + generateFileEnd();
        String path = PathConsts.DYNAMICS_JAVA_CODE_PATH + name + ".java";
        try {
            File file = new File(path);
            if (!file.createNewFile()) {
                log.warn(String.format("Replace existing file \"%s.java\".", name));
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            log.error("Can not write file: " + path, e);
        }
    }

    private String generateImports() {
        return "import base.type.BaseEntity;\nimport base.type.primitive.*;\nimport base.type.collection.*;\n\n";
    }

    private String generateFileHead(String name) {
        return String.format("public final class %s extends BaseEntity {\n", name);
    }

    private String generateFields(Map<String, String> fields2type) {
        StringBuilder content = new StringBuilder();
        for (Map.Entry<String, String> entry: fields2type.entrySet()) {
            content.append("    public ")
                .append(TypeManager.type2class(entry.getValue()))
                .append(' ')
                .append(entry.getKey())
                .append(";\n");
        }
        return content.append('\n').toString();
    }

    private String generateGetType(String name) {
        return String.format("    @Override\n    public String getType() {\n        return \"%s\";\n    }\n\n", name);
    }

    private String generateIsPrimitive() {
        return "    @Override\n    public boolean isPrimitive() {\n        return false;\n    }\n\n";
    }

    private String generateIsRequirement(boolean isRequirement) {
        return String.format("    @Override\n    public boolean isRequirement() {\n        return %b;\n    }\n\n", isRequirement);
    }

    private String generateEquals(String name, Map<String, String> fields2type) {
        String result = fields2type.keySet().stream()
            .map(f -> String.format("%s.equal(that.%s)", f, f))
            .collect(Collectors.joining(",\n            "));
        return  "    @Override\n"
            + "    public BoolEntity equal(BaseEntity entity) {\n"
            + "        if (! getType().equals(entity.getType())) {\n"
            + "            return BoolEntity.FALSE;\n"
            + "        }\n"
            + String.format("        %s that = (%s)entity;\n", name, name)
            + "        return BoolEntity.and(\n"
            + String.format("            %s\n", result)
            + "        );\n"
            + "    }\n";
    }

    private String generateFileEnd() {
        return "}";
    }
}
