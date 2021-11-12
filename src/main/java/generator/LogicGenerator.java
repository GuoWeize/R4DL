package generator;

import com.fasterxml.jackson.databind.JsonNode;

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
public final class LogicGenerator {

    private static final String ARGS_DELIMITER = ", ";

    private static final Predicate<Integer> UNARY = (i -> i == 1);
    private static final Predicate<Integer> BINARY = (i -> i == 2);
    private static final Predicate<Integer> MULTI_ARGS = (i -> i > 1);

    private static final Map<String, Function<List<String>, String>> OPERATORS = Map.ofEntries(
        // unary operation
        Map.entry("is_null", LogicGenerator::isNull),
        Map.entry("!", LogicGenerator::not),
        Map.entry("size_of", LogicGenerator::size),
        // binary operation
        Map.entry("==", LogicGenerator::equal),
        Map.entry("!=", LogicGenerator::notEqual),
        Map.entry(">", LogicGenerator::greater),
        Map.entry("<", LogicGenerator::less),
        Map.entry(">=", LogicGenerator::notLess),
        Map.entry("<=", LogicGenerator::notGreater),
        Map.entry("-", LogicGenerator::sub),
        Map.entry("/", LogicGenerator::divide),
        Map.entry("include", LogicGenerator::include),
        Map.entry("contains", LogicGenerator::contains),
        Map.entry("intersect", LogicGenerator::intersect),
        Map.entry("union", LogicGenerator::union),
        Map.entry("synonym", LogicGenerator::synonym),
        Map.entry("antonym", LogicGenerator::antonym),
        // multi-argument operation
        Map.entry("and", LogicGenerator::and),
        Map.entry("or", LogicGenerator::or),
        Map.entry("max", LogicGenerator::maximum),
        Map.entry("min", LogicGenerator::minimum),
        Map.entry("+", LogicGenerator::add),
        Map.entry("*", LogicGenerator::multiple),
        Map.entry("merge", LogicGenerator::merge),
        // others
        Map.entry("?", LogicGenerator::select),
        Map.entry("all", LogicGenerator::all),
        Map.entry("any", LogicGenerator::any),
        Map.entry("find", LogicGenerator::find),
        Map.entry("substring", LogicGenerator::substring)
    );

