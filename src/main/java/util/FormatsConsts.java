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
 * @date 2021/4/3
 */
public final class FormatsConsts {

    private static final Map<String, String> FORMATS = new HashMap<>(128);
    static {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(
                System.getProperty("user.dir") + "/src/main/resources/formats.properties")
            );
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

    /* Definition words */

    public static final String DEFINE_ENTITY = FORMATS.get("define_entity");
    public static final String DEFINE_REQUIREMENT = FORMATS.get("define_requirement");
    public static final String DEFINE_FUNCTION = FORMATS.get("define_function");
    public static final String DEFINE_RULE = FORMATS.get("define_rule");

    /* Symbols */

    public static final String COMMA = ",";
    public static final String SEPARATOR = "/";
    public static final String SEMICOLON = ";";
    public static final String OPEN_BRACE = "{";
    public static final String CLOSE_BRACE = "}";
    public static final String OPEN_PAREN = "(";
    public static final String CLOSE_PAREN = ")";
    public static final String OPEN_BRACKET = "[";
    public static final String CLOSE_BRACKET = "]";
    public static final String EQUAL = "==";
    public static final String NOT_EQUAL = "!=";
    public static final String NOT_LESS = ">=";
    public static final String NOT_GREATER = "<=";
    public static final String ARROW = "->";
    public static final String PARAMETER_DELIMITER = FORMATS.get("parameter_delimiter");
    public static final String VARIABLE_PARAMETERS = FORMATS.get("variable_parameters");
    static final Set<String> MULTIPLE_CHARS_SIGNAL = Set.of(
            EQUAL, NOT_EQUAL, NOT_LESS, NOT_GREATER, ARROW
    );

    /* Json file formats: fields' names */

    public static final String MODEL_TYPE_FIELD = FORMATS.get("model_type_field");
    public static final String MODEL_NAME_FIELD = FORMATS.get("model_name_field");
    public static final String RULE_TYPE_FIELD = FORMATS.get("rule_type_field");
    public static final String RULE_ARGUMENT_FIELD = FORMATS.get("rule_argument_types_field");
    public static final String RULE_RETURN_FIELD = FORMATS.get("rule_return_type_field");
    public static final String RULE_LOGIC_FIELD = FORMATS.get("rule_logic_field");

    /* Requirement elements formats */

    public static final String ENTITY_SIGNAL = FORMATS.get("entity_signal");
    public static final String MAP_SIGNAL = FORMATS.get("map_signal");
    public static final String LIST_SIGNAL = FORMATS.get("list_signal");
    public static final String SET_SIGNAL = FORMATS.get("set_signal");
    public static final String KEY_SIGNAL = FORMATS.get("key_signal");
    public static final String VALUE_SIGNAL = FORMATS.get("value_signal");

    /* Calculations */

    public static final String CALCULATE_ADDITION = FORMATS.get("calculate_addition");
    public static final String CALCULATE_SUBTRACTION = FORMATS.get("calculate_subtraction");
    public static final String CALCULATE_MULTIPLE = FORMATS.get("calculate_multiple");
    public static final String CALCULATE_DIVISION = FORMATS.get("calculate_division");
    public static final String CALCULATE_SUMMATION = FORMATS.get("calculate_summation");
    public static final String CALCULATE_MULTIPLICATION = FORMATS.get("calculate_multiplication");

    /* Comparisons */

    public static final String COMPARE_EQUAL = FORMATS.get("compare_equal");
    public static final String COMPARE_NOT_EQUAL = FORMATS.get("compare_not_equal");
    public static final String COMPARE_GREATER = FORMATS.get("compare_greater");
    public static final String COMPARE_LESS = FORMATS.get("compare_less");
    public static final String COMPARE_NOT_GREATER = FORMATS.get("compare_not_greater");
    public static final String COMPARE_NOT_LESS = FORMATS.get("compare_not_less");

    /* Logical operators */

    public static final String LOGICAL_NOT = FORMATS.get("logical_not");
    public static final String LOGICAL_AND = FORMATS.get("logical_and");
    public static final String LOGICAL_OR = FORMATS.get("logical_or");

    /* Collection operators */

    public static final String COLLECTION_SIZE_OF = FORMATS.get("collection_size_of");
    public static final String COLLECTION_INCLUDE = FORMATS.get("collection_include");
    public static final String COLLECTION_IN = FORMATS.get("collection_in");
    public static final String COLLECTION_MERGE = FORMATS.get("collection_merge");

    /* Loop operators */

    public static final String LOOP_SIGNAL = FORMATS.get("loop_signal");
    public static final String ALL_SATISFY = FORMATS.get("all_satisfy");
    public static final String ANY_SATISFY = FORMATS.get("any_satisfy");
    public static final String RANGE_SIGNAL = FORMATS.get("range_signal");
    public static final String RANGE_BEGIN_SIGNAL = FORMATS.get("range_begin_signal");
    public static final String RANGE_END_SIGNAL = FORMATS.get("range_end_signal");

    public static final Set<String> UNARY_OPERATORS = Set.of(
        LOGICAL_NOT,
        COLLECTION_SIZE_OF
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
        COLLECTION_IN
    );
    public static final Set<String> MULTIPLE_ARG_OPERATORS = Set.of(
        CALCULATE_SUMMATION,
        CALCULATE_MULTIPLICATION,
        LOGICAL_AND,
        LOGICAL_OR,
        COLLECTION_MERGE
    );
    public static final Set<String> CUSTOMIZED_OPERATORS = new HashSet<>(32);

}
