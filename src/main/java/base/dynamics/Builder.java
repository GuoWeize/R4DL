package base.dynamics;

import base.type.BaseEntity;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Builder of entity.<p>
 * Using reflect to construct any entity, set/get field value.
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
@Slf4j
public final class Builder {
    private static final Map<String, Constructor<?>> NAME_2_CONSTRUCTOR = new HashMap<>();

    /**
     * initialization with all classes.
     */
    public static void initialization() {
        NAME_2_CONSTRUCTOR.clear();
        try {
            for (Map.Entry<String, Class<?>> entry: Compiler.ENTITY_CLASSES.entrySet()) {
                String className = entry.getKey();
                Class<?> clazz = entry.getValue();
                NAME_2_CONSTRUCTOR.put(className, clazz.getDeclaredConstructor());
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        log.info("Initialization finished.");
        log.debug("All customized types are: " + NAME_2_CONSTRUCTOR.keySet());
    }

    /**
     * Set specific field of specific entity with specific value.
     * @param fieldName Name of entity's field.
     * @param entity to be set.
     * @param value to be set to specific field.
     */
    public static void setField(BaseEntity entity, String fieldName, Object value) {
        try {
            entity.getClass().getField(fieldName).set(entity, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get specific field value of specific entity.
     * @param entity Specific entity.
     * @param fieldName Name of entity's field.
     * @return value of the field of the entity.
     */
    public static BaseEntity getField(BaseEntity entity, String fieldName) {
        try {
            return (BaseEntity) entity.getClass().getField(fieldName).get(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Construct new entity instance of specific class.
     * @param className to construct.
     * @return entity of specific class.
     */
    public static BaseEntity newInstance(String className) {
        try {
            return (BaseEntity) NAME_2_CONSTRUCTOR.get(className).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}