package base.type.primitive;

import base.type.BaseEntity;

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
