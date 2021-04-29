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

    private static final String NONE_PRE_TOKEN = "";

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

    static String parseTerm() throws IOException {
        String nextToken = TextReader.nextToken();
        if (Formats.OPEN_BRACE.equals(nextToken)) {
            TextReader.nextTokenWithTest(Formats.CLOSE_BRACE);
            return parseUnitaryBinary();
        }
        else if (Formats.MULTIPLE_ARG_OPERATORS.contains(nextToken) ||
                Formats.CUSTOMIZED_OPERATORS.contains(nextToken)) {
            return parseMultipleArg(nextToken);
        }
        else if (Formats.LOOP_SIGNAL.equals(nextToken)) {
            return parseLoop();
        }
        else {
            return parseElement(nextToken);
        }
    }

    /**
     * Parse unitary or binary operators.
     * @return json format logic
     */
    static String parseUnitaryBinary() throws IOException {
        String firstToken = TextReader.nextToken();
        if (Formats.UNITARY_OPERATORS.contains(firstToken)) {
            String object = parseTerm();
            return "{\"" + firstToken + "\": " + object + "}";
        }
        else {
            String object1 = parseTerm();
            String operator = TextReader.nextToken();
            if (! Formats.BINARY_OPERATORS.contains(operator)) {
                throw new TokenInvalidException("Binary operator \"" + operator + "\" is not defined.");
            }
            String object2 = parseTerm();
            return "{\"" + operator + "\": [" + object1 + ", " + object2 + "]}";
        }
    }

    /**
     * Parse multiple arguments operators.
     * @return json format logic
     */
    static String parseMultipleArg(String operator) throws IOException {
        TextReader.nextTokenWithTest(Formats.OPEN_PAREN);
        List<String> args = new ArrayList<>();
        while (true) {
            args.add(parseTerm());
            String nextToken = TextReader.nextToken();
            if (Formats.CLOSE_PAREN.equals(nextToken)) {
                break;
            }
            else if (! Formats.COMMA.equals(nextToken)) {
                throw new TokenInvalidException(nextToken, Formats.COMMA);
            }
        }
        return "{\"" + operator + "\": [" + String.join(Formats.COMMA, args) + "]}";
    }

    /**
     * Parse loop operators.
     * @return json format logic
     */
    static String parseLoop() throws IOException {
        // <loop statement> := for (all | any) <statement> <range> '(' <statement> ')'
        // <range> := in <statement> | from <statement> to <statement>
        String quantifier = TextReader.nextToken();
        if (! Formats.ALL_SATISFY.equals(quantifier) && ! Formats.ANY_SATISFY.equals(quantifier)) {
            throw new TokenInvalidException(quantifier, List.of(Formats.ALL_SATISFY, Formats.ANY_SATISFY));
        }
        quantifier = "\"" + quantifier + "\"";
        String loopVariable = "\"" + parseElement(NONE_PRE_TOKEN) + "\"";
        String range;
        String nextToken = TextReader.nextToken();
        if (Formats.RANGE_SIGNAL.equals(nextToken)) {
            range = "\"" + Formats.RANGE_SIGNAL + TextReader.nextToken() + "\"";
        }
        else if (Formats.RANGE_BEGIN_SIGNAL.equals(nextToken)) {
            range = TextReader.nextToken();
            TextReader.nextTokenWithTest(Formats.RANGE_END_SIGNAL);
            range = "\"" + range + TextReader.nextToken() + "\"";
        }
        else {
            throw new TokenInvalidException(nextToken, List.of(Formats.RANGE_SIGNAL, Formats.RANGE_BEGIN_SIGNAL));
        }
        TextReader.nextTokenWithTest(Formats.OPEN_PAREN);
        String logicalBody = parseTerm();
        TextReader.nextTokenWithTest(Formats.CLOSE_PAREN);
        return "{" + quantifier + ": [" + loopVariable + "," + range + "," + logicalBody + "]}";
    }

    /**
     * Parse single element, including: basic elements and statement '[' statement ']'
     * @param preReadToken if there is a token pre-read, {@value NONE_PRE_TOKEN} if not
     * @return json format element
     */
    static String parseElement(String preReadToken) throws IOException {
        String nextToken;
        if (! NONE_PRE_TOKEN.equals(preReadToken)) {
            nextToken = preReadToken;
        }
        else {
            nextToken = TextReader.nextToken();
        }
        return nextToken;
    }

}
