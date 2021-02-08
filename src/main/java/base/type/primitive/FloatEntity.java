package base.type.primitive;

import base.type.BaseEntity;

/**
 * @author Guo Weize
 */
public final class FloatEntity extends PrimitiveEntity {
    private final double value;

    public FloatEntity(double value) {
        this.value = value;
    }

    public FloatEntity(float value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String getType() {
        return "float";
    }

    @Override
    public BoolEntity equal(BaseEntity entity) {
        if (! getType().equals(entity.getType())) {
            return new BoolEntity(false);
        }
        return getValue() == ((FloatEntity) entity).getValue() ?
                new BoolEntity(true):
                new BoolEntity(false);
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
