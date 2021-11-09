package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * @author Guo Weize
 * @date 2021/3/27
 */
public final class PathConsts {

    private static final String NO_SUCH_FILE = "";
    private static final String LANGUAGE_FILE_SUFFIX = ".txt";
    private static final String JSON_FILE_SUFFIX = ".json";
    public static final String PROJECT_PATH = System.getProperty("user.dir");

    private static final Map<String, String> CONFIGS = new HashMap<>(32);
    static {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(PROJECT_PATH + "/src/main/resources/properties/config.properties"));
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

    public static final String DYNAMICS_JAVA_CODE_DIR = PROJECT_PATH + CONFIGS.get("dynamics_java_code_dir");
    public static final String DYNAMICS_CLASS_DIR = PROJECT_PATH + CONFIGS.get("dynamics_class_dir");
    public static final String MODELS_DIR = PROJECT_PATH + CONFIGS.get("models_dir");
    public static final String RULES_DIR = PROJECT_PATH + CONFIGS.get("rules_dir");
    public static final String REQUIREMENTS_DIR = PROJECT_PATH + CONFIGS.get("requirements_dir");
    public static final String THESAURUS_DIR = PROJECT_PATH + CONFIGS.get("thesaurus_dir");

    public static final String RULE_JAVA_CLASS = CONFIGS.get("rule_java_class_name");
    public static final String RULE_JAVA_NAME = RULE_JAVA_CLASS + ".java";
    public static final String RULE_JAVA_FILE = DYNAMICS_JAVA_CODE_DIR + RULE_JAVA_NAME;

    public static final String DEFAULT_MODEL_NAME = CONFIGS.get("default_model_name");
    public static final String DEFAULT_RULE_NAME = CONFIGS.get("default_rule_name");
    public static final String DEFAULT_REQUIREMENT_NAME = CONFIGS.get("default_requirement_name");

    public static final String SYNONYM_FILE_NAME = THESAURUS_DIR + CONFIGS.get("synonym_file_name");
    public static final String ANTONYM_FILE_NAME = THESAURUS_DIR + CONFIGS.get("antonym_file_name");

    private static final Map<String, String> FILES = new HashMap<>(8);

    public static void initialization(String model, String rule, String requirement) {
        if (model == null || Objects.equals(model, NO_SUCH_FILE)) {
            model = DEFAULT_MODEL_NAME;
        }
        if (rule == null || Objects.equals(rule, NO_SUCH_FILE)) {
            rule = DEFAULT_RULE_NAME;
        }
        if (requirement == null || Objects.equals(requirement, NO_SUCH_FILE)) {
            requirement = DEFAULT_REQUIREMENT_NAME;
        }
        FILES.put(ModeEnum.MODEL.name() + TypeEnum.LANGUAGE.name(),
            MODELS_DIR + model + LANGUAGE_FILE_SUFFIX);
        FILES.put(ModeEnum.RULE.name() + TypeEnum.LANGUAGE.name(),
            RULES_DIR + rule + LANGUAGE_FILE_SUFFIX);
        FILES.put(ModeEnum.REQUIREMENT.name() + TypeEnum.LANGUAGE.name(),
            REQUIREMENTS_DIR + requirement + LANGUAGE_FILE_SUFFIX);
        FILES.put(ModeEnum.MODEL.name() + TypeEnum.JSON.name(),
            MODELS_DIR + model + JSON_FILE_SUFFIX);
        FILES.put(ModeEnum.RULE.name() + TypeEnum.JSON.name(),
            RULES_DIR + rule + JSON_FILE_SUFFIX);
        FILES.put(ModeEnum.REQUIREMENT.name() + TypeEnum.JSON.name(),
            REQUIREMENTS_DIR + requirement + JSON_FILE_SUFFIX);
    }

    public static String file(ModeEnum mode, TypeEnum type) {
        return FILES.getOrDefault(mode.name() + type.name(), NO_SUCH_FILE);
    }

}
