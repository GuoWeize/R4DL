package process.definition.model;

import api.Main;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import codeGenerator.ModelJsonParser;
import util.ModeEnum;
import util.PathConsts;
import util.TypeEnum;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

class ModelParserTest {
    private String json;

    @BeforeEach
    void setUp() {
        File file = new File(PathConsts.file(ModeEnum.MODEL, TypeEnum.JSON));
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