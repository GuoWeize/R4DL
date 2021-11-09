package types.primitive;

import java.util.Arrays;

/**
 * @author Guo Weize
 * @date 2021/10/2
 */
public abstract class BaseNumber extends BasePrimitive {

    /**
     * get the true number
     * @return double
     */
    protected abstract double getNum();

    /**
     * Summation of a string of numbers.
     * @param numbers to be summed
     * @return the result.
     */
    public static IntEntity summation(IntEntity... numbers) {
        return IntEntity.valueOf(
            Arrays.stream(numbers).map(IntEntity::getValue).reduce(0, Integer::sum)
        );
    }
    public static FloatEntity summation(BaseNumber... numbers) {
        double result = 0.0;
        for (BaseNumber number: numbers) {
            result += number.getNum();
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
    public static FloatEntity product(BaseNumber... numbers) {
        double result = 1.0;
        for (BaseNumber number: numbers) {
            result *= number.getNum();
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
    public static FloatEntity max(BaseNumber... numbers) {
        double result = - Double.MAX_VALUE;
        for (BaseNumber number: numbers) {
            result = Math.max(result, number.getNum());
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
    public static FloatEntity min(BaseNumber... numbers) {
        double result = Double.MAX_VALUE;
        for (BaseNumber number: numbers) {
            result = Math.min(result, number.getNum());
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
    public static BoolEntity compare(BaseNumber entity1, BaseNumber entity2, String operator) {
        double number1 = entity1.getNum();
        double number2 = entity2.getNum();
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

}
