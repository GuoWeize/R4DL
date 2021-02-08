package base.dynamics;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gwz
 */
public final class TypeManager {

    private static final Map<String, String> TYPE_2_CLASS = new HashMap<>();
    static {
        TYPE_2_CLASS.put("list", "ListEntity");
        TYPE_2_CLASS.put("map", "MapEntity");
        TYPE_2_CLASS.put("set", "SetEntity");
        TYPE_2_CLASS.put("boolean", "BoolEntity");
        TYPE_2_CLASS.put("float", "FloatEntity");
        TYPE_2_CLASS.put("integer", "IntEntity");
        TYPE_2_CLASS.put("string", "StringEntity");
    }

    public static void addType(String name, Map<String, String> fields2type) {
        TYPE_2_CLASS.put(name, name);
    }

    public static void checkType(String type) {
        if (! TYPE_2_CLASS.containsKey(type)) {
            throw new IllegalArgumentException();
        }
    }

    public static String type2class(String type) {
        type = type.replace("[", "<").replace("]", ">");
        for (Map.Entry<String, String> entry: TYPE_2_CLASS.entrySet()) {
            type = type.replace(entry.getKey(), entry.getValue());
        }
        return type;
    }

}
