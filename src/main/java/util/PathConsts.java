package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Guo Weize
 * @date 2021/3/27
 */
public final class PathConsts {

    public static final String PROJECT_PATH = System.getProperty("user.dir");

    private static final Map<String, String> CONFIGS = new HashMap<>(32);
    static {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(PROJECT_PATH + "/src/main/resources/config.properties"));
            Enumeration<?> en = props.propertyNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                String property = props.getProperty(key);
                CONFIGS.put(key, property);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final String REQUIREMENT_PATH = PROJECT_PATH + CONFIGS.get("requirement_path");
    public static final String DEFINITION_PATH = PROJECT_PATH + CONFIGS.get("definition_path");
    public static final String DYNAMICS_JAVA_CODE_PATH = PROJECT_PATH + CONFIGS.get("dynamics_java_code_path");
    public static final String DYNAMICS_CLASS_PATH = PROJECT_PATH + CONFIGS.get("dynamics_class_path");

    public static final String MODEL_TEXT_FILE = DEFINITION_PATH + CONFIGS.get("model_text_file_name");
    public static final String MODEL_JSON_FILE = DEFINITION_PATH + CONFIGS.get("model_json_file_name");
    public static final String RULE_TEXT_FILE = DEFINITION_PATH + CONFIGS.get("rule_text_file_name");
    public static final String RULE_JSON_FILE = DEFINITION_PATH + CONFIGS.get("rule_json_file_name");

    public static final String RULE_JAVA_NAME = CONFIGS.get("rule_java_file_name");
    public static final String RULE_JAVA_FILE = DYNAMICS_JAVA_CODE_PATH + RULE_JAVA_NAME;
}
