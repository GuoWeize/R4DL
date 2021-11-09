package types.primitive;

import types.BaseEntity;

import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Class that store integer data.
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
public final class IntEntity extends BaseNumber {

    private final int value;
    private static final String TYPE_NAME = "integer";

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
    protected double getNum() {
        return value;
    }

    public IntEntity negative() {
        return new IntEntity(- this.value);
    }

    public static Stream<IntEntity> range(IntEntity startInclusive, IntEntity endExclusive) {
        return IntStream.range((startInclusive).value, (endExclusive).value).mapToObj(IntEntity::new);
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
            value == ((IntEntity) entity).value
        );
    }

    public static IntEntity newInstance() {
        return IntEntity.valueOf(0);
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

}