    public static String parse(JsonNode node) {
        if (node.isTextual()) {
            return String.format("StringEntity.valueOf(\"%s\")", node.asText());
        }
        if (node.isBoolean()) {
            return String.format("BoolEntity.valueOf(%b)", node.asBoolean());
        }
        if (node.isInt()) {
            return String.format("IntEntity.valueOf(%d)", node.asInt());
        }
        if (node.isFloat()) {
            return String.format("FloatEntity.valueOf(%f)", node.asDouble());
        }
        if (node.has("$")) {
            return String.format("$%s$", node.get("$").asInt());
        }
        if (node.has("%")) {
            return node.get("%").asText();
        }
        if (node.has(".")) {
            return parse(node.get("&")) + ".f_" + node.get(".").asText();
        }
        if (node.has("@")) {
            return parse(node.get("&")) + ".get(" + parse(node.get("@")) + ")";
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

    private static String isNull(List<String> arguments) {
        checkArgsNumberAmount(arguments, UNARY);
        return String.format("%s.isNull()", arguments.get(0));
    }

    private static String not(List<String> arguments) {
        checkArgsNumberAmount(arguments, UNARY);
        return String.format("%s.not()", arguments.get(0));
    }

    private static String size(List<String> arguments) {
        checkArgsNumberAmount(arguments, UNARY);
        return String.format("%s.size()", arguments.get(0));
    }

    private static String equal(List<String> arguments) {
        checkArgsNumberAmount(arguments, BINARY);
        return String.format("%s.equal(%s)", arguments.get(0), arguments.get(1));
    }

    private static String notEqual(List<String> arguments) {
        checkArgsNumberAmount(arguments, BINARY);
        return String.format("%s.equal(%s).not()", arguments.get(0), arguments.get(1));
    }

    private static String greater(List<String> arguments) {
        checkArgsNumberAmount(arguments, BINARY);
        return String.format("BaseNumber.compare(%s, %s, \">\")", arguments.get(0), arguments.get(1));
    }

    private static String less(List<String> arguments) {
        checkArgsNumberAmount(arguments, BINARY);
        return String.format("BaseNumber.compare(%s, %s, \"<\")", arguments.get(0), arguments.get(1));
    }

    private static String notLess(List<String> arguments) {
        checkArgsNumberAmount(arguments, BINARY);
        return String.format("BaseNumber.compare(%s, %s, \">=\")", arguments.get(0), arguments.get(1));
    }

    private static String notGreater(List<String> arguments) {
        checkArgsNumberAmount(arguments, BINARY);
        return String.format("BaseNumber.compare(%s, %s, \"<=\")", arguments.get(0), arguments.get(1));
    }

    private static String include(List<String> arguments) {
        checkArgsNumberAmount(arguments, BINARY);
        return String.format("%s.include(%s)", arguments.get(0), arguments.get(1));
    }

    private static String contains(List<String> arguments) {
        checkArgsNumberAmount(arguments, BINARY);
        return String.format("%s.contains(%s)", arguments.get(0), arguments.get(1));
    }

    private static String intersect(List<String> arguments) {
        checkArgsNumberAmount(arguments, BINARY);
        return String.format("%s.intersect(%s)", arguments.get(0), arguments.get(1));
    }

    private static String union(List<String> arguments) {
        checkArgsNumberAmount(arguments, BINARY);
        return String.format("%s.union(%s)", arguments.get(0), arguments.get(1));
    }

    private static String synonym(List<String> arguments) {
        checkArgsNumberAmount(arguments, BINARY);
        return String.format("StringEntity.synonym(%s, %s)", arguments.get(0), arguments.get(1));
    }

    private static String antonym(List<String> arguments) {
        checkArgsNumberAmount(arguments, BINARY);
        return String.format("StringEntity.antonym(%s, %s)", arguments.get(0), arguments.get(1));
    }

    private static String and(List<String> arguments) {
        checkArgsNumberAmount(arguments, MULTI_ARGS);
        List<String> temp = new ArrayList<>();
        for (String arg: arguments) {
            temp.add(arg + ".getValue()");
        }
        return String.format("BoolEntity.valueOf(%s)", String.join(" && ", temp));
    }

    private static String or(List<String> arguments) {
        checkArgsNumberAmount(arguments, MULTI_ARGS);
        List<String> temp = new ArrayList<>();
        for (String arg: arguments) {
            temp.add(arg + ".getValue()");
        }
        return String.format("BoolEntity.valueOf(%s)", String.join(" || ", temp));
    }

    private static String maximum(List<String> arguments) {
        checkArgsNumberAmount(arguments, MULTI_ARGS);
        return String.format("BaseNumber.max(%s)", String.join(ARGS_DELIMITER, arguments));
    }

    private static String minimum(List<String> arguments) {
        checkArgsNumberAmount(arguments, MULTI_ARGS);
        return String.format("BaseNumber.min(%s)", String.join(ARGS_DELIMITER, arguments));
    }

    private static String add(List<String> arguments) {
        checkArgsNumberAmount(arguments, MULTI_ARGS);
        if (arguments.size() == 2) {
            return String.format("BasePrimitive.calculate(%s, %s, \"+\")", arguments.get(0), arguments.get(1));
        }
        return String.format("BaseNumber.sum(%s)", String.join(ARGS_DELIMITER, arguments));
    }

    private static String sub(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 1 || i == 2);
        if (arguments.size() == 1) {
            return String.format("%s.negative()", arguments.get(0));
        }
        return String.format("BasePrimitive.calculate(%s, %s, \"-\")", arguments.get(0), arguments.get(1));
    }

    private static String multiple(List<String> arguments) {
        checkArgsNumberAmount(arguments, MULTI_ARGS);
        if (arguments.size() == 2) {
            return String.format("BasePrimitive.calculate(%s, %s, \"*\")", arguments.get(0), arguments.get(1));
        }
        return String.format("BaseNumber.product(%s)", String.join(ARGS_DELIMITER, arguments));
    }

    private static String divide(List<String> arguments) {
        checkArgsNumberAmount(arguments, BINARY);
        return String.format("BasePrimitive.calculate(%s, %s, \"/\")", arguments.get(0), arguments.get(1));
    }

    private static String merge(List<String> arguments) {
        checkArgsNumberAmount(arguments, MULTI_ARGS);
        return String.format("BaseCollection.merge(%s)", String.join(ARGS_DELIMITER, arguments));
    }

    private static String select(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 3);
        return String.format("%s.select(%s, %s)", arguments.get(0), arguments.get(1), arguments.get(2));
    }

    private static String find(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 3);
        return String.format("%s.find(%s, %s)", arguments.get(0), arguments.get(2), arguments.get(1));
    }

    private static String substring(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 3);
        return String.format("%s.substring(%s, %s)", arguments.get(0), arguments.get(1), arguments.get(2));
    }

    private static String all(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 3 || i == 4);
        if (arguments.size() == 3) {
            return String.format("%s.allMatch(%s -> %s)", arguments.get(1), arguments.get(0), arguments.get(2));
        }
        return String.format("BoolEntity.valueOf(IntEntity.range(%s, %s).allMatch(%s -> (%s).getValue()))",
            arguments.get(1), arguments.get(2), arguments.get(0), arguments.get(3));
    }

    private static String any(List<String> arguments) {
        checkArgsNumberAmount(arguments, i -> i == 3 || i == 4);
        if (arguments.size() == 3) {
            return String.format("%s.anyMatch(%s -> %s)", arguments.get(1), arguments.get(0), arguments.get(2));
        }
        return String.format("BoolEntity.valueOf(IntEntity.range(%s, %s).anyMatch(%s -> (%s).getValue()))",
            arguments.get(1), arguments.get(2), arguments.get(0), arguments.get(3));
    }

}
