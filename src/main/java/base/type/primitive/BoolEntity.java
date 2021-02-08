package base.type.primitive;

import base.type.BaseEntity;

/**
 * @author Guo Weize
 */
public final class BoolEntity extends PrimitiveEntity {
    private final boolean value;

    public BoolEntity(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String getType() {
        return "boolean";
    }

    @Override
    public BoolEntity equal(BaseEntity entity) {
        if (! getType().equals(entity.getType())) {
            return new BoolEntity(false);
        }
        return getValue() == ((BoolEntity) entity).getValue() ?
                new BoolEntity(true):
                new BoolEntity(false);
    }

    @Override
    public String toString() {
        return value ? "true": "false";
    }
}
