package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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

    public static final String MODEL_TYPE_FIELD = FORMATS.get("model_type_field");
    public static final String MODEL_NAME_FIELD = FORMATS.get("model_name_field");
    public static final String RULE_TYPE_FIELD = FORMATS.get("rule_type_field");
    public static final String RULE_ARGUMENT_FIELD = FORMATS.get("rule_argument_types_field");
    public static final String RULE_RETURN_FIELD = FORMATS.get("rule_return_type_field");
    public static final String RULE_LOGIC_FIELD = FORMATS.get("rule_body_field");

    public static final String ENTITY_DEFINE = FORMATS.get("entity_model_define");
    public static final String REQUIREMENT_DEFINE = FORMATS.get("requirement_model_define");
    public static final String FUNCTION_DEFINE = FORMATS.get("function_define");
    public static final String RULE_DEFINE = FORMATS.get("rule_define");

    public static final String SUM_OF_NUMBERS = FORMATS.get("sum_of_numbers");
    public static final String PRODUCT_OF_NUMBERS = FORMATS.get("product_of_numbers");
    public static final String AND_BOOL_OPERATE = FORMATS.get("and_bool_operate");
    public static final String OR_BOOL_OPERATE = FORMATS.get("or_bool_operate");
    public static final String COLLECTION_MERGE = FORMATS.get("collection_merge");


    public static void main(String[] args) {
        System.out.println(SUM_OF_NUMBERS);
    }

}
