package base.dynamics;

import base.type.BaseEntity;
import exceptions.TypeInvalidException;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Manager of all entity types.<p>
 * 2 useful functions: {@code checkType}, {@code type2class}.
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
public final class TypeManager {

    /** Entity type name -> its class name */
    private static final Map<String, String> TYPE2CLASS = Stream.of(new String[][] {
        {"list", "ListEntity"},
        {"map", "MapEntity"},
        {"set", "SetEntity"},
        {"boolean", "BoolEntity"},
        {"float", "FloatEntity"},
        {"integer", "IntEntity"},
        {"string", "StringEntity"}
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

    private static final String LIST = "list";
    private static final String SET = "set";
    private static final String MAP = "map";
    private static final int MAP_PARA_NUM = 2;

    /**
     * Initialization with all customized entity types.
     * @param typeNames All names of types that parse from file.
     */
    public static void initialization(Set<String> typeNames) {
        typeNames.forEach(name -> TYPE2CLASS.put(name, name));
    }

    /**
     * Check whether type is valid, if not, throw TypeInvalidException.
     * @param type to be checked.
     * @throws TypeInvalidException if type is not valid.
     */
    public static void checkType(String type) {
        if (! TYPE2CLASS.containsKey(type)) {
            if (type.startsWith(LIST)) {
                checkType(type.substring(5, type.length() - 1));
            } else if (type.startsWith(SET)) {
                checkType(type.substring(4, type.length() - 1));
            } else if (type.startsWith(MAP)) {
                String[] temp = type.split(", ");
                if (temp.length != MAP_PARA_NUM) {
                    throw new TypeInvalidException(
                        String.format("Map type requires 2 types in template, but got %s.", temp.length));
                }
                checkType(temp[0].substring(4));
                checkType(temp[1].substring(0, temp[1].length() - 1));
            } else {
                throw new TypeInvalidException(String.format("Type \"%s\" is not defined.", type));
            }
        }
    }

    /**
     * Transform the name of type to the name of class.
     * @param type to be transformed.
     * @return name of class that extends {@link BaseEntity}.
     */
    public static String type2class(String type) {
        for (Map.Entry<String, String> entry: TYPE2CLASS.entrySet()) {
            type = type.replace(entry.getKey(), entry.getValue());
        }
        return type;
    }

}
