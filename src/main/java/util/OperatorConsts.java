package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author Guo Weize
 * @date 2021/6/13
 */
public class OperatorConsts {

    private static final Map<String, String> OPS = new HashMap<>(128);
    static {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(
                System.getProperty("user.dir") + "/src/main/resources/operators.properties")
            );
            Enumeration<?> en = props.propertyNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                String property = props.getProperty(key);
                OPS.put(key, property);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Calculations */

    public static final String CALCULATE_ADDITION = OPS.get("calculate_addition");
    public static final String CALCULATE_SUBTRACTION = OPS.get("calculate_subtraction");
    public static final String CALCULATE_MULTIPLE = OPS.get("calculate_multiple");
    public static final String CALCULATE_DIVISION = OPS.get("calculate_division");
    public static final String CALCULATE_SUMMATION = OPS.get("calculate_summation");
    public static final String CALCULATE_MULTIPLICATION = OPS.get("calculate_multiplication");

    /* Comparisons */

    public static final String COMPARE_EQUAL = OPS.get("compare_equal");
    public static final String COMPARE_NOT_EQUAL = OPS.get("compare_not_equal");
    public static final String COMPARE_GREATER = OPS.get("compare_greater");
    public static final String COMPARE_LESS = OPS.get("compare_less");
    public static final String COMPARE_NOT_GREATER = OPS.get("compare_not_greater");
    public static final String COMPARE_NOT_LESS = OPS.get("compare_not_less");
    public static final String MAXIMUM_NUMBER = OPS.get("maximum_number");
    public static final String MINIMUM_NUMBER = OPS.get("minimum_number");

    /* Logical operators */

    public static final String LOGICAL_NOT = OPS.get("logical_not");
    public static final String LOGICAL_AND = OPS.get("logical_and");
    public static final String LOGICAL_OR = OPS.get("logical_or");

    /* Collection operators */

    public static final String COLLECTION_SIZE_OF = OPS.get("collection_size_of");
    public static final String COLLECTION_INCLUDE = OPS.get("collection_include");
    public static final String COLLECTION_IN = OPS.get("collection_in");
    public static final String COLLECTION_MERGE = OPS.get("collection_merge");
    public static final String COLLECTION_GET = OPS.get("collection_get");

    /* Loop operators */

    public static final String LOOP_SIGNAL = OPS.get("loop_signal");
    public static final String ALL_SATISFY = OPS.get("all_satisfy");
    public static final String ANY_SATISFY = OPS.get("any_satisfy");
    public static final String RANGE_SIGNAL = OPS.get("range_signal");
    public static final String RANGE_BEGIN_SIGNAL = OPS.get("range_begin_signal");
    public static final String RANGE_END_SIGNAL = OPS.get("range_end_signal");

    /* String operators */

    public static final String STRING_FIND = OPS.get("string_find");
    public static final String STRING_SUBSTRING = OPS.get("string_substring");
    public static final String SYNONYMS_WORD = OPS.get("synonyms_word");
    public static final String ANTONYM_WORDS = OPS.get("antonym_words");

    public static final Set<String> UNARY_OPERATORS = Set.of(
        LOGICAL_NOT,
        COLLECTION_SIZE_OF,
        CALCULATE_SUBTRACTION
    );
    public static final Set<String> BINARY_OPERATORS = Set.of(
        CALCULATE_ADDITION,
        CALCULATE_SUBTRACTION,
        CALCULATE_MULTIPLE,
        CALCULATE_DIVISION,
        LOGICAL_AND,
        LOGICAL_OR,
        COMPARE_EQUAL,
        COMPARE_NOT_EQUAL,
        COMPARE_GREATER,
        COMPARE_LESS,
        COMPARE_NOT_GREATER,
        COMPARE_NOT_LESS,
        COLLECTION_INCLUDE,
        COLLECTION_IN,
        SYNONYMS_WORD,
        ANTONYM_WORDS
    );
    public static final Set<String> MULTIPLE_ARG_OPERATORS = Set.of(
        MAXIMUM_NUMBER,
        MINIMUM_NUMBER,
        CALCULATE_SUMMATION,
        CALCULATE_MULTIPLICATION,
        LOGICAL_AND,
        LOGICAL_OR,
        COLLECTION_MERGE,
        STRING_FIND,
        STRING_SUBSTRING
    );
    public static final Set<String> CUSTOMIZED_OPERATORS = new HashSet<>(32);

}
