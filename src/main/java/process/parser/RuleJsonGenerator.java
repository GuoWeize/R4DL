package process.parser;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import util.Configs;
import util.Formats;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Guo Weize
 * @date 2021/4/3
 */
public final class RuleJsonGenerator {

    private static JsonGenerator jg;

    static void initialization() {
        try {
            FileOutputStream file = new FileOutputStream(Configs.RULE_JSON_FILE);
            jg = (new JsonFactory()).createGenerator(file, JsonEncoding.UTF8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void generateModel() throws IOException {
        jg.writeStartObject();

        jg.writeFieldName(Formats.FUNCTIONS_FIELD);
        jg.writeStartObject();
        RuleTextParser.parseFunctions();
        jg.writeEndObject();

        jg.writeFieldName(Formats.RULES_FIELD);
        jg.writeStartObject();
        RuleTextParser.parseRules();
        jg.writeEndObject();

        jg.writeEndObject();
    }

}
