package base.type.primitive;

import base.type.BaseEntity;

import java.util.Objects;

/**
 * @author Guo Weize
 */
public final class IntEntity extends PrimitiveEntity {
    private final int value;

    public IntEntity(int value) {
        this.value = value;
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
            return new BoolEntity(false);
        }
        return getValue() == ((IntEntity) entity).getValue() ?
                new BoolEntity(true):
                new BoolEntity(false);
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
