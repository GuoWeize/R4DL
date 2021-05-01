package process.parser;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import util.FormatsConsts;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * @author Guo Weize
 * @date 2021/4/3
 */
public final class RuleJsonGenerator {

    private static JsonGenerator jg;

    static void initialization() {
        try {
//            FileOutputStream file = new FileOutputStream(PathConsts.RULE_JSON_FILE);
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
        jg.writeStringField(FormatsConsts.RULE_TYPE_FIELD, type);
        RuleTextParser.parseArguments();
        RuleTextParser.parseReturn();
        jg.writeFieldName(FormatsConsts.RULE_LOGIC_FIELD);
        jg.flush();

        String logic = RuleTextParser.parseLogic();
        addLogic(logic);

        jg.writeEndObject();
    }

    static void addLogic(String logic) throws IOException {
        Files.write(
            Paths.get("demo.json"),
            logic.getBytes(StandardCharsets.UTF_8),
            StandardOpenOption.APPEND
        );
    }

    static void generateArguments(List<List<String>> arguments) throws IOException {
        jg.writeFieldName(FormatsConsts.RULE_ARGUMENT_FIELD);
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
        jg.writeStringField(FormatsConsts.RULE_RETURN_FIELD, returnType);
    }

}
