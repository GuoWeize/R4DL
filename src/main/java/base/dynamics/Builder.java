package base.dynamics;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Guo Weize
 */
public final class Builder {
    private static final Map<String, Constructor<?>> NAME_2_CONSTRUCTOR = new HashMap<>();
    private static final Map<String, Map<String, Field>> NAME_2_FIELDS = new HashMap<>();

    public static void initialization(Map<String, Class<?>> allClasses) {
        NAME_2_CONSTRUCTOR.clear();
        NAME_2_FIELDS.clear();
        try {
            for (Map.Entry<String, Class<?>> entry: allClasses.entrySet()) {
                String className = entry.getKey();
                Class<?> clazz = entry.getValue();
                NAME_2_CONSTRUCTOR.put(className, clazz.getDeclaredConstructor());
                NAME_2_FIELDS.put(className,
                        Arrays.stream(clazz.getFields()).collect(Collectors.toMap(Field::getName, t->t)));
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static void setField(String className, String fieldName, Object entity, Object value) {
        try {
            NAME_2_FIELDS.get(className).get(fieldName).set(entity, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void setField(Object entity, String fieldName, Object value) {
        try {
            entity.getClass().getField(fieldName).set(entity, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getField(String className, String fieldName, Object entity) {
        try {
            return NAME_2_FIELDS.get(className).get(fieldName).get(entity);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object newInstance(String className) {
        try {
            return NAME_2_CONSTRUCTOR.get(className).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}