package base.type.primitive;

import base.type.BaseEntity;

/**
 * Class that store integer data.
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
public final class IntEntity extends BasePrimitiveEntity {

    private final int value;

    private IntEntity(int value) {
        this.value = value;
    }

    public static IntEntity valueOf(int value) {
        return new IntEntity(value);
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
