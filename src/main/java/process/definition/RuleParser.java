package process.definition;

import base.dynamics.TypeManager;
import util.PathConsts;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import util.FormatsConsts;

/**
 * Parse recognition rules from file.
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
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
        String contents = generateJavadoc()
            + GeneralJavaHeaderGenerator.generateJavadoc(PathConsts.RULE_JAVA_NAME, PathConsts.RULE_JSON_FILE)
            + "public class Rule {\n\n"
            + parseRoot(root.get(FormatsConsts.DEFINE_FUNCTION), false)
            + parseRoot(root.get(FormatsConsts.DEFINE_RULE), true)
            + "}\n";
        addToJavaFile(contents);
        return null;
    }

    private String generateJavadoc() {
        return "import base.type.BaseEntity;\nimport base.type.primitive.*;\nimport base.type.collection.*;\n"
            + "import java.util.stream.IntStream;\n\n";
    }

    private String parseRoot(JsonNode node, boolean isRule) {
        StringBuilder contents = new StringBuilder();
        node.fields().forEachRemaining(entry -> {
            String name = entry.getKey();
            JsonNode n = entry.getValue();
            List<String> arguments = parseArgument(n.get(FormatsConsts.RULE_ARGUMENT_FIELD));
            String returns = parseReturn(n.get(FormatsConsts.RULE_RETURN_FIELD));
            String logic = FunctionParser.parse(n.get(FormatsConsts.RULE_LOGIC_FIELD));
            for (String argument: arguments) {
                contents.append(generateRuleFunction(name, argument, returns, logic, isRule));
            }
        });
        return contents.toString();
    }

    private List<String> parseArgument(JsonNode node) {
        List<String> result = new ArrayList<>();
        for (JsonNode arguments: node) {
            List<String> temp = new ArrayList<>();
            for (int i = 0; i < arguments.size(); i++) {
                temp.add(String.format("%s _%d", TypeManager.type2class(arguments.get(i).asText()), i + 1));
            }
            result.add(String.join(", ", temp));
        }
        return result;
    }

    private String parseReturn(JsonNode node) {
        return TypeManager.type2class(node.asText());
    }

    private void addToJavaFile(String contents){
        try {
            File file = new File(PathConsts.RULE_JAVA_FILE);
            if (! file.createNewFile()) {
                System.out.printf("Replace \"%s\" before.%n", PathConsts.RULE_JAVA_NAME);
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(contents);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateRuleFunction(String name, String argument, String returns, String logic, boolean isRule) {
        return String.format("    %s static %s %s(%s) {\n        return %s;\n    }\n\n",
            (isRule ? "public": "private"), returns, name, argument, logic);
    }
}