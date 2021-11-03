package types.primitive;

import types.BaseEntity;

import java.util.Collections;
import java.util.Objects;

/**
 * Abstract base class for all primitive types, including boolean, integer, float and string.
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
public abstract class BasePrimitive extends BaseEntity {

    /**
     * Calculate basic operations of two numbers, including addition, subtraction, multiplication, division.
     * @param operator to be executed: + - * /
     * @return the result.
     */
    public static IntEntity calculate(IntEntity num1, IntEntity num2, String operator) {
        int integer1 = num1.getValue();
        int integer2 = num2.getValue();
        switch (operator) {
            case "+":
                return IntEntity.valueOf(integer1 + integer2);
            case "-":
                return IntEntity.valueOf(integer1 - integer2);
            case "*":
                return IntEntity.valueOf(integer1 * integer2);
            case "/":
                return IntEntity.valueOf(integer1 / integer2);
            default:
                throw new IllegalArgumentException();
        }
    }
    public static FloatEntity calculate(BaseNumber num1, BaseNumber num2, String operator) {
        double number1 = num1.getNum();
        double number2 = num2.getNum();
        switch (operator) {
            case "+":
                return FloatEntity.valueOf(number1 + number2);
            case "-":
                return FloatEntity.valueOf(number1 - number2);
            case "*":
                return FloatEntity.valueOf(number1 * number2);
            case "/":
                return FloatEntity.valueOf(number1 / number2);
            default:
                throw new IllegalArgumentException();
        }
    }

    /** Or string jointing and string copying.
     * @param operator + or *
     * @return string result
     */
    public static StringEntity calculate(StringEntity string1, StringEntity string2, String operator) {
        if (! Objects.equals(operator, "+")) {
            throw new IllegalArgumentException();
        }
        return StringEntity.valueOf(string1.getValue() + string2.getValue());
    }
    public static StringEntity calculate(StringEntity string, IntEntity integer, String operator) {
        if (! Objects.equals(operator, "*")) {
            throw new IllegalArgumentException();
        }
        return StringEntity.valueOf(String.join("", Collections.nCopies(integer.getValue(), string.getValue())));
    }

    @Override
    public final BoolEntity isNull() {
        return BoolEntity.FALSE;
    }

}
