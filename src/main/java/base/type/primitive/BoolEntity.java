package base.type.primitive;

import base.type.BaseEntity;

import java.util.Arrays;
import java.util.Objects;

/**
 * Class that store boolean data.
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
public final class BoolEntity extends BasePrimitiveEntity {
    public final static BoolEntity TRUE = new BoolEntity(true);
    public final static BoolEntity FALSE = new BoolEntity(false);

    private final boolean value;

    private BoolEntity(boolean value) {
        this.value = value;
    }

    public static BoolEntity valueOf(boolean value) {
        return value ? TRUE: FALSE;
    }

    public static BoolEntity and(BoolEntity... arguments) {
        return Arrays.stream(arguments).allMatch(BoolEntity::getValue) ? TRUE: FALSE;
    }

    public static BoolEntity or(BoolEntity... arguments) {
        return Arrays.stream(arguments).anyMatch(BoolEntity::getValue) ? TRUE: FALSE;
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
        if (! Objects.equals(getType(), entity.getType())) {
            return FALSE;
        }
        return getValue() == ((BoolEntity) entity).getValue() ? TRUE: FALSE;
    }

    @Override
    public String toString() {
        return value ? "true": "false";
    }
}
