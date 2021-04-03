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

    public static final String TYPE_FIELD = FORMATS.get("model_type_field");
    public static final String NAME_FIELD = FORMATS.get("model_name_field");
    public static final String FUNCTIONS_FIELD = FORMATS.get("functions_field");
    public static final String RULES_FIELD = FORMATS.get("rules_field");

    public static final String ENTITY_DEFINE = FORMATS.get("entity_model_define");
    public static final String REQUIREMENT_DEFINE = FORMATS.get("requirement_model_define");
    public static final String FUNCTION_DEFINE = FORMATS.get("function_define");
    public static final String RULE_DEFINE = FORMATS.get("rule_define");

    public static final String SUM_OF_NUMBERS =FORMATS.get("sum_of_numbers");


    public static void main(String[] args) {
        System.out.println(SUM_OF_NUMBERS);
    }

}
