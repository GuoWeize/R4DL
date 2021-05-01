package process.parser;

import exceptions.TokenInvalidException;
import exceptions.TypeInvalidException;
import util.PathConsts;
import util.FormatsConsts;
import util.TextReader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Guo Weize
 * @date 2021/4/3
 */
public final class RuleTextParser {

    private static final List<String> RULES = new ArrayList<>();
    private static final String NONE_PRE_TOKEN = "";
    private static final Set<Character> nums = Set.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.');

    /**
     * entry of the function of parsing the rule text file.
     */
    public static void parseRuleFile() {
        TextReader.readFile(PathConsts.RULE_TEXT_FILE);
        try {
            String result = String.format("{%s}", parseRules());
            Files.write(Paths.get("demo.json"), result.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String parseRules() throws IOException {
        String preRead = TextReader.nextToken();
        while (! Objects.equals(preRead, TextReader.EMPTY_STRING)) {
            parseRule(preRead);
            preRead = TextReader.nextToken();
        }
        return String.join(", ", RULES);
    }

    static void parseRule(String type) throws IOException {
        if (! Objects.equals(type, FormatsConsts.DEFINE_FUNCTION) && ! Objects.equals(type, FormatsConsts.DEFINE_RULE) ) {
            throw new TokenInvalidException(type, List.of(FormatsConsts.DEFINE_FUNCTION, FormatsConsts.DEFINE_RULE));
        }
        String name = TextReader.nextToken();
        FormatsConsts.CUSTOMIZED_OPERATORS.add(name);
        String head = String.format("\"%s\": {\"%s\": \"%s\", ", name, FormatsConsts.RULE_TYPE_FIELD, type);
        String arguments = String.format("\"%s\": %s, ", FormatsConsts.RULE_ARGUMENT_FIELD, parseArguments());
        String returnType = String.format("\"%s\": \"%s\", ", FormatsConsts.RULE_RETURN_FIELD, parseReturn());
        String logic = String.format("\"%s\": %s", FormatsConsts.RULE_LOGIC_FIELD, parseLogic());
        String tail = "}";
        RULES.add(String.format("%s%s%s%s%s", head, arguments, returnType, logic, tail));
    }

    static String parseArguments() throws IOException {
        TextReader.nextTokenWithTest(FormatsConsts.OPEN_PAREN);
        List<List<String>> arguments = new ArrayList<>();
        while (true) {
            List<String> argument = new ArrayList<>();
            String next = TextReader.nextToken();
            argument.add(next);
            next = TextReader.nextToken();
            while (Objects.equals(next, FormatsConsts.COMMA)) {
                argument.add(TextReader.nextToken());
                next = TextReader.nextToken();
            }
            arguments.add(argument);
            if (Objects.equals(next, FormatsConsts.CLOSE_PAREN)) {
                break;
            } else if (! Objects.equals(next, FormatsConsts.SEPARATOR)) {
                throw new TypeInvalidException(next, List.of(FormatsConsts.SEPARATOR, FormatsConsts.CLOSE_PAREN));
            }
        }
        List<String> temp = arguments.stream()
            .map(l -> String.format("[\"%s\"]", String.join("\", \"", l)))
            .collect(Collectors.toList());
        return "[" + String.join(", ", temp) + "]";
    }

    static String parseReturn() throws IOException {
        TextReader.nextTokenWithTest(FormatsConsts.ARROW);
        return TextReader.nextToken();
    }

    static String parseLogic() throws IOException {
        TextReader.nextTokenWithTest(FormatsConsts.OPEN_BRACE);
        String result = parseTerm(NONE_PRE_TOKEN);
        TextReader.nextTokenWithTest(FormatsConsts.CLOSE_BRACE);
        return result;
    }

    static String parseTerm(String preRead) throws IOException {
        String nextToken = Objects.equals(preRead, NONE_PRE_TOKEN) ? TextReader.nextToken(): preRead;
        if (Objects.equals(nextToken, FormatsConsts.OPEN_BRACE)) {
            String result = parseUnitaryBinary();
            TextReader.nextTokenWithTest(FormatsConsts.CLOSE_BRACE);
            return result;
        } else if (FormatsConsts.MULTIPLE_ARG_OPERATORS.contains(nextToken) ||
                FormatsConsts.CUSTOMIZED_OPERATORS.contains(nextToken)) {
            return parseMultipleArg(nextToken);
        } else if (Objects.equals(nextToken, FormatsConsts.LOOP_SIGNAL)) {
            return parseLoop();
        } else {
            return parseElement(nextToken);
        }
    }

    /**
     * Parse unitary or binary operators.
     * @return json format logic
     */
    static String parseUnitaryBinary() throws IOException {
        String firstToken = TextReader.nextToken();
        if (FormatsConsts.UNARY_OPERATORS.contains(firstToken)) {
            String object = parseTerm(NONE_PRE_TOKEN);
            return String.format("{\"%s\": %s}", firstToken, object);
        } else {
            String object1 = parseTerm(firstToken);
            String operator = TextReader.nextToken();
            if (! FormatsConsts.BINARY_OPERATORS.contains(operator)) {
                throw new TokenInvalidException(String.format("Binary operator \"%s\" is not defined.", operator));
            }
            String object2 = parseTerm(NONE_PRE_TOKEN);
            return String.format("{\"%s\": [%s, %s]}", operator, object1, object2);
        }
    }

    /**
     * Parse multiple arguments operators.
     * @return json format logic
     */
    static String parseMultipleArg(String operator) throws IOException {
        TextReader.nextTokenWithTest(FormatsConsts.OPEN_PAREN);
        List<String> args = new ArrayList<>();
        while (true) {
            args.add(parseTerm(NONE_PRE_TOKEN));
            String nextToken = TextReader.nextToken();
            if (Objects.equals(nextToken, FormatsConsts.CLOSE_PAREN)) {
                break;
            } else if (! Objects.equals(nextToken, FormatsConsts.COMMA)) {
                throw new TokenInvalidException(nextToken, List.of(FormatsConsts.COMMA, FormatsConsts.CLOSE_PAREN));
            }
        }
        return String.format("{\"%s\": [%s]}", operator, String.join(FormatsConsts.COMMA, args));
    }

    /**
     * Parse loop operators.
     * @return json format logic
     */
    static String parseLoop() throws IOException {
        // <loop statement> := for (all | any) <statement> <range> '(' <statement> ')'
        // <range> := in <statement> | from <statement> to <statement>
        String quantifier = TextReader.nextToken();
        if (! Objects.equals(quantifier, FormatsConsts.ALL_SATISFY) && ! Objects.equals(quantifier, FormatsConsts.ANY_SATISFY)) {
            throw new TokenInvalidException(quantifier, List.of(FormatsConsts.ALL_SATISFY, FormatsConsts.ANY_SATISFY));
        }
        String loopVariable = parseElement(NONE_PRE_TOKEN);
        String range;
        String nextToken = TextReader.nextToken();
        if (Objects.equals(nextToken, FormatsConsts.RANGE_SIGNAL)) {
            range = FormatsConsts.RANGE_SIGNAL + " " + TextReader.nextToken();
        } else if (Objects.equals(nextToken, FormatsConsts.RANGE_BEGIN_SIGNAL)) {
            range = TextReader.nextToken();
            TextReader.nextTokenWithTest(FormatsConsts.RANGE_END_SIGNAL);
            range = range + TextReader.nextToken();
        } else {
            throw new TokenInvalidException(nextToken, List.of(FormatsConsts.RANGE_SIGNAL, FormatsConsts.RANGE_BEGIN_SIGNAL));
        }
        TextReader.nextTokenWithTest(FormatsConsts.OPEN_BRACE);
        String logicalBody = parseTerm(NONE_PRE_TOKEN);
        TextReader.nextTokenWithTest(FormatsConsts.CLOSE_BRACE);
        return String.format("{\"%s\": [%s, \"%s\", %s]}", quantifier, loopVariable, range, logicalBody);
    }

    /**
     * Parse single element, including: basic elements and statement '[' statement ']'
     * @param preReadToken if there is a token pre-read, {@value NONE_PRE_TOKEN} if not
     * @return json format element
     */
    static String parseElement(String preReadToken) {
        String element;
        if (! Objects.equals(preReadToken, NONE_PRE_TOKEN)) {
            element = preReadToken;
        } else {
            element = TextReader.nextToken();
        }
        String ele;
        while (true) {
            ele = unfoldIndex(element);
            if (Objects.equals(ele, "")) {
                break;
            } else {
                element = ele;
            }
        }
        return String.format("\"%s\"", element);
    }

    private static String unfoldIndex(String s) {
        int count = 0;
        int begin = -1;
        int end = -1;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '[') {
                if (begin < 0) {
                    begin = i;
                }
                count++;
            } else if (s.charAt(i) == ']') {
                count--;
                if (count == 0) {
                    end = i;
                }
            }
        }
        if (begin >= 0) {
            String collection = parseElement(s.substring(0, begin));
            String index = parseElement(s.substring(begin + 1, end));
            String tail = s.substring(end + 1);
            return String.format("%s.get(%s)%s", collection, index, tail);
        }
        return "";
    }

    private static boolean isNumeric(String s) {
        for (char c: s.toCharArray()) {
            if (! nums.contains(c)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        parseRuleFile();
    }

}
