package basicTypes.primitive;

import basicTypes.BaseEntity;
import util.ThesaurusReader;

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

    public IntEntity size() {
        return IntEntity.valueOf(value.length());
    }

    public BoolEntity include(StringEntity string) {
        return this.value.contains(string.value) ? BoolEntity.TRUE: BoolEntity.FALSE;
    }

    public IntEntity find(StringEntity string, IntEntity fromIndex) {
        return IntEntity.valueOf(this.value.indexOf(string.value, fromIndex.getValue()));
    }

    public StringEntity substring(IntEntity beginIndex, IntEntity endIndex) {
        return new StringEntity(this.value.substring(beginIndex.getValue(), endIndex.getValue()));
    }

    public static BoolEntity synonym(StringEntity string1, StringEntity string2) {
        return BoolEntity.valueOf(ThesaurusReader.isSynonym(string1.getValue(), string2.getValue()));
    }

    public static BoolEntity antonym(StringEntity string1, StringEntity string2) {
        return BoolEntity.valueOf(ThesaurusReader.isAntonym(string1.getValue(), string2.getValue()));
    }

    public String getValue() {
        return value;
    }

    @Override
    public String getType() {
        return STRING;
    }

    @Override
    public BoolEntity equal(BaseEntity entity) {
        if (! Objects.equals(getType(), entity.getType())) {
            return BoolEntity.FALSE;
        }
        return BoolEntity.valueOf(Objects.equals(getValue(), ((StringEntity)entity).getValue()));
    }

    public static StringEntity newInstance() {
        return StringEntity.valueOf("");
    }

    @Override
    public String toString() {
        return String.format("\"%s\"", value);
    }
}
