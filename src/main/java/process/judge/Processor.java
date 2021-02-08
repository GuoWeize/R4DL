package process.judge;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Guo Weize
 */
public final class Processor {
    public static final Map<String, Method> NAME_2_METHOD = new HashMap<>();

    public static void initialization(Class<?> rule) {
        NAME_2_METHOD.clear();
        for (Method r: rule.getMethods()) {
            NAME_2_METHOD.put(r.getName(), r);
        }
    }

    public static Object process(String ruleName, Object... arguments) {
        Method rule = NAME_2_METHOD.get(ruleName);
        try {
            return rule.invoke(null, arguments);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}