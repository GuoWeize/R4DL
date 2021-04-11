package process.parser;

import util.Configs;
import util.Formats;
import util.LanguageFormatException;
import util.TextReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Guo Weize
 * @date 2021/4/3
 */
public final class RuleTextParser {

    private static final Set<String> CUSTOMIZED_OPERATORS = new HashSet<>();
    private static final Set<String> UNITARY_OPERATORS = new HashSet<>();
    private static final Set<String> BINARY_OPERATORS = new HashSet<>();
    private static final Set<String> MULTIPLE_OPERATORS = new HashSet<>();
    static {
        MULTIPLE_OPERATORS.add(Formats.SUM_OF_NUMBERS);
        MULTIPLE_OPERATORS.add(Formats.PRODUCT_OF_NUMBERS);
        MULTIPLE_OPERATORS.add(Formats.AND_BOOL_OPERATE);
        MULTIPLE_OPERATORS.add(Formats.OR_BOOL_OPERATE);
        MULTIPLE_OPERATORS.add(Formats.COLLECTION_MERGE);
    }

    /**
     * entry of the function of parsing the rule text file.
     */
    public static void parseRuleFile() {
        TextReader.readFile(Configs.RULE_TEXT_FILE);
        RuleJsonGenerator.initialization();
        try {
            RuleJsonGenerator.generateRules();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void parseRules() throws IOException {
        String type, name;
        while (true) {
            type = TextReader.nextToken();
            if (TextReader.EMPTY_STRING.equals(type)) {
                break;
            }
            else if (! Formats.FUNCTION_DEFINE.equals(type) && ! Formats.RULE_DEFINE.equals(type) ) {
                throw new LanguageFormatException(type, Formats.FUNCTION_DEFINE + "\" or \"" + Formats.RULE_DEFINE);
            }
            name = TextReader.nextToken();
            CUSTOMIZED_OPERATORS.add(name);
            RuleJsonGenerator.generateRule(name, type);
        }
    }

    static void parseArguments() throws IOException {
        TextReader.nextTokenWithTest(TextReader.OPEN_PAREN);
        List<List<String>> arguments = new ArrayList<>();
        while (true) {
            List<String> argument = new ArrayList<>();
            String next = TextReader.nextToken();
            argument.add(next);
            next = TextReader.nextToken();
            while (TextReader.COMMA.equals(next)) {
                argument.add(TextReader.nextToken());
                next = TextReader.nextToken();
            }
            arguments.add(argument);
            if (TextReader.CLOSE_PAREN.equals(next)) {
                break;
            }
            else {
                TextReader.nextTokenWithTest(TextReader.DIV);
            }
        }
        RuleJsonGenerator.generateArguments(arguments);
    }

    static void parseReturn() throws IOException {
        TextReader.nextTokenWithTest(TextReader.ARROW);
        RuleJsonGenerator.generateReturn(TextReader.nextToken());
    }

    static void parseLogic() throws IOException {
        TextReader.nextTokenWithTest(TextReader.OPEN_BRACE);
        parseTerm();
        TextReader.nextTokenWithTest(TextReader.CLOSE_BRACE);
    }

    static void parseTerm() throws IOException {
        String preReadToken = TextReader.nextToken();
        if (TextReader.OPEN_PAREN.equals(preReadToken)) {

        }
        else if (MULTIPLE_OPERATORS.contains(preReadToken) || CUSTOMIZED_OPERATORS.contains(preReadToken)) {
            RuleJsonGenerator.generatePreOperator(preReadToken);
        }
        else {

        }
    }

}
