package process.parser;

import exception.TokenInvalidException;
import util.Configs;
import util.Formats;
import util.TextReader;

import java.io.IOException;
import java.util.ArrayList;
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
            else if (! Formats.DEFINE_FUNCTION.equals(type) && ! Formats.DEFINE_RULE.equals(type) ) {
                throw new TokenInvalidException(type, List.of(Formats.DEFINE_FUNCTION, Formats.DEFINE_RULE));
            }
            name = TextReader.nextToken();
            Formats.CUSTOMIZED_OPERATORS.add(name);
            RuleJsonGenerator.generateRule(name, type);
        }
    }

    static void parseArguments() throws IOException {
        TextReader.nextTokenWithTest(Formats.OPEN_PAREN);
        List<List<String>> arguments = new ArrayList<>();
        while (true) {
            List<String> argument = new ArrayList<>();
            String next = TextReader.nextToken();
            argument.add(next);
            next = TextReader.nextToken();
            while (Formats.COMMA.equals(next)) {
                argument.add(TextReader.nextToken());
                next = TextReader.nextToken();
            }
            arguments.add(argument);
            if (Formats.CLOSE_PAREN.equals(next)) {
                break;
            }
            else {
                TextReader.nextTokenWithTest(Formats.SEPARATOR);
            }
        }
        RuleJsonGenerator.generateArguments(arguments);
    }

    static void parseReturn() throws IOException {
        TextReader.nextTokenWithTest(Formats.ARROW);
        RuleJsonGenerator.generateReturn(TextReader.nextToken());
    }

    static void parseLogic() throws IOException {
        TextReader.nextTokenWithTest(Formats.OPEN_BRACE);
        parseTerm();
        TextReader.nextTokenWithTest(Formats.CLOSE_BRACE);
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
        else if (Formats.LOOP_SIGNAL.equals(preReadToken)) {

        }
        else {
            throw new TokenInvalidException("Token should be contained in operators, view all operators in file \"formats.properties\".");
        }
    }

}
