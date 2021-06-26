package basicTypes.primitive;

import basicTypes.BaseEntity;

import java.util.Objects;

/**
 * Class that store float data.
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
public final class FloatEntity extends BasePrimitiveEntity {

    private final double value;

    private FloatEntity(double value) {
        this.value = value;
    }

    public static FloatEntity valueOf(double value) {
        return new FloatEntity(value);
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
        if (! Objects.equals(getType(), entity.getType())) {
            return BoolEntity.FALSE;
        }
        return BoolEntity.valueOf(
            getValue() == ((FloatEntity) entity).getValue()
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
