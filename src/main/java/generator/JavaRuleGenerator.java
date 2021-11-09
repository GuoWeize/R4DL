package generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * Parse recognition rules from file.
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
@Slf4j
public final class JavaRuleGenerator extends StdDeserializer<Object> {

    public JavaRuleGenerator() {
        this(null);
    }

    private JavaRuleGenerator(Class<?> vc) {
        super(vc);
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

        StringBuilder rules = new StringBuilder();
        for (JsonNode ruleNode: nodes) {
            rules.append(parseRule(ruleNode));
        }
        String contents = JavaHeaderGenerator.generateImports()
            + JavaHeaderGenerator.generateJavadoc("rule.java")
            + "public final class rule {\n\n"
            + rules
            + "}\n";
        addToJavaFile(name, contents);
        log.info("Finish parse rule JSON file: ");
        return null;
    }

    private void addToJavaFile(String packageName, String contents){
        String path = "/Users/gwz/Desktop/Code/R4DL/src/main/generated/" + packageName + "/rule.java";
        try {
            File file = new File(path);
            if (! file.createNewFile()) {
                log.warn(String.format("Replace \"%s\" before.", path));
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(contents);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            log.error("Can not write file: " + path, e);
        }
    }

    private String parseRule(JsonNode node) {
        StringBuilder rule = new StringBuilder();
        boolean isRule = node.get("?").asBoolean();
        String name = node.get("#").asText();
        String arguments = parseArgument(node.get("$"));
        String returnType = JavaClassGenerator.parseType(node.get("*"));
        String logic = LogicGenerator.parse(node.get("~"));
        rule.append(String.format("    /**\n     * %s\n     * @return %s\n     */\n", name, returnType));
        rule.append(generateRule(name, arguments, returnType, logic, isRule));
        rule.append('\n');
        return rule.toString();
    }

    private String parseArgument(JsonNode node) {
        List<String> arguments = new ArrayList<>();
        int index = 1;
        for (JsonNode argument: node) {
            arguments.add(JavaClassGenerator.parseType(argument) + " $" + index + "$");
            index += 1;
        }
        return String.join(", ", arguments);
    }

    private String generateRule(String name, String argument, String returns, String logic, boolean isRule) {
        return String.format("    %s static %s %s(%s) {\n        return %s;\n    }\n",
            (isRule ? "public": "private"), returns, name, argument, logic);
    }

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Object.class, new JavaRuleGenerator());
        mapper.registerModule(module);

        File file = new File("/Users/gwz/Desktop/Code/R4DL/src/main/resources/rules/basic/rule.json");
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
