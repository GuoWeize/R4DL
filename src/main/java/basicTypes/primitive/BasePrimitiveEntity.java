package basicTypes.primitive;

import basicTypes.BaseEntity;
import exceptions.TypeInvalidException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Abstract base class for all collection types, including boolean, integer, float and string.
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
public abstract class BasePrimitiveEntity extends BaseEntity {

    protected static final String INTEGER = "integer";
    protected static final String FLOAT = "float";
    protected static final String STRING = "string";

    /**
     * Calculate the negative number of given number.
     * @param num to calculate
     * @return the negative number.
     */
    public static IntEntity negative(IntEntity num) {
        return IntEntity.valueOf(- num.getValue());
    }
    public static FloatEntity negative(FloatEntity num) {
        return FloatEntity.valueOf(- num.getValue());
    }

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
    public static FloatEntity calculate(FloatEntity num1, IntEntity num2, String operator) {
        return FloatEntity.valueOf(calculateUtil(num1.getValue(), num2.getValue(), operator));
    }
    public static FloatEntity calculate(IntEntity num1, FloatEntity num2, String operator) {
        return FloatEntity.valueOf(calculateUtil(num1.getValue(), num2.getValue(), operator));
    }
    public static FloatEntity calculate(FloatEntity num1, FloatEntity num2, String operator) {
        return FloatEntity.valueOf(calculateUtil(num1.getValue(), num2.getValue(), operator));
    }

    /**
     * String jointing and string copying
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

    /**
     * Summation of a string of numbers.
     * @param numbers to be summed
     * @return the result.
     */
    public static IntEntity summation(IntEntity... numbers) {
        return IntEntity.valueOf(Arrays.stream(numbers).map(IntEntity::getValue).reduce(0, Integer::sum));
    }
    public static FloatEntity summation(BasePrimitiveEntity... numbers) {
        double result = 0.0;
        for (BasePrimitiveEntity number: numbers) {
            result += getNumValue(number);
        }
        return FloatEntity.valueOf(result);
    }

    /**
     * Product of a string of numbers.
     * @param numbers to be produced
     * @return the result.
     */
    public static IntEntity product(IntEntity... numbers) {
        int result = 1;
        for (IntEntity number: numbers) {
            result *= number.getValue();
        }
        return IntEntity.valueOf(result);
    }
    public static FloatEntity product(BasePrimitiveEntity... numbers) {
        double result = 1.0;
        for (BasePrimitiveEntity number: numbers) {
            result *= getNumValue(number);
        }
        return FloatEntity.valueOf(result);
    }

    /**
     * Maximum of all these numbers.
     * @param numbers all numbers
     * @return the maximum one
     */
    public static IntEntity max(IntEntity... numbers) {
        int result = Integer.MIN_VALUE;
        for (IntEntity number: numbers) {
            result = Math.max(result, number.getValue());
        }
        return IntEntity.valueOf(result);
    }
    public static FloatEntity max(BasePrimitiveEntity... numbers) {
        double result = - Double.MAX_VALUE;
        for (BasePrimitiveEntity number: numbers) {
            result = Math.max(result, getNumValue(number));
        }
        return FloatEntity.valueOf(result);
    }

    /**
     * Minimum of all these numbers.
     * @param numbers all numbers
     * @return the minimum one
     */
    public static IntEntity min(IntEntity... numbers) {
        int result = Integer.MAX_VALUE;
        for (IntEntity number: numbers) {
            result = Math.min(result, number.getValue());
        }
        return IntEntity.valueOf(result);
    }
    public static FloatEntity min(BasePrimitiveEntity... numbers) {
        double result = Double.MAX_VALUE;
        for (BasePrimitiveEntity number: numbers) {
            result = Math.min(result, getNumValue(number));
        }
        return FloatEntity.valueOf(result);
    }

    /**
     * Compare two numbers, including greater, less, not less, not greater.
     * @param entity1 an entity of IntEntity or FloatEntity
     * @param entity2 an entity of IntEntity or FloatEntity
     * @param operator to be executed: > < >= <=
     * @return the result.
     */
    public static BoolEntity compare(BasePrimitiveEntity entity1, BasePrimitiveEntity entity2, String operator) {
        double number1 = getNumValue(entity1);
        double number2 = getNumValue(entity2);
        switch (operator) {
            case "<":
                return number1 < number2 ? BoolEntity.TRUE: BoolEntity.FALSE;
            case ">":
                return number1 > number2 ? BoolEntity.TRUE: BoolEntity.FALSE;
            case "<=":
                return number1 <= number2 ? BoolEntity.TRUE: BoolEntity.FALSE;
            case ">=":
                return number1 >= number2 ? BoolEntity.TRUE: BoolEntity.FALSE;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public final boolean isRequirement() {
        return false;
    }

    /**
     * Get the value of given number.
     * @param num to get
     * @return the value in double.
     */
    private static double getNumValue(BaseEntity num) {
        String type = num.getType();
        if (Objects.equals(type, FLOAT)) {
            return ((FloatEntity)num).getValue();
        }
        else if (Objects.equals(type, INTEGER)) {
            return ((IntEntity)num).getValue();
        }
        throw new TypeInvalidException(type, List.of(FLOAT, INTEGER));
    }

    private static double calculateUtil(double num1, double num2, String operator) {
        switch (operator) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                return num1 / num2;
            default:
                throw new IllegalArgumentException();
        }
    }

}
