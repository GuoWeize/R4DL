package base.type.primitive;

import base.type.BaseEntity;

import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

    public static Stream<IntEntity> range(BaseEntity startInclusive, BaseEntity endExclusive) {
        checkMatched(startInclusive.getType(), "integer");
        checkMatched(endExclusive.getType(), "integer");
        return IntStream.range(((IntEntity)startInclusive).value, ((IntEntity)endExclusive).value).mapToObj(IntEntity::new);
    }

    @Override
    public String getType() {
        return "integer";
    }

    @Override
    public BoolEntity equal(BaseEntity entity) {
        if (! Objects.equals(getType(), entity.getType())) {
            return BoolEntity.FALSE;
        }
        return BoolEntity.valueOf(
            getValue() == ((IntEntity) entity).getValue()
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
