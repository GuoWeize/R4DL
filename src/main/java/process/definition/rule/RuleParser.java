package process.definition.rule;

import base.dynamics.TypeManager;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author gwz
 */
public final class RuleParser extends StdDeserializer<Object> {

    private static final String FUNCTION_SIGNAL = "function";
    private static final String RULE_SIGNAL = "rule";
    private static final String ARGUMENT_SIGNAL = "argument";
    private static final String LOGIC_SIGNAL = "logic";
    private static final String RETURN_SIGNAL = "return";

    public static final String PROJECT_PATH = System.getProperty("user.dir");
    public static final String JAVA_FILE_PATH = PROJECT_PATH + "/src/main/resources/definitionJava/";

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
        String contents = "import base.type.BaseEntity;\n" +
                "import base.type.primitive.*;\n" +
                "import base.type.collection.*;\n" +
                "import java.util.stream.IntStream;\n\n" +
                "public class Rule {\n\n" +
                parseRoot(root.get(FUNCTION_SIGNAL), false) +
                parseRoot(root.get(RULE_SIGNAL), true) +
                "}\n";
        addToJavaFile(contents);
        return null;
    }

    private String parseRoot(JsonNode node, boolean isRule) throws IOException {
        Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
        StringBuilder contents = new StringBuilder();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = iterator.next();
            String name = entry.getKey();
            JsonNode n = entry.getValue();
            List<String> arguments = parseArgument(n.get(ARGUMENT_SIGNAL));
            String returns = parseReturn(n.get(RETURN_SIGNAL));
            String logic = FunctionParser.parse(n.get(LOGIC_SIGNAL));
            for (String argument: arguments) {
                contents.append(generateRuleFunction(name, argument, returns, logic, isRule));
            }
        }
        return contents.toString();
    }

    private List<String> parseArgument(JsonNode node) {
        List<String> result = new ArrayList<>();
        for (JsonNode arguments: node) {
            List<String> temp = new ArrayList<>();
            for (int i = 0; i < arguments.size(); i++) {
                temp.add(TypeManager.type2class(arguments.get(i).asText()) + " _" + (i+1));
            }
            result.add(String.join(", ", temp));
        }
        return result;
    }

    private String parseReturn(JsonNode node) {
        return TypeManager.type2class(node.asText());
    }

    private void addToJavaFile(String contents) throws IOException {
        File file = new File(JAVA_FILE_PATH + "Rule.java");
        if (! file.createNewFile()) {
            System.out.println("Replace file Rule.java" + " before.");
        }
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(contents);
        fileWriter.flush();
        fileWriter.close();
    }

    private String generateRuleFunction(String name, String argument, String returns, String logic, boolean isRule) {
        String contents = "    ";
        contents += (isRule ? "public": "private");
        contents += " static " + returns + " " + name + "(" + argument + ") {\n";
        contents += "        return " + logic + ";\n";
        contents += "    }\n\n";
        return contents;
    }



}
