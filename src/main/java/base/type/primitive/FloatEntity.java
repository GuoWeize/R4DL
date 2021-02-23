package base.type.primitive;

import base.type.BaseEntity;

/**
 * Class that store float data.
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
public final class FloatEntity extends BasePrimitiveEntity {
    private static final String INTEGER = "integer";
    private static final String FLOAT = "float";

    private final double value;

    private FloatEntity(double value) {
        this.value = value;
    }

    public static FloatEntity valueOf(double value) {
        return new FloatEntity(value);
    }

    public static FloatEntity add(BaseEntity... arguments) {
        double result = 0.0;
        for (BaseEntity entity: arguments) {
            if (INTEGER.equals(entity.getType())) {
                result += ((IntEntity) entity).getValue();
            }
            else if (FLOAT.equals(entity.getType())) {
                result += ((FloatEntity) entity).getValue();
            }
            else {
                throw new IllegalArgumentException();
            }
        }
        return FloatEntity.valueOf(result);
    }

    public static FloatEntity sub(BaseEntity minuend, BaseEntity subtrahend) {
        double result;
        if (INTEGER.equals(minuend.getType())) {
            result = ((IntEntity) minuend).getValue();
        }
        else if (FLOAT.equals(minuend.getType())) {
            result = ((FloatEntity) minuend).getValue();
        }
        else {
            throw new IllegalArgumentException();
        }
        if (INTEGER.equals(subtrahend.getType())) {
            return FloatEntity.valueOf(result - ((IntEntity) subtrahend).getValue());
        }
        else if (FLOAT.equals(subtrahend.getType())) {
            return FloatEntity.valueOf(result - ((FloatEntity) subtrahend).getValue());
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    public static FloatEntity mult(BaseEntity... arguments) {
        double result = 1.0;
        for (BaseEntity entity: arguments) {
            if (INTEGER.equals(entity.getType())) {
                result *= ((IntEntity) entity).getValue();
            }
            else if (FLOAT.equals(entity.getType())) {
                result *= ((FloatEntity) entity).getValue();
            }
            else {
                throw new IllegalArgumentException();
            }
        }
        return FloatEntity.valueOf(result);
    }

    public static FloatEntity div(BaseEntity dividend, BaseEntity divisor) {
        double result;
        if (INTEGER.equals(dividend.getType())) {
            result = ((IntEntity) dividend).getValue();
        }
        else if (FLOAT.equals(dividend.getType())) {
            result = ((FloatEntity) dividend).getValue();
        }
        else {
            throw new IllegalArgumentException();
        }
        if (INTEGER.equals(divisor.getType())) {
            return FloatEntity.valueOf(result / ((IntEntity) divisor).getValue());
        }
        else if (FLOAT.equals(divisor.getType())) {
            return FloatEntity.valueOf(result / ((FloatEntity) divisor).getValue());
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    public double getValue() {
        return value;
    }

    @Override
    public String getType() {
        return FLOAT;
    }

    @Override
    public BoolEntity equal(BaseEntity entity) {
        if (! getType().equals(entity.getType())) {
            return BoolEntity.FALSE;
        }
        return BoolEntity.valueOf(
                getValue() == ((FloatEntity) entity).getValue()
        );
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
