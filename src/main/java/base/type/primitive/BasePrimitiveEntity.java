package base.type.primitive;

import base.type.BaseEntity;
import exceptions.TypeInvalidException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Abstract base class for all collection types, including boolean, integer, float and string.
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
public abstract class BasePrimitiveEntity extends BaseEntity {

    protected static final String INTEGER = "integer";
    protected static final String FLOAT = "float";
    protected static final String STRING = "string";

    /**
     * Check whether the entity is an IntEntity.
     * @param entity to check
     * @return a "true" BoolEntity if the entity is an IntEntity, otherwise "false" BoolEntity.
     */
    protected static boolean isInteger(BaseEntity entity) {
        return Objects.equals(entity.getType(), INTEGER);
    }

    /**
     * Check whether the entity is a FloatEntity.
     * @param entity to check
     * @return a "true" BoolEntity if the entity is an FloatEntity, otherwise "false" BoolEntity.
     */
    protected static boolean isFloat(BaseEntity entity) {
        return Objects.equals(entity.getType(), FLOAT);
    }

    /**
     * Get the number type after calculation.
     * @param entities to get
     * @return if there is a FloatEntity, the result is FloatEntity, otherwise IntEntity.
     */
    private static String calculateType(BaseEntity... entities) {
        for (BaseEntity entity: entities) {
            if (isFloat(entity)) {
                return FLOAT;
            }
            if (! isInteger(entity)) {
                throw new TypeInvalidException(entity.getType(), List.of(INTEGER, FLOAT));
            }
        }
        return INTEGER;
    }

    /**
     * Get the value of given number.
     * @param num to get
     * @return the value in double.
     */
    private static double getNumValue(BaseEntity num) {
        return isFloat(num) ? ((FloatEntity)num).getValue() : ((IntEntity)num).getValue();
    }

    /**
     * Calculate the negative number of given number.
     * @param entity to calculate
     * @return the negative number.
     */
    public static BaseEntity negative(BaseEntity entity) {
        if (isInteger(entity)) {
            return IntEntity.valueOf(- ((IntEntity)entity).getValue());
        }
        if (isFloat(entity)) {
            return FloatEntity.valueOf(- ((IntEntity)entity).getValue());
        }
        throw new TypeInvalidException(entity.getType(), List.of(INTEGER, FLOAT));
    }


    private static BaseEntity stringProcess(BaseEntity entity1, BaseEntity entity2, String operator) {
        String string1 = ((StringEntity)entity1).getValue();
        if (Objects.equals(operator, "+")) {
            if (! Objects.equals(entity2.getType(), STRING)) {
                throw new IllegalArgumentException();
            }
            String string2 = ((StringEntity)entity2).getValue();
            return StringEntity.valueOf(string1 + string2);
        } else if (Objects.equals(operator, "*")) {
            if (! Objects.equals(entity2.getType(), INTEGER)) {
                throw new IllegalArgumentException();
            }
            int integer = ((IntEntity)entity2).getValue();
            return StringEntity.valueOf(String.join("", Collections.nCopies(integer, string1)));
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Calculate basic operations of two numbers, including addition, subtraction, multiplication, division.
     * @param entity1 an entity of IntEntity or FloatEntity
     * @param entity2 an entity of IntEntity or FloatEntity
     * @param operator to be executed: + - * /
     * @return the result.
     */
    public static BaseEntity calculate(BaseEntity entity1, BaseEntity entity2, String operator) {
        if (Objects.equals(entity1.getType(), STRING)) {
            return stringProcess(entity1, entity2, operator);
        }

        String type = calculateType(entity1, entity2);
        if (Objects.equals(type, INTEGER)) {
            int integer1 = ((IntEntity)entity1).getValue();
            int integer2 = ((IntEntity)entity2).getValue();
            switch (operator) {
                case "+":
                    return IntEntity.valueOf(integer1 + integer2);
                case "-":
                    return IntEntity.valueOf(integer1 - integer2);
                case "*":
                    return IntEntity.valueOf(integer1 * integer2);
                case "/":
                    return IntEntity.valueOf(integer1 / integer2);
                default:
                    throw new IllegalArgumentException();
            }
        } else {
            double double1 = getNumValue(entity1);
            double double2 = getNumValue(entity2);
            switch (operator) {
                case "+":
                    return FloatEntity.valueOf(double1 + double2);
                case "-":
                    return FloatEntity.valueOf(double1 - double2);
                case "*":
                    return FloatEntity.valueOf(double1 * double2);
                case "/":
                    return FloatEntity.valueOf(double1 / double2);
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    /**
     * Summation of a string of entities.
     * @param entities to be summed
     * @return the result.
     */
    public static BaseEntity summation(BaseEntity... entities) {
        String type = calculateType(entities);
        if (Objects.equals(type, INTEGER)) {
            int result = 0;
            for (BaseEntity entity: entities) {
                result += ((IntEntity)entity).getValue();
            }
            return IntEntity.valueOf(result);
        } else {
            double result = 0.0;
            for (BaseEntity entity: entities) {
                result += getNumValue(entity);
            }
            return FloatEntity.valueOf(result);
        }
    }

    /**
     * Product of a string of entities.
     * @param entities to be summed
     * @return the result.
     */
    public static BaseEntity product(BaseEntity... entities) {
        String type = calculateType(entities);
        if (Objects.equals(type, INTEGER)) {
            int result = 1;
            for (BaseEntity entity: entities) {
                result *= ((IntEntity)entity).getValue();
            }
            return IntEntity.valueOf(result);
        } else {
            double result = 1.0;
            for (BaseEntity entity: entities) {
                result *= getNumValue(entity);
            }
            return FloatEntity.valueOf(result);
        }
    }

    /**
     * Compare two numbers, including greater, less, not less, not greater.
     * @param entity1 an entity of IntEntity or FloatEntity
     * @param entity2 an entity of IntEntity or FloatEntity
     * @param operator to be executed: > < >= <=
     * @return the result.
     */
    public static BoolEntity compare(BaseEntity entity1, BaseEntity entity2, String operator) {
        calculateType(entity1, entity2);
        double number1 = getNumValue(entity1);
        double number2 = getNumValue(entity2);
        switch (operator) {
            case "<":
                return number1 < number2 ? BoolEntity.TRUE: BoolEntity.FALSE;
            case ">":
                return number1 > number2 ? BoolEntity.TRUE: BoolEntity.FALSE;
            case "<=":
                return number1 <= number2 ? BoolEntity.TRUE: BoolEntity.FALSE;
            case ">=":
                return number1 >= number2 ? BoolEntity.TRUE: BoolEntity.FALSE;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public final boolean isPrimitive() {
        return true;
    }

    @Override
    public final boolean isRequirement() {
        return false;
    }

}
