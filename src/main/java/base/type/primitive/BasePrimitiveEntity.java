package base.type.primitive;

import base.type.BaseEntity;
import exceptions.TypeInvalidException;

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

    @Override
    public final boolean isPrimitive() {
        return true;
    }

    @Override
    public final boolean isRequirement() {
        return false;
    }

    protected static boolean isInteger(BaseEntity entity) {
        return Objects.equals(entity.getType(), INTEGER);
    }

    protected static boolean isFloat(BaseEntity entity) {
        return Objects.equals(entity.getType(), FLOAT);
    }

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

    private static double getNumValue(BaseEntity num) {
        return isFloat(num) ? ((FloatEntity)num).getValue() : ((IntEntity)num).getValue();
    }

    public static BaseEntity negative(BaseEntity entity) {
        if (isInteger(entity)) {
            return IntEntity.valueOf(- ((IntEntity)entity).getValue());
        }
        if (isFloat(entity)) {
            return FloatEntity.valueOf(- ((IntEntity)entity).getValue());
        }
        throw new TypeInvalidException(entity.getType(), List.of(INTEGER, FLOAT));
    }

    public static BaseEntity calculate(BaseEntity entity1, BaseEntity entity2, String operator) {
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

    public static BaseEntity multiplication(BaseEntity... entities) {
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
}
