package codeGenerator;

import dynamics.TypeManager;
import lombok.extern.slf4j.Slf4j;
import util.ModeEnum;
import util.PathConsts;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import util.FormatsConsts;
import util.TypeEnum;

/**
 * Parse recognition rules from file.
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
@Slf4j
public final class RuleParser extends StdDeserializer<Object> {

    public RuleParser() {
        this(null);
    }

    protected RuleParser(Class<?> vc) {
        super(vc);
    }

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        String contents = GeneralJavaHeaderGenerator.generateImports()
            + GeneralJavaHeaderGenerator.generateJavadoc(PathConsts.RULE_JAVA_NAME, PathConsts.file(ModeEnum.RULE, TypeEnum.JSON))
            + String.format("public final class %s {\n\n", PathConsts.RULE_JAVA_CLASS)
            + parseRules(root)
            + "}\n";
        addToJavaFile(contents);
        log.info("Finish parse rule JSON file: " + PathConsts.file(ModeEnum.RULE, TypeEnum.JSON));
        return null;
    }

    private void addToJavaFile(String contents){
        String path = PathConsts.RULE_JAVA_FILE;
        try {
            File file = new File(PathConsts.RULE_JAVA_FILE);
            if (! file.createNewFile()) {
                log.warn(String.format("Replace \"%s\" before.", PathConsts.RULE_JAVA_NAME));
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(contents);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            log.error("Can not write file: " + path, e);
        }
    }

    private String parseRules(JsonNode root) {
        StringBuilder rules = new StringBuilder();
        root.fields().forEachRemaining(entry -> {
            String name = entry.getKey();
            String rule = parseRule(name, entry.getValue());
            rules.append(rule);
        });
        return rules.toString();
    }

    private String parseRule(String name, JsonNode ruleNode) {
        StringBuilder rule = new StringBuilder();
        boolean isRule = Objects.equals(FormatsConsts.DEFINE_RULE, ruleNode.get(FormatsConsts.RULE_TYPE_FIELD).asText());
        String returnType = TypeManager.type2class(ruleNode.get(FormatsConsts.RULE_RETURN_FIELD).asText());
        List<String> arguments = parseArgument(ruleNode.get(FormatsConsts.RULE_ARGUMENT_FIELD));
        String logic = LogicParser.parse(ruleNode.get(FormatsConsts.RULE_LOGIC_FIELD));
        rule.append(String.format("    /**\n     * %s\n     * @return %s\n     */\n", name, returnType));
        for (String args: arguments) {
            rule.append(generateRule(name, args, returnType, logic, isRule));
        }
        rule.append('\n');
        return rule.toString();
    }

    private List<String> parseArgument(JsonNode node) {
        List<String> result = new ArrayList<>();
        for (JsonNode arguments: node) {
            List<String> temp = new ArrayList<>();
            for (int i = 0; i < arguments.size(); i++) {
                temp.add(String.format("%s $%d$", TypeManager.type2class(arguments.get(i).asText()), i + 1));
            }
            result.add(String.join(", ", temp));
        }
        return result;
    }

    private String generateRule(String name, String argument, String returns, String logic, boolean isRule) {
        return String.format("    %s static %s %s(%s) {\n        return %s;\n    }\n",
            (isRule ? "public": "private"), returns, name, argument, logic);
    }
}
