package dynamics;

import types.BaseEntity;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Builder of customized entity.<p>
 * Using reflect to construct customized entity and set/get field value.
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
@Slf4j
public final class Builder {

    /** Customized entity type name -> its constructor */
    private static final Map<String, Method> TYPE_NAME2CONSTRUCTOR = new HashMap<>();

    /**
     * Initialization with all customized classes according to {@link Compiler}.
     */
    public static void initialization() {
        TYPE_NAME2CONSTRUCTOR.clear();
        try {
            for (Map.Entry<String, Class<?>> entry: Compiler.getEntitiesClasses().entrySet()) {
                String className = entry.getKey();
                Class<?> clazz = entry.getValue();
                TYPE_NAME2CONSTRUCTOR.put(className, clazz.getMethod("newInstance", (Class<?>[]) null));
            }
        } catch (NoSuchMethodException e) {
            log.error("Can not find method.", e);
        }
        log.info("Initialization finished.");
        log.info("All customized types are: " + TYPE_NAME2CONSTRUCTOR.keySet());
    }

    /**
     * Set given field of given entity with given value.
     * @param fieldName name of entity's field.
     * @param entity to be set.
     * @param value to be set to given field.
     */
    public static void setField(BaseEntity entity, String fieldName, Object value) {
        try {
            entity.getClass().getField(fieldName).set(entity, value);
        } catch (Exception e) {
            log.error(String.format("Can not set field \"%s\" in entity \"%s\"", fieldName, entity), e);
        }
    }

    /**
     * Get the value of given field of given entity.
     * @param entity given entity.
     * @param fieldName name of entity's field.
     * @return value of the field of the entity.
     */
    public static BaseEntity getField(BaseEntity entity, String fieldName) {
        try {
            return (BaseEntity)entity.getClass().getField(fieldName).get(entity);
        } catch (Exception e) {
            log.error(String.format("Can not get field \"%s\" in entity \"%s\"", fieldName, entity), e);
            return null;
        }
    }

    /**
     * Construct new entity instance of given class.
     * @param className to construct.
     * @return entity of specific class.
     */
    public static BaseEntity newInstance(String className) {
        try {
            return (BaseEntity) TYPE_NAME2CONSTRUCTOR.get(className).invoke(null);
        } catch (Exception e) {
            log.error("can not new an instance of class \"" + className + "\".", e);
            return null;
        }
    }
}