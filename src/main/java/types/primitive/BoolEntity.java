package types.primitive;

import types.BaseEntity;

import java.util.Arrays;
import java.util.Objects;

/**
 * Class that store boolean data.
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
public final class BoolEntity extends BasePrimitive {

    public final static BoolEntity TRUE = new BoolEntity(true);
    public final static BoolEntity FALSE = new BoolEntity(false);

    private final boolean value;
    private static final String TYPE_NAME = "boolean";

    private BoolEntity(boolean value) {
        this.value = value;
    }

    public static BoolEntity valueOf(boolean value) {
        return value ? TRUE: FALSE;
    }

    public BoolEntity not() {
        return this.value ? FALSE: TRUE;
    }

    public BaseEntity select(BaseEntity True, BaseEntity False) {
        return this.value ? True: False;
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
        return TYPE_NAME;
    }

    @Override
    public BoolEntity equal(BaseEntity entity) {
        if (! Objects.equals(getType(), entity.getType())) {
            return FALSE;
        }
        return value == ((BoolEntity) entity).value ? TRUE: FALSE;
    }

    public static BoolEntity newInstance() {
        return FALSE;
    }

    @Override
    public String toString() {
        return value ? "true": "false";
    }
}
