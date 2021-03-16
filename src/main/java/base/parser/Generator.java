package base.parser;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Guo Weize
 * @date 2021/2/25
 */
public class Generator {

    private static JsonGenerator jg;
    public static final String PROJECT_PATH = System.getProperty("user.dir");
    public static final String MODEL_JSON_FILE = PROJECT_PATH + "/src/main/resources/definitionFile/model.json";

    public static void initialization() {
        try {
            FileOutputStream file = new FileOutputStream(MODEL_JSON_FILE);
            jg = (new JsonFactory()).createGenerator(file, JsonEncoding.UTF8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void parseModelFile() throws IOException {
        jg.writeStartArray();
        LanguageParser.parseEntities();
        jg.writeEndArray();
        jg.flush();
        jg.close();
    }

    static void parseEntity(String type, String name) throws IOException {
        jg.writeStartObject();
        jg.writeStringField("_type_", type);
        jg.writeStringField("_name_", name);
        LanguageParser.parseFields();
        jg.writeEndObject();
    }

    static void parseField(String fieldName, String fieldType) throws IOException {
        jg.writeStringField(fieldName, fieldType);
    }

}
