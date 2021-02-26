package base.parser;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

/**
 * @author Guo Weize
 * @date 2021/2/22
 */
public class Demo {

    public static void main(String[] args) throws IOException {
        JsonFactory factory = new JsonFactory();
        try (JsonGenerator jg = factory.createGenerator(System.out, JsonEncoding.UTF8)) {
            jg.writeStartObject();

            jg.writeFieldName("type");

            jg.writeStartObject();
            jg.writeFieldName("operation");
            jg.writeStartObject();
            jg.writeStringField("reaction", "string");
            jg.writeStringField("isAble", "string");
            jg.writeStringField("isNot", "string");
            jg.writeEndObject();

            jg.writeEndObject();

            jg.writeEndObject();
        }
    }

}
