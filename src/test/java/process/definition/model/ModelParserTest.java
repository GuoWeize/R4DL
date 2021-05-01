package process.definition.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import process.definition.ModelJsonParser;
import util.PathConsts;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

class ModelParserTest {
    private String json;

    @BeforeEach
    void setUp() {
        File file = new File(PathConsts.MODEL_JSON_FILE);
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
        module.addDeserializer(Object.class, new ModelJsonParser());
        mapper.registerModule(module);

        try {
            Object readValue = mapper.readValue(json, Object.class);
            System.out.println(readValue);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}