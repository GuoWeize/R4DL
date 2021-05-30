package process;

import base.dynamics.Compiler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import process.definition.ModelJsonParser;
import process.definition.RuleParser;
import process.judge.Processor;
import process.parser.BaseParser;
import process.requirement.EntityParser;
import util.PathConsts;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * @author Guo Weize
 * @date 2021/2/22
 */
public class Demo {

    public static void definitions2json() {
        Processor.initialization();
        BaseParser.run();
    }

    public static void json2java() {
        clearJavaFiles();
        parseJsonFile(PathConsts.MODEL_JSON_FILE, new ModelJsonParser());
        parseJsonFile(PathConsts.RULE_JSON_FILE, new RuleParser());
    }

    public static void judge() {
        Compiler.run();
        parseJsonFile(PathConsts.REQUIREMENT_FILE, new EntityParser());
        Processor.run();
    }

    private static void clearJavaFiles() {
        File file = new File(PathConsts.DYNAMICS_JAVA_CODE_PATH);
        for (File f: Objects.requireNonNull(file.listFiles())) {
            if (f.isFile()) {
                f.delete();
            }
        }
    }

    private static void parseJsonFile(String filePath, JsonDeserializer<?> parser) {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Object.class, parser);
        mapper.registerModule(module);
        try {
            mapper.readValue(readFile(filePath), Object.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private static String readFile(String filePath) {
        File file = new File(filePath);
        long length = file.length();
        byte[] content = new byte[(int) length];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(content);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(content);
    }

    public static void main(String[] args) {
        definitions2json();
        json2java();
        judge();
    }
}
