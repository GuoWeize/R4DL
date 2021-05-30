package process.parser;

import exceptions.TokenInvalidException;
import lombok.extern.slf4j.Slf4j;
import process.judge.Processor;
import util.FormatsConsts;
import util.TextReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Guo Weize
 * @date 2021/4/3
 */
@Slf4j
public final class RuleTextParser extends BaseParser {

    private static final List<String> RULES = new ArrayList<>();
    private static final String NONE_PRE_TOKEN = "";

    /**
     * entry of the function of parsing the rule text file.
     */
    protected static String parse() throws IOException {
        String preRead = TextReader.nextToken();
        while (! Objects.equals(preRead, TextReader.EMPTY_STRING)) {
            parseRule(preRead);
            preRead = TextReader.nextToken();
        }
        return String.format("{%s}", String.join(BaseParser.DELIMITER, RULES));
    }

    private static void parseRule(String type) throws IOException {
        List<String> defSymbols = List.of(FormatsConsts.DEFINE_FUNCTION, FormatsConsts.DEFINE_RULE,
            FormatsConsts.DEFINE_REVERSIBLE);
        if (! defSymbols.contains(type)) {
            throw new TokenInvalidException(type, defSymbols);
        }
        String name = TextReader.nextToken();
        FormatsConsts.CUSTOMIZED_OPERATORS.add(name);
        if (Objects.equals(type, FormatsConsts.DEFINE_REVERSIBLE)) {
            type = FormatsConsts.DEFINE_RULE;
            Processor.addReversibleRule(name);
        }
        String head = String.format("\"%s\": {\"%s\": \"%s\", ", name, FormatsConsts.RULE_TYPE_FIELD, type);
        String arguments = String.format("\"%s\": %s, ", FormatsConsts.RULE_ARGUMENT_FIELD, parseArguments());
        if (Objects.equals(type, FormatsConsts.DEFINE_RULE)) {
            int begin = arguments.indexOf("\"list<");
            if (begin > 1) {
                int end = arguments.indexOf(">", begin);
                Processor.addListArg(name, arguments.substring(begin + 6, end));
            }
        }
        String returnType = String.format("\"%s\": \"%s\", ", FormatsConsts.RULE_RETURN_FIELD, parseReturn());
        String logic = String.format("\"%s\": %s", FormatsConsts.RULE_LOGIC_FIELD, parseLogic());
        String tail = "}";
        RULES.add(String.format("%s%s%s%s%s", head, arguments, returnType, logic, tail));
    }

    private static String parseArguments() throws IOException {
        TextReader.nextTokenWithTest(FormatsConsts.OPEN_PAREN);
        List<List<String>> arguments = new ArrayList<>();
        String preToken;
        do {
            List<String> argument = new ArrayList<>();
            do {
                StringBuilder arg = new StringBuilder();
                while (true) {
                    preToken = TextReader.nextToken();
                    if (Objects.equals(preToken, FormatsConsts.COMMA)
                        || Objects.equals(preToken, FormatsConsts.SEPARATOR)
                        || Objects.equals(preToken, FormatsConsts.CLOSE_PAREN)) {
                        break;
                    } else {
                        arg.append(preToken);
                    }
                }
                argument.add(arg.toString());
            } while (!Objects.equals(preToken, FormatsConsts.SEPARATOR)
                && !Objects.equals(preToken, FormatsConsts.CLOSE_PAREN));
            arguments.add(argument);
        } while (!Objects.equals(preToken, FormatsConsts.CLOSE_PAREN));
        List<String> temp = arguments.stream()
            .map(l -> String.format("[\"%s\"]", String.join("\", \"", l)))
            .collect(Collectors.toList());
        return "[" + String.join(", ", temp) + "]";
    }

    private static String parseReturn() throws IOException {
        TextReader.nextTokenWithTest(FormatsConsts.ARROW);
        return TextReader.nextToken();
    }

    private static String parseLogic() throws IOException {
        TextReader.nextTokenWithTest(FormatsConsts.OPEN_BRACE);
        String result = parseTerm();
        TextReader.nextTokenWithTest(FormatsConsts.CLOSE_BRACE);
        return result;
    }

