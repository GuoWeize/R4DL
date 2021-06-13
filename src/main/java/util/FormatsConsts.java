package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
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
    public static final String DEFINE_REVERSIBLE = FORMATS.get("define_reversible");
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
    public static final String LINK_SIGNAL = FORMATS.get("link_signal");

}
