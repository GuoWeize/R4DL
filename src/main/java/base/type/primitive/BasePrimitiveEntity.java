package base.type.primitive;

import base.type.BaseEntity;

/**
 * Abstract base class for all collection types, including boolean, integer, float and string.<p>
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
public abstract class BasePrimitiveEntity extends BaseEntity {

    protected static final String INTEGER = "integer";
    protected static final String FLOAT = "float";
    private static final String ERROR = "error";

    protected static boolean isInteger(BaseEntity entity) {
        return INTEGER.equals(entity.getType());
    }
    protected static boolean isFloat(BaseEntity entity) {
        return FLOAT.equals(entity.getType());
    }

    private static String calculateType(BaseEntity... entities) {
        for (BaseEntity entity: entities) {
            if (isFloat(entity)) {
                return FLOAT;
            }
            else if (! isInteger(entity)) {
                return ERROR;
            }
        }
        return INTEGER;
    }

    public static BaseEntity calculate(BaseEntity entity1, BaseEntity entity2, String operator) {
        String type = calculateType(entity1, entity2);
        if (ERROR.equals(type)) {
            throw new IllegalArgumentException();
        }
        else if (INTEGER.equals(type)) {
            int integer1 = ((IntEntity) entity1).getValue();
            int integer2 = ((IntEntity) entity2).getValue();
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
        }
        else {
            double double1 = isFloat(entity1) ?
                    ((FloatEntity) entity1).getValue() : ((IntEntity) entity1).getValue();
            double double2 = isFloat(entity2) ?
                    ((FloatEntity) entity2).getValue() : ((IntEntity) entity2).getValue();
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

    public static BaseEntity sum(BaseEntity... entities) {
        String type = calculateType(entities);
        if (ERROR.equals(type)) {
            throw new IllegalArgumentException();
        }
        else if (INTEGER.equals(type)) {
            int result = 0;
            for (BaseEntity entity: entities) {
                result += ((IntEntity) entity).getValue();
            }
            return IntEntity.valueOf(result);
        }
        else {
            double result = 0.0;
            for (BaseEntity entity: entities) {
                result += isFloat(entity) ?
                        ((FloatEntity) entity).getValue() : ((IntEntity) entity).getValue();
            }
            return FloatEntity.valueOf(result);
        }
    }

    public static BoolEntity compare(BaseEntity entity1, BaseEntity entity2, String operator) {
        double number1, number2;
        if (isInteger(entity1)) {
            number1 = ((IntEntity) entity1).getValue();
        }
        else if (isFloat(entity1)) {
            number1 = ((FloatEntity) entity1).getValue();
        }
        else {
            throw new IllegalArgumentException();
        }
        if (isInteger(entity2)) {
            number2 = ((IntEntity) entity2).getValue();
        }
        else if (isFloat(entity2)) {
            number2 = ((FloatEntity) entity2).getValue();
        }
        else {
            throw new IllegalArgumentException();
        }
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