    private static String parseTerm() throws IOException {
        String nextToken = TextReader.nextToken();
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
    private static String parseUnitaryBinary() throws IOException {
        String firstToken = TextReader.nextToken();
        if (FormatsConsts.UNARY_OPERATORS.contains(firstToken)) {
            String object = parseTerm();
            return String.format("{\"%s\": [%s]}", firstToken, object);
        } else {
            TextReader.rollBack(firstToken);
            String object1 = parseTerm();
            String operator = TextReader.nextToken();
            if (! FormatsConsts.BINARY_OPERATORS.contains(operator)) {
                throw new TokenInvalidException(String.format("Binary operator \"%s\" is not defined.", operator));
            }
            String object2 = parseTerm();
            return String.format("{\"%s\": [%s, %s]}", operator, object1, object2);
        }
    }

    /**
     * Parse multiple arguments operators.
     * @return json format logic
     */
    private static String parseMultipleArg(String operator) throws IOException {
        TextReader.nextTokenWithTest(FormatsConsts.OPEN_PAREN);
        List<String> args = new ArrayList<>();
        while (true) {
            args.add(parseTerm());
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
    private static String parseLoop() throws IOException {
        String quantifier = TextReader.nextToken();
        if (! Objects.equals(quantifier, FormatsConsts.ALL_SATISFY) && ! Objects.equals(quantifier, FormatsConsts.ANY_SATISFY)) {
            throw new TokenInvalidException(quantifier, List.of(FormatsConsts.ALL_SATISFY, FormatsConsts.ANY_SATISFY));
        }
        String loopVariable = parseElement(NONE_PRE_TOKEN);
        String nextToken = TextReader.nextToken();
        if (Objects.equals(nextToken, FormatsConsts.RANGE_SIGNAL)) {
            String range = parseTerm();
            TextReader.nextTokenWithTest(FormatsConsts.OPEN_PAREN);
            String logicalBody = parseTerm();
            TextReader.nextTokenWithTest(FormatsConsts.CLOSE_PAREN);
            return String.format("{\"%s\": [%s, %s, %s]}", quantifier, loopVariable, range, logicalBody);
        } else if (Objects.equals(nextToken, FormatsConsts.RANGE_BEGIN_SIGNAL)) {
            String rangeBegin = parseTerm();
            TextReader.nextTokenWithTest(FormatsConsts.RANGE_END_SIGNAL);
            String rangeEnd = parseTerm();
            TextReader.nextTokenWithTest(FormatsConsts.OPEN_PAREN);
            String logicalBody = parseTerm();
            TextReader.nextTokenWithTest(FormatsConsts.CLOSE_PAREN);
            return String.format("{\"%s\": [%s, %s, %s, %s]}",
                quantifier, loopVariable, rangeBegin, rangeEnd, logicalBody);
        } else {
            throw new TokenInvalidException(nextToken, List.of(FormatsConsts.RANGE_SIGNAL, FormatsConsts.RANGE_BEGIN_SIGNAL));
        }
    }

    /**
     * Parse single element, including: basic elements and statement '[' statement ']'
     * @param preReadToken if there is a token pre-read, {@value NONE_PRE_TOKEN} if not
     * @return json format element
     */
    private static String parseElement(String preReadToken) throws IOException {
        String element;
        if (! Objects.equals(preReadToken, NONE_PRE_TOKEN)) {
            element = preReadToken;
        } else {
            element = TextReader.nextToken();
        }
        String preToken = TextReader.nextToken();
        if (Objects.equals(preToken, FormatsConsts.OPEN_BRACKET)) {
            String collection = String.format("\"%s\"", element);
            String index = parseTerm();
            TextReader.nextTokenWithTest(FormatsConsts.CLOSE_BRACKET);
            return String.format("{\"%s\": [%s, %s]}", FormatsConsts.COLLECTION_GET, collection, index);
        } else {
            TextReader.rollBack(preToken);
        }
        if (isNumeric(element) || isBoolean(element)) {
            return element;
        }
        return String.format("\"%s\"", element);
    }

}
