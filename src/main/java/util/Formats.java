package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author Guo Weize
 * @date 2021/4/3
 */
public final class Formats {

    private static final Map<String, String> FORMATS = new HashMap<>(32);
    static {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/formats.properties"));
            Enumeration<?> en = props.propertyNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                String property = props.getProperty(key);
                FORMATS.put(key, property);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Json file formats: fields' names */

    public static final String MODEL_TYPE_FIELD = FORMATS.get("model_type_field");
    public static final String MODEL_NAME_FIELD = FORMATS.get("model_name_field");
    public static final String RULE_TYPE_FIELD = FORMATS.get("rule_type_field");
    public static final String RULE_ARGUMENT_FIELD = FORMATS.get("rule_argument_types_field");
    public static final String RULE_RETURN_FIELD = FORMATS.get("rule_return_type_field");
    public static final String RULE_LOGIC_FIELD = FORMATS.get("rule_logic_field");

    /* Specific reserved words */

    public static final String ENTITY_DEFINE = FORMATS.get("entity_model_define");
    public static final String REQUIREMENT_DEFINE = FORMATS.get("requirement_model_define");
    public static final String FUNCTION_DEFINE = FORMATS.get("function_define");
    public static final String RULE_DEFINE = FORMATS.get("rule_define");

    /* Unitary operators */

    public static final String NOT_BOOLEAN = FORMATS.get("not_boolean");
    public static final String SIZE_OF_COLLECTION = FORMATS.get("size_of_collection");

    /* Binary operators */

    public static final String ADDITION_CALCULATE = FORMATS.get("addition_calculate");
    public static final String SUBTRACTION_CALCULATE = FORMATS.get("subtraction_calculate");
    public static final String MULTIPLE_CALCULATE = FORMATS.get("multiple_calculate");
    public static final String DIVISION_CALCULATE = FORMATS.get("division_calculate");
    public static final String EQUAL_COMPARISON = FORMATS.get("equal_comparison");
    public static final String NOT_EQUAL_COMPARISON = FORMATS.get("not_equal_comparison");
    public static final String GREATER_COMPARISON = FORMATS.get("greater_comparison");
    public static final String LESS_COMPARISON = FORMATS.get("less_comparison");
    public static final String NOT_GREATER_COMPARISON = FORMATS.get("not_greater_comparison");
    public static final String NOT_LESS_COMPARISON = FORMATS.get("not_less_comparison");
    public static final String COLLECTION_INCLUDE = FORMATS.get("collection_include");

    /* Multiple arguments operators */

    public static final String SUM_OF_NUMBERS = FORMATS.get("sum_of_numbers");
    public static final String PRODUCT_OF_NUMBERS = FORMATS.get("product_of_numbers");
    public static final String AND_BOOL_OPERATE = FORMATS.get("and_boolean");
    public static final String OR_BOOL_OPERATE = FORMATS.get("or_boolean");
    public static final String COLLECTION_MERGE = FORMATS.get("collection_merge");

    /* loop operators */

    public static final String LOOP_BEGIN_SIGNAL = FORMATS.get("loop_begin_signal");
    public static final String ALL_SATISFY = FORMATS.get("all_satisfy");
    public static final String ANY_SATISFY = FORMATS.get("any_satisfy");
    public static final String RANGE_SIGNAL = FORMATS.get("range_signal");
    public static final String RANGE_BEGIN_SIGNAL = FORMATS.get("range_begin_signal");
    public static final String RANGE_END_SIGNAL = FORMATS.get("range_end_signal");

    public static final Set<String> UNITARY_OPERATORS = new HashSet<>();
    public static final Set<String> BINARY_OPERATORS = new HashSet<>();
    public static final Set<String> MULTIPLE_ARG_OPERATORS = new HashSet<>();
    public static final Set<String> CUSTOMIZED_OPERATORS = new HashSet<>();
    static {
        UNITARY_OPERATORS.add(NOT_BOOLEAN);
        UNITARY_OPERATORS.add(SIZE_OF_COLLECTION);

        BINARY_OPERATORS.add(ADDITION_CALCULATE);
        BINARY_OPERATORS.add(SUBTRACTION_CALCULATE);
        BINARY_OPERATORS.add(MULTIPLE_CALCULATE);
        BINARY_OPERATORS.add(DIVISION_CALCULATE);
        BINARY_OPERATORS.add(EQUAL_COMPARISON);
        BINARY_OPERATORS.add(NOT_EQUAL_COMPARISON);
        BINARY_OPERATORS.add(GREATER_COMPARISON);
        BINARY_OPERATORS.add(LESS_COMPARISON);
        BINARY_OPERATORS.add(NOT_GREATER_COMPARISON);
        BINARY_OPERATORS.add(NOT_LESS_COMPARISON);
        BINARY_OPERATORS.add(COLLECTION_INCLUDE);

        MULTIPLE_ARG_OPERATORS.add(Formats.SUM_OF_NUMBERS);
        MULTIPLE_ARG_OPERATORS.add(Formats.PRODUCT_OF_NUMBERS);
        MULTIPLE_ARG_OPERATORS.add(Formats.AND_BOOL_OPERATE);
        MULTIPLE_ARG_OPERATORS.add(Formats.OR_BOOL_OPERATE);
        MULTIPLE_ARG_OPERATORS.add(Formats.COLLECTION_MERGE);
    }

    public static void main(String[] args) {
        System.out.println(SUM_OF_NUMBERS);
    }

}
