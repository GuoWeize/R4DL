package process.requirement;

import base.dynamics.Builder;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class EntityParserTest {
    private String json;

    @BeforeEach
    void setUp() {
        List<String> l = new ArrayList<>();
        l.add("condition");
        l.add("entity");
        l.add("operation");
        l.add("functional");

        Compiler.compile(l);
        Map<String, Class<?>> classes = Compiler.loadClass(l);
        Builder.initialization(classes);

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
    }

    @Test
    void deserialize() {
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