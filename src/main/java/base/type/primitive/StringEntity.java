package base.type.primitive;

import base.type.BaseEntity;

/**
 * @author Guo Weize
 */
public final class StringEntity extends PrimitiveEntity {
    private final String value;

    public StringEntity(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String getType() {
        return "string";
    }

    @Override
    public BoolEntity equal(BaseEntity entity) {
        if (! getType().equals(entity.getType())) {
            return new BoolEntity(false);
        }
        return ((StringEntity) entity).getValue().equals(getValue()) ?
                new BoolEntity(true):
                new BoolEntity(false);
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }
}
