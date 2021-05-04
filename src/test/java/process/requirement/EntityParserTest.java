package process.requirement;

import base.dynamics.Compiler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.PathConsts;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

class EntityParserTest {
    private String json;

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
    }

    @Test
    void deserialize() {
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

        json = new String(content);
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Object.class, new EntityParser());
        mapper.registerModule(module);

        try {
            Object readValue = mapper.readValue(json, Object.class);
            System.out.println(readValue);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}