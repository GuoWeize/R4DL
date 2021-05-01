package process.judge;

import base.type.BaseEntity;
import base.type.primitive.BoolEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Recognize relationships between requirements.
 *
 * @author Guo Weize
 * @date 2021/2/19
 */
public final class Processor {
    private static final Map<Method, List<String>> METHOD_2_ARGUMENTS = new HashMap<>();

    public static void initialization() {
        METHOD_2_ARGUMENTS.clear();
    }

    public static void addRule(Method rule, List<String> arguments) {
        METHOD_2_ARGUMENTS.put(rule, arguments);
    }

    public static String recognition(Map<String, List<BaseEntity>> requirements) {
        String result = "";
        for (Map.Entry<Method, List<String>> entry: METHOD_2_ARGUMENTS.entrySet()) {
            Method rule = entry.getKey();
            List<String> types = entry.getValue();
            List<List<BaseEntity>> requirementsByTypes = types.stream().map(requirements::get).collect(Collectors.toList());


        }
        return result;
    }

    private static String process(Method rule, Object... arguments) {
        try {
            Object result = rule.invoke(null, arguments);
            if (! (result instanceof BoolEntity)) {
                throw new IllegalArgumentException();
            }
            if (((BoolEntity) result).getValue()) {
                return "Relationship " + rule.getName() + " between ";
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return "";
    }
}