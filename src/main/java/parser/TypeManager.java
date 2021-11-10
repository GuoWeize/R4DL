package parser;

import exceptions.TypeInvalidException;

import java.util.HashMap;
import java.util.Map;

/**
 * Manager of all entity types.<p>
 * 2 useful functions: {@code checkType}, {@code type2class}.
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
public final class TypeManager {

    private static final Map<String, Boolean> TYPES = new HashMap<>();
    private static final Map<String, Boolean> PRIMITIVE_TYPES = Map.of(
        "boolean", false,
        "integer", false,
        "float", false,
        "string", false,
        "list", false,
        "set", false,
        "map", false
    );

    public static void initialization() {
        TYPES.clear();
        TYPES.putAll(PRIMITIVE_TYPES);
    }

    public static void add(String name, boolean isRequirement) {
        TYPES.put(name, isRequirement);
    }

    public static boolean isRequirement(String type) {
        return TYPES.getOrDefault(type, false);
    }

    /**
     * Check whether type is valid, if not, throw TypeInvalidException.
     * @param type to be checked.
     * @throws TypeInvalidException if type is not valid.
     */
    public static void checkType(String type) {
        if (! TYPES.containsKey(type)) {
            throw new TypeInvalidException(type + "is invalid");
        }
    }

}
