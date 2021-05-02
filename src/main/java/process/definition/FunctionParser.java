package process.definition;

import com.fasterxml.jackson.databind.JsonNode;
import util.FormatsConsts;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Parse recognition functions from file.
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
public final class FunctionParser {

    private static final String AUTO_CREATED_PREFIX = "_";
    private static final String PARAMETER_DELIMITER = FormatsConsts.PARAMETER_DELIMITER;
    private static final String ARGS_DELIMITER = ", ";

    private static final Map<String, Function<List<String>, String>> WORDS = Map.ofEntries(
        Map.entry("and", FunctionParser::and),
        Map.entry("or", FunctionParser::or),
        Map.entry("!", FunctionParser::not),
        Map.entry("==", FunctionParser::equal),
        Map.entry("!=", FunctionParser::notEqual),
        Map.entry(">", FunctionParser::greater),
        Map.entry("<", FunctionParser::less),
        Map.entry(">=", FunctionParser::notLess),
        Map.entry("<=", FunctionParser::notGreater),
        Map.entry("include", FunctionParser::include),
        Map.entry("size_of", FunctionParser::size),
        Map.entry("all", FunctionParser::all),
        Map.entry("any", FunctionParser::any),
        Map.entry("+", FunctionParser::add),
        Map.entry("-", FunctionParser::sub),
        Map.entry("*", FunctionParser::multiple),
        Map.entry("/", FunctionParser::divide),
        Map.entry("[]", FunctionParser::get)
    );

    public static String parse(JsonNode node) {
        if (node.isTextual()) {
            return argumentTransform(node.asText());
        }
        List<String> list = new ArrayList<>();
        node.fieldNames().forEachRemaining(list::add);
        if (list.size() != 1) {
            throw new IllegalArgumentException();
        }
        String operation = list.get(0);
        JsonNode subNodes = node.get(operation);
        List<String> arguments = new ArrayList<>();
        for (JsonNode n: subNodes) {
            arguments.add(parse(n));
        }
        return WORDS.containsKey(operation) ?
            WORDS.get(operation).apply(arguments) : customizedFunction(operation, arguments);
    }

    private static String argumentTransform(String itemString) {
        int left = itemString.indexOf(PARAMETER_DELIMITER);
        if (left >= 0) {
            int right = itemString.indexOf(PARAMETER_DELIMITER, left+1);
            if (right + 1 == itemString.length()) {
                return AUTO_CREATED_PREFIX + itemString.substring(left+1, right);
            }
            return AUTO_CREATED_PREFIX + itemString.substring(left+1, right) + itemString.substring(right+1);
        }
        return itemString;
    }

    private static void checkArgsNumberAmount(List<String> arguments, Predicate<Integer> range) {
        if (! range.test(arguments.size())) {
            throw new IllegalArgumentException();
        }
    }

    private static String customizedFunction(String name, List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i > 0);
        return String.format("%s(%s)", name, String.join(ARGS_DELIMITER, arguments));
    }

    private static String and(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i > 1);
        return String.format("BoolEntity.and(%s)", String.join(ARGS_DELIMITER, arguments));
    }

    private static String or(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i > 1);
        return String.format("BoolEntity.or(%s)", String.join(ARGS_DELIMITER, arguments));
    }

    private static String equal(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 2);
        return String.format("%s.equal(%s)", arguments.get(0), arguments.get(1));
    }

    private static String notEqual(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 2);
        return String.format("(! %s.equal(%s))", arguments.get(0), arguments.get(1));
    }

    private static String include(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 2);
        return String.format("%s.include(%s)", arguments.get(0), arguments.get(1));
    }

    private static String size(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 1);
        return String.format("%s.size()", arguments.get(0));
    }

    private static String all(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 3 || i == 4);
        if (arguments.size() == 3) {
            return String.format("%s.allMatch(%s -> %s)", arguments.get(1), arguments.get(0), arguments.get(2));
        }
        return String.format("IntStream.range(%s, %s).allMatch(%s -> %s)",
            arguments.get(1), arguments.get(2), arguments.get(0), arguments.get(3));
    }

    private static String any(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 3 || i == 4);
        if (arguments.size() == 3) {
            return String.format("%s.anyMatch(%s -> %s)", arguments.get(1), arguments.get(0), arguments.get(2));
        }
        return String.format("IntStream.range(%s, %s).anyMatch(%s -> %s)",
            arguments.get(1), arguments.get(2), arguments.get(0), arguments.get(3));
    }

    private static String add(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i > 1);
        if (arguments.size() == 2) {
            return String.format("BasePrimitiveEntity.calculate(%s, %s, \"+\")", arguments.get(0), arguments.get(1));
        }
        return String.format("BasePrimitiveEntity.summation(%s)", String.join(ARGS_DELIMITER, arguments));
    }

    private static String sub(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 1 || i == 2);
        if (arguments.size() == 1) {
            return String.format("BasePrimitiveEntity.calculate(0, %s, \"-\")", arguments.get(0));
        }
        return String.format("BasePrimitiveEntity.calculate(%s, %s, \"-\")", arguments.get(0), arguments.get(1));
    }

    private static String multiple(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i > 1);
        if (arguments.size() == 2) {
            return String.format("BasePrimitiveEntity.calculate(%s, %s, \"*\")", arguments.get(0), arguments.get(1));
        }
        return String.format("BasePrimitiveEntity.multiplication(%s)", String.join(ARGS_DELIMITER, arguments));
    }

    private static String divide(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 2);
        return String.format("BasePrimitiveEntity.calculate(%s, %s, \"/\")", arguments.get(0), arguments.get(1));
    }

    private static String greater(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 2);
        return String.format("BasePrimitiveEntity.compare(%s, %s, \">\")", arguments.get(0), arguments.get(1));
    }

    private static String less(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 2);
        return String.format("BasePrimitiveEntity.compare(%s, %s, \"<\")", arguments.get(0), arguments.get(1));
    }

    private static String notLess(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 2);
        return String.format("BasePrimitiveEntity.compare(%s, %s, \">=\")", arguments.get(0), arguments.get(1));
    }

    private static String notGreater(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 2);
        return String.format("BasePrimitiveEntity.compare(%s, %s, \"<=\")", arguments.get(0), arguments.get(1));
    }

    private static String not(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 1);
        return String.format("BoolEntity.not(%s)", arguments.get(0));
    }

    private static String get(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 2);
        return String.format("%s.get(%s)", arguments.get(0), arguments.get(1));
    }

}
