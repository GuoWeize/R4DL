package base.dynamics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import process.judge.Processor;
import process.requirement.EntityParser;
import util.PathConsts;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProcessorTest {

    @BeforeEach
    void setUp() {
        List<String> l = List.of(
            "condition",
            "entity",
            "operation",
            "functional",
            "_rule_"
        );
        Compiler.compile(l);
        Processor.initialization();
    }

    @Test
    void process() {
        String path = PathConsts.REQUIREMENT_FILE;
        File file = new File(path);
        long length = file.length();
        byte[] content = new byte[(int) length];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(content);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String json = new String(content);
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Object.class, new EntityParser());
        mapper.registerModule(module);

        try {
            Object readValue = mapper.readValue(json, Object.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Processor.judgeRules();

    }
}