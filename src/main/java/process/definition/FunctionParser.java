package process.definition;

import base.dynamics.TypeManager;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.List;
import java.util.Map;

/**
 * Parse recognition functions from file.
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
public final class FunctionParser {

    private static final String AUTO_CREATED_PREFIX = "_";
    private static final String PARAMETER_DELIMITER = "$";
    private static final String FOR_DELIMITER = "@";
    private static final String ARGS_DELIMITER = ", ";

    private static final int SINGLE_ARGUMENT = 1;
    private static final int TWO_ARGUMENT = 2;
    private static final int THREE_ARGUMENT = 3;
    private static final int FOUR_ARGUMENT = 4;

    private static final Map<String, Function<List<String>, String>> WORDS = Map.ofEntries(
        Map.entry("and", FunctionParser::and),
        Map.entry("or", FunctionParser::or),
        Map.entry("equal", FunctionParser::equal),
        Map.entry("not equal", FunctionParser::notEqual),
        Map.entry("include", FunctionParser::include),
        Map.entry("size", FunctionParser::size),
        Map.entry("all", FunctionParser::all),
        Map.entry("any", FunctionParser::any),
        Map.entry("all argument", FunctionParser::allArgument)
    );

    public static String parse(JsonNode node) {
        if (node.isTextual()) {
            return argumentTransform(node.asText());
        }
        List<String> list = new ArrayList<>();
        node.fieldNames().forEachRemaining(list::add);
        if (list.size() != SINGLE_ARGUMENT) {
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

    private static void checkArgumentsNumber(List<String> arguments, int size) {
        if (arguments.size() != size) {
            throw new IllegalArgumentException();
        }
    }

    private static String customizedFunction(String name, List<String> arguments) {
        return String.format("%s(%s)", name, String.join(ARGS_DELIMITER, arguments));
    }

    private static String and(List<String> arguments) {
        return String.format("BoolEntity.and(%s)", String.join(ARGS_DELIMITER, arguments));
    }

    private static String or(List<String> arguments) {
        return String.format("BoolEntity.or(%s)", String.join(ARGS_DELIMITER, arguments));
    }

    private static String equal(List<String> arguments) {
        checkArgumentsNumber(arguments, TWO_ARGUMENT);
        return String.format("%s.equal(%s)", arguments.get(0), arguments.get(1));
    }

    private static String notEqual(List<String> arguments) {
        checkArgumentsNumber(arguments, TWO_ARGUMENT);
        return String.format("(! %s.equal(%s))", arguments.get(0), arguments.get(1));
    }

    private static String include(List<String> arguments) {
        checkArgumentsNumber(arguments, TWO_ARGUMENT);
        return String.format("%s.include(%s)", arguments.get(0), arguments.get(1));
    }

    private static String size(List<String> arguments) {
        checkArgumentsNumber(arguments, SINGLE_ARGUMENT);
        return String.format("%s.size()", arguments.get(0));
    }

    private static String all(List<String> arguments) {
        checkArgumentsNumber(arguments, THREE_ARGUMENT);
        return String.format("%s.allMatch(%s -> %s)", arguments.get(1), arguments.get(0), arguments.get(2));
    }

    private static String any(List<String> arguments) {
        checkArgumentsNumber(arguments, THREE_ARGUMENT);
        return String.format("%s.anyMatch(%s -> %s)", arguments.get(1), arguments.get(0), arguments.get(2));
    }

    private static String allArgument(List<String> arguments) {
        checkArgumentsNumber(arguments, FOUR_ARGUMENT);
        String type = "Functional";
        String list = String.format("ListEntity<%s> _list = new ListEntity<>(\"%s\", Arrays.asList(%s));",
            TypeManager.type2class(type), type, arguments.get(1));
        String bound = arguments.get(2);
        String logic = forArgumentTransform(arguments.get(3));
        return String.format(
            "BoolEntity.valueOf(IntStream.range(0, Arrays.asList(%s).size()%s).allMatch(%s -> {%s return (%s).getValue();}))",
            arguments.get(1), bound, arguments.get(0), list, logic
        );
    }

    private static String forArgumentTransform(String logic) {
        int left = logic.indexOf(FOR_DELIMITER);
        int right;
        while (left >= 0) {
            right = logic.indexOf(FOR_DELIMITER, left +1);
            String previous = logic.substring(left, right +1);
            String number = logic.substring(left+1, right);
            String replacement = String.format("_list.get(%s)", number);
            logic = logic.replace(previous, replacement);
            left = logic.indexOf(FOR_DELIMITER);
        }
        return logic;
    }

}