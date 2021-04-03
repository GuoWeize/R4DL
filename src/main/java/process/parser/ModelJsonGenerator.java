package process.parser;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import util.Formats;

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
        jg.writeStringField(Formats.TYPE_FIELD, type);
        jg.writeStringField(Formats.NAME_FIELD, name);
        ModelTextParser.parseFields();
        jg.writeEndObject();
    }

    static void generateField(String fieldName, String fieldType) throws IOException {
        jg.writeStringField(fieldName, fieldType);
    }

}
