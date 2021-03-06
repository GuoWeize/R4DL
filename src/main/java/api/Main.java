package api;

import dynamics.Compiler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import codeGenerator.ModelJsonParser;
import codeGenerator.RuleParser;
import judge.Processor;
import languageParser.BaseParser;
import reqParser.EntityParser;
import util.ModeEnum;
import util.PathConsts;
import util.TypeEnum;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @author Guo Weize
 * @date 2021/2/22
 */
public class Main {

    private static final Map<ModeEnum, JsonDeserializer<?>> PARSERS = Map.ofEntries(
        Map.entry(ModeEnum.MODEL, new ModelJsonParser()),
        Map.entry(ModeEnum.RULE, new RuleParser()),
        Map.entry(ModeEnum.REQUIREMENT, new EntityParser())
    );

    public static void run(String model, String rule, String requirement) {
        PathConsts.initialization(model, rule, requirement);
        definitions2json();
        json2java();
        judge();
    }

    public static void definitions2json() {
        Processor.initialization();
        BaseParser.run();
    }

    public static void json2java() {
        clearJavaFiles();
        parseJsonFile(ModeEnum.MODEL);
        parseJsonFile(ModeEnum.RULE);
    }

    public static void judge() {
        Compiler.run();
        parseJsonFile(ModeEnum.REQUIREMENT);
        Processor.run();
    }

    private static void clearJavaFiles() {
        File file = new File(PathConsts.DYNAMICS_JAVA_CODE_DIR);
        for (File f: Objects.requireNonNull(file.listFiles())) {
            if (f.isFile()) {
                f.delete();
            }
        }
    }

    private static void parseJsonFile(ModeEnum mode) {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Object.class, PARSERS.get(mode));
        mapper.registerModule(module);
        try {
            mapper.readValue(readFile(PathConsts.file(mode, TypeEnum.JSON)), Object.class);
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
        run(null, null, null);
    }
}
