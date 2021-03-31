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
public final class ModelJsonGenerator {

    private static JsonGenerator jg;

    static void initialization() {
        try {
            FileOutputStream file = new FileOutputStream(util.Configs.MODEL_JSON_FILE);
            jg = (new JsonFactory()).createGenerator(file, JsonEncoding.UTF8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void generateModel() throws IOException {
        jg.writeStartArray();
        ModelTextParser.parseEntities();
        jg.writeEndArray();
        jg.flush();
        jg.close();
    }

    static void generateEntity(String type, String name) throws IOException {
        jg.writeStartObject();
        jg.writeStringField("_type_", type);
        jg.writeStringField("_name_", name);
        ModelTextParser.parseFields();
        jg.writeEndObject();
    }

    static void generateField(String fieldName, String fieldType) throws IOException {
        jg.writeStringField(fieldName, fieldType);
    }

}
