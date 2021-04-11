package process.parser;

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
            else if (! Formats.FUNCTION_DEFINE.equals(type) && ! Formats.RULE_DEFINE.equals(type) ) {
                throw new IllegalArgumentException();
            }
            name = TextReader.nextToken();
            RuleJsonGenerator.generateRule(name, type);
        }
    }

    static void parseArguments() throws IOException {
        String symbol = TextReader.nextToken();
        if (! TextReader.OPEN_PAREN.equals(symbol)) {
            throw new IllegalArgumentException();
        }
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
            else if (! TextReader.DIV.equals(next)) {
                throw new IllegalArgumentException();
            }
        }
        RuleJsonGenerator.generateArguments(arguments);
    }

    static void parseReturn() throws IOException {
        String symbol = TextReader.nextToken();
        if (! TextReader.ARROW.equals(symbol)) {
            throw new IllegalArgumentException();
        }
        symbol = TextReader.nextToken();
        RuleJsonGenerator.generateReturn(symbol);
    }

    static void parseLogic() throws IOException {
        String symbol = TextReader.nextToken();
        if (TextReader.OPEN_BRACE.equals(symbol)) {
            String operator = TextReader.nextToken();
            if (Formats.SUM_OF_NUMBERS.equals(operator)) {

            }
            else if () {

            }

        }
        else if (TextReader.OPEN_PAREN.equals(symbol)) {

        }
        else {
            throw new IllegalArgumentException();
        }
    }

}
