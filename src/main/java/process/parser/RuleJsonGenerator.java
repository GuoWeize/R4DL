package process.parser;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import util.Formats;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author Guo Weize
 * @date 2021/4/3
 */
public final class RuleJsonGenerator {

    private static JsonGenerator jg;

    static void initialization() {
        try {
//            FileOutputStream file = new FileOutputStream(Configs.RULE_JSON_FILE);
            FileOutputStream file = new FileOutputStream("demo.json");
            jg = (new JsonFactory()).createGenerator(file, JsonEncoding.UTF8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void generateRules() throws IOException {
        jg.writeStartObject();
        RuleTextParser.parseRules();
        jg.writeEndObject();
        jg.flush();
        jg.close();
    }

    static void generateRule(String name, String type) throws IOException {
        jg.writeFieldName(name);
        jg.writeStartObject();
        jg.writeStringField(Formats.RULE_TYPE_FIELD, type);
        RuleTextParser.parseArguments();
        RuleTextParser.parseReturn();
        RuleTextParser.parseLogic();
        jg.writeEndObject();
    }

    static void generateArguments(List<List<String>> arguments) throws IOException {
        jg.writeFieldName(Formats.RULE_ARGUMENT_FIELD);
        jg.writeStartArray();
        for (List<String> argument: arguments) {
            jg.writeStartArray();
            for (String singleType: argument) {
                jg.writeString(singleType);
            }
            jg.writeEndArray();
        }
        jg.writeEndArray();
    }

    static void generateReturn(String returnType) throws IOException {
        jg.writeStringField(Formats.RULE_RETURN_FIELD, returnType);
    }

    static void generatePreOperator(String operator) throws IOException {

    }

}
