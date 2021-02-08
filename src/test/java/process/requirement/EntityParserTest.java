package process.requirement;

import base.dynamics.Builder;
import base.dynamics.Compiler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class EntityParserTest {

    @BeforeEach
    void setUp() {
        List<String> l = new ArrayList<>();
        l.add("Rule");
        l.add("Condition");
        l.add("Entity");
        l.add("Functional");
        l.add("Operation");

        Compiler.compile(l);
        var classes = Compiler.loadClass(l);
        Builder.initialization(classes);
    }

    @Test
    void deserialize() {
        String json = "[\n" +
                "    {\n" +
                "        \"_\": true,\n" +
                "        \"Functional\": {\n" +
                "            \"event\": {\n" +
                "                \"_set_\": []\n" +
                "            },\n" +
                "            \"agent\": {\n" +
                "                \"_\": true,\n" +
                "                \"Entity\": {}\n" +
                "            },\n" +
                "            \"operation\": {\n" +
                "                \"_\": true,\n" +
                "                \"Operation\": {}\n" +
                "            },\n" +
                "            \"input\": {\n" +
                "                \"_set_\": []\n" +
                "            },\n" +
                "            \"output\": {\n" +
                "                \"_set_\": []\n" +
                "            },\n" +
                "            \"restriction\": {\n" +
                "                \"_set_\": []\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "]";

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