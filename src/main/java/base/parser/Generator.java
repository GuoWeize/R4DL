package base.parser;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

/**
 * @author Guo Weize
 * @date 2021/2/25
 */
public class Generator {

    private static JsonGenerator jg;

    public static void initialization() {
        try {
            jg = (new JsonFactory()).createGenerator(System.out, JsonEncoding.UTF8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void parseModelFile() throws IOException {
        jg.writeStartObject();
        LanguageParser.parseTypes();
        LanguageParser.parseRequirements();
        jg.writeEndObject();
        jg.flush();
        jg.close();
    }

    static void parseType() throws IOException {
        jg.writeFieldName("type");
        jg.writeStartObject();
        LanguageParser.parseEntities(true);
        jg.writeEndObject();
    }

    static void parseRequirement() throws IOException {
        jg.writeFieldName("requirement");
        jg.writeStartObject();
        LanguageParser.parseEntities(false);
        jg.writeEndObject();
    }

    static void parseEntity(String entityName) throws IOException {
        jg.writeFieldName(entityName);
        jg.writeStartObject();
        LanguageParser.parseField();
        jg.writeEndObject();
    }

    static void parseField(String fieldName, String fieldType) throws IOException {
        jg.writeStringField(fieldName, fieldType);
    }

}
