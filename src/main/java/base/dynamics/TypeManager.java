package base.dynamics;

import base.type.BaseEntity;

import java.util.*;

/**
 * Manager of all entity types.<p>
 * 2 useful functions: {@code checkType}, {@code type2class}.
 *
 * @author Guo Weize
 * @date 2021/2/1
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
    private static final String LIST = "list";
    private static final String SET = "set";
    private static final String MAP = "map";
    private static final int MAP_PARA_NUM = 2;

    /**
     * initialization with all types.
     * @param typeNames All names of types that parse from file.
     */
    public static void initialization(Set<String> typeNames) {
        typeNames.forEach(name -> TYPE_2_CLASS.put(name, name));
    }

    /**
     * check whether type is valid, if not, throw IllegalArgumentException.
     * @param type to be checked.
     * @throws IllegalArgumentException if type is not valid.
     */
    public static void checkType(String type) {
        if (! TYPE_2_CLASS.containsKey(type)) {
            if (type.startsWith(LIST)) {
                checkType(type.substring(5, type.length() -1));
            }
            else if (type.startsWith(SET)) {
                checkType(type.substring(4, type.length() -1));
            }
            else if (type.startsWith(MAP)) {
                List<String> temp = Arrays.asList(type.split(", "));
                if (temp.size() != MAP_PARA_NUM) {
                    throw new IllegalArgumentException();
                }
                checkType(temp.get(0).substring(4));
                checkType(temp.get(1).substring(0, temp.get(1).length() -1));
            }
            else {
                throw new IllegalArgumentException();
            }
        }
    }

    /**
     * Transform name of type to class signature.
     * @param type name to be transformed.
     * @return signature of class that extends {@link BaseEntity}.
     */
    public static String type2class(String type) {
        type = type.replace("[", "<").replace("]", ">");
        for (Map.Entry<String, String> entry: TYPE_2_CLASS.entrySet()) {
            type = type.replace(entry.getKey(), entry.getValue());
        }
        return type;
    }

}
