package types.primitive;

import types.BaseEntity;

import java.util.Objects;

/**
 * Class that store float data.
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
public final class FloatEntity extends BaseNumber {

    private final double value;
    private static final String TYPE_NAME = "float";

    private FloatEntity(double value) {
        this.value = value;
    }

    public static FloatEntity valueOf(double value) {
        return new FloatEntity(value);
    }

    public double getValue() {
        return value;
    }

    public FloatEntity negative() {
        return new FloatEntity(- this.value);
    }

    @Override
    protected double getNum() {
        return value;
    }

    @Override
    public String getType() {
        return TYPE_NAME;
    }

    @Override
    public BoolEntity equal(BaseEntity entity) {
        if (! Objects.equals(getType(), entity.getType())) {
            return BoolEntity.FALSE;
        }
        return BoolEntity.valueOf(
            value == ((FloatEntity) entity).value
        );
    }

    public static FloatEntity newInstance() {
        return FloatEntity.valueOf(0.0);
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
