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
public final class LogicParser {

    private static final String ARGS_DELIMITER = ", ";

    private static final Map<String, Function<List<String>, String>> OPERATORS = Map.ofEntries(
        Map.entry(FormatsConsts.LOGICAL_NOT, LogicParser::not),
        Map.entry(FormatsConsts.LOGICAL_AND, LogicParser::and),
        Map.entry(FormatsConsts.LOGICAL_OR, LogicParser::or),
        Map.entry(FormatsConsts.COMPARE_EQUAL, LogicParser::equal),
        Map.entry(FormatsConsts.COMPARE_NOT_EQUAL, LogicParser::notEqual),
        Map.entry(FormatsConsts.COMPARE_GREATER, LogicParser::greater),
        Map.entry(FormatsConsts.COMPARE_LESS, LogicParser::less),
        Map.entry(FormatsConsts.COMPARE_NOT_LESS, LogicParser::notLess),
        Map.entry(FormatsConsts.COMPARE_NOT_GREATER, LogicParser::notGreater),
        Map.entry(FormatsConsts.CALCULATE_ADDITION, LogicParser::add),
        Map.entry(FormatsConsts.CALCULATE_SUBTRACTION, LogicParser::sub),
        Map.entry(FormatsConsts.CALCULATE_MULTIPLE, LogicParser::multiple),
        Map.entry(FormatsConsts.CALCULATE_DIVISION, LogicParser::divide),
        Map.entry(FormatsConsts.COLLECTION_SIZE_OF, LogicParser::size),
        Map.entry(FormatsConsts.COLLECTION_GET, LogicParser::get),
        Map.entry(FormatsConsts.COLLECTION_INCLUDE, LogicParser::include),
        Map.entry(FormatsConsts.ALL_SATISFY, LogicParser::all),
        Map.entry(FormatsConsts.ANY_SATISFY, LogicParser::any),
        Map.entry(FormatsConsts.STRING_FIND, LogicParser::find),
        Map.entry(FormatsConsts.STRING_SUBSTRING, LogicParser::substring)
    );

    public static String parse(JsonNode node) {
        if (node.isTextual()) {
            return node.asText();
        } else if (node.isBoolean()) {
            return String.format("BoolEntity.valueOf(%b)", node.asBoolean());
        } else if (node.isInt()) {
            return String.format("IntEntity.valueOf(%d)", node.asInt());
        } else if (node.isFloat()) {
            return String.format("FloatEntity.valueOf(%f)", node.asDouble());
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
        return OPERATORS.containsKey(operation) ?
            OPERATORS.get(operation).apply(arguments) : customizedFunction(operation, arguments);
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

    private static String not(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 1);
        return String.format("BoolEntity.not(%s)", arguments.get(0));
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
        return String.format("BaseEntity.equal(%s, %s)", arguments.get(0), arguments.get(1));
    }

    private static String notEqual(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 2);
        return String.format("BoolEntity.not(%s.equal(%s))", arguments.get(0), arguments.get(1));
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
            return String.format("BasePrimitiveEntity.negative(%s)", arguments.get(0));
        }
        return String.format("BasePrimitiveEntity.calculate(%s, %s, \"-\")", arguments.get(0), arguments.get(1));
    }

    private static String multiple(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i > 1);
        if (arguments.size() == 2) {
            return String.format("BasePrimitiveEntity.calculate(%s, %s, \"*\")", arguments.get(0), arguments.get(1));
        }
        return String.format("BasePrimitiveEntity.product(%s)", String.join(ARGS_DELIMITER, arguments));
    }

    private static String divide(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 2);
        return String.format("BasePrimitiveEntity.calculate(%s, %s, \"/\")", arguments.get(0), arguments.get(1));
    }

    private static String size(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 1);
        return String.format("%s.size()", arguments.get(0));
    }

    private static String get(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 2);
        return String.format("%s.get(%s)", arguments.get(0), arguments.get(1));
    }

    private static String include(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 2);
        return String.format("%s.include(%s)", arguments.get(0), arguments.get(1));
    }

    private static String all(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 3 || i == 4);
        if (arguments.size() == 3) {
            return String.format("%s.allMatch(%s -> %s)", arguments.get(1), arguments.get(0), arguments.get(2));
        }
        return String.format("BoolEntity.valueOf(IntEntity.range(%s, %s).allMatch(%s -> ((BoolEntity)%s).getValue()))",
            arguments.get(1), arguments.get(2), arguments.get(0), arguments.get(3));
    }

    private static String any(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 3 || i == 4);
        if (arguments.size() == 3) {
            return String.format("%s.anyMatch(%s -> %s)", arguments.get(1), arguments.get(0), arguments.get(2));
        }
        return String.format("BoolEntity.valueOf(IntEntity.range(%s, %s).anyMatch(%s -> ((BoolEntity)%s).getValue()))",
            arguments.get(1), arguments.get(2), arguments.get(0), arguments.get(3));
    }

    private static String find(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 3);
        return String.format("%s.find(%s, %s)", arguments.get(0), arguments.get(2), arguments.get(1));
    }

    private static String substring(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 3);
        return String.format("%s.substring(%s, %s)", arguments.get(0), arguments.get(1), arguments.get(2));
    }

}
