package base.type.primitive;

import base.type.BaseEntity;

/**
 * Class that store integer data.
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
public final class IntEntity extends BasePrimitiveEntity {
    private static final String INTEGER = "integer";

    private final int value;

    private IntEntity(int value) {
        this.value = value;
    }

    public static IntEntity valueOf(int value) {
        return new IntEntity(value);
    }

    public static IntEntity add(BaseEntity... arguments) {
        int result = 0;
        for (BaseEntity entity: arguments) {
            if (INTEGER.equals(entity.getType())) {
                result += ((IntEntity) entity).getValue();
            }
            else {
                throw new IllegalArgumentException();
            }
        }
        return IntEntity.valueOf(result);
    }

    public static IntEntity sub(BaseEntity minuend, BaseEntity subtrahend) {
        if (INTEGER.equals(minuend.getType()) && INTEGER.equals(subtrahend.getType())) {
            return IntEntity.valueOf(((IntEntity) minuend).getValue() - ((IntEntity) subtrahend).getValue());
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    public static IntEntity mult(BaseEntity... arguments) {
        int result = 1;
        for (BaseEntity entity: arguments) {
            if (INTEGER.equals(entity.getType())) {
                result *= ((IntEntity) entity).getValue();
            }
            else {
                throw new IllegalArgumentException();
            }
        }
        return IntEntity.valueOf(result);
    }

    public static IntEntity div(BaseEntity dividend, BaseEntity divisor) {
        if (INTEGER.equals(dividend.getType()) && INTEGER.equals(divisor.getType())) {
            return IntEntity.valueOf(((IntEntity) dividend).getValue() / ((IntEntity) divisor).getValue());
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    public int getValue() {
        return value;
    }

    @Override
    public String getType() {
        return "integer";
    }

    @Override
    public BoolEntity equal(BaseEntity entity) {
        if (! getType().equals(entity.getType())) {
            return BoolEntity.FALSE;
        }
        return BoolEntity.valueOf(
                getValue() == ((IntEntity) entity).getValue()
        );
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
