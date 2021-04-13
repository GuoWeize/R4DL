package process.parser;

import util.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Guo Weize
 * @date 2021/4/3
 */
public final class RuleTextParser {

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
                throw new TokenNotExpectationException(type, Arrays.asList(Formats.FUNCTION_DEFINE, Formats.RULE_DEFINE));
            }
            name = TextReader.nextToken();
            Formats.CUSTOMIZED_OPERATORS.add(name);
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
        if (Formats.UNITARY_OPERATORS.contains(preReadToken)) {

        }
        else if (Formats.BINARY_OPERATORS.contains(preReadToken)) {

        }
        else if (Formats.MULTIPLE_ARG_OPERATORS.contains(preReadToken)
                || Formats.CUSTOMIZED_OPERATORS.contains(preReadToken)) {

        }
        else if (Formats.LOOP_BEGIN_SIGNAL.equals(preReadToken)) {

        }
        else {
            throw new TokenInvalidException(preReadToken,
                    "token should be contained in operators, view all operators in file \"formats.properties\".");
        }
    }

}
