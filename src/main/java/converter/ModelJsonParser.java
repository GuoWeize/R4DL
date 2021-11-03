package converter;

import dynamics.TypeManager;
import exceptions.TokenInvalidException;
import lombok.extern.slf4j.Slf4j;
import util.ModeEnum;
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
import util.TypeEnum;

/**
 * Parse entity models from file.
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
@Slf4j
public final class ModelJsonParser extends StdDeserializer<Object> {

    private final static Set<String> PRIMITIVE_TYPES = Set.of(
        "BoolEntity", "IntEntity", "FloatEntity", "StringEntity"
    );

    private ModelJsonParser(Class<?> vc) {
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
        log.info("Finish parse model JSON file: " + PathConsts.file(ModeEnum.MODEL, TypeEnum.JSON));
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
        StringBuilder content = new StringBuilder()
            .append(JavaHeaderGenerator.generateImports())
            .append(JavaHeaderGenerator.generateJavadoc(name + ".java", PathConsts.file(ModeEnum.MODEL, TypeEnum.JSON)))
            .append(generateFileHead(name))
            .append(generateFields(name, fields2type))
            .append(generateGetType(name))
            .append(generateIsRequirement(isRequirement))
            .append(generateEquals(name, fields2type))
            .append(generateNewInstance(name))
            .append(generateFileEnd());
        String path = PathConsts.DYNAMICS_JAVA_CODE_DIR + name + ".java";
        try {
            File file = new File(path);
            if (!file.createNewFile()) {
                log.warn(String.format("Replace existing file \"%s.java\".", name));
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
        return String.format("public final class %s extends BaseEntity {\n", name);
    }

    private String setDefaultValue(String type, String defaultValue) {
        if (defaultValue == null) {
            if (type.startsWith("SetEntity<")) {
                return String.format("(%s) SetEntity.newInstance()", type);
            }
            if (type.startsWith("ListEntity<")) {
                return String.format("(%s) ListEntity.newInstance()", type);
            }
            if (type.startsWith("MapEntity<")) {
                return String.format("(%s) MapEntity.newInstance()", type);
            }
            if (PRIMITIVE_TYPES.contains(type)) {
                return String.format("%s.newInstance()", type);
            }
            return "null";
        }
        else {
            if (PRIMITIVE_TYPES.contains(type)) {
                return String.format("%s.valueOf(%s)", type, defaultValue);
            }
            return "null";
        }
    }

    private String generateFields(String name, Map<String, String> fields2type) {
        StringBuilder content = new StringBuilder();
        for (Map.Entry<String, String> entry: fields2type.entrySet()) {
            String type = TypeManager.type2class(entry.getValue());
            String filedName = entry.getKey();
            content.append("    public ")
                .append(type)
                .append(' ')
                .append(filedName)
                .append(" = ")
                .append(setDefaultValue(type, null))
                .append(";\n");
        }
        return content.append('\n').toString();
    }

    private String generateGetType(String name) {
        return String.format("    @Override\n    public String getType() {\n        return \"%s\";\n    }\n\n", name);
    }

    private String generateIsRequirement(boolean isRequirement) {
        return String.format("    @Override\n    public boolean isRequirement() {\n        return %b;\n    }\n\n", isRequirement);
    }

    private String generateEquals(String name, Map<String, String> fields2type) {
        String result = fields2type.keySet().stream()
            .map(field -> String.format("BaseEntity.equal(%s, that.%s)", field, field))
            .collect(Collectors.joining(",\n            "));
        return new StringBuilder("    @Override\n")
            .append("    public BoolEntity equal(BaseEntity entity) {\n")
            .append("        if (! getType().equals(entity.getType())) {\n")
            .append("            return BoolEntity.FALSE;\n")
            .append("        }\n")
            .append(String.format("        %s that = (%s)entity;\n", name, name))
            .append("        return BoolEntity.and(\n")
            .append(String.format("            %s\n", result))
            .append("        );\n")
            .append("    }\n")
            .toString();
    }

    private String generateNewInstance(String name) {
        return String.format(
            "    public static %s newInstance() {\n        return new %s();\n    }\n\n",
            name, name);
    }

    private String generateFileEnd() {
        return "}\n";
    }
}
