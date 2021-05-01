package base.type.primitive;

import base.type.BaseEntity;

import java.util.Objects;

/**
 * Class that store string data.
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
public final class StringEntity extends BasePrimitiveEntity {
    private final String value;

    private StringEntity(String value) {
        this.value = value;
    }

    public static StringEntity valueOf(String value) {
        return new StringEntity(value);
    }

    public static StringEntity append(StringEntity... arguments) {
        StringBuilder sb = new StringBuilder();
        for (StringEntity substring: arguments) {
            sb.append(substring.value);
        }
        return StringEntity.valueOf(sb.toString());
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
        if (! Objects.equals(getType(), entity.getType())) {
            return BoolEntity.FALSE;
        }
        return BoolEntity.valueOf(Objects.equals(getValue(), ((StringEntity)entity).getValue()));
    }

    @Override
    public String toString() {
        return String.format("\"%s\"", value);
    }
}
