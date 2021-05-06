package process.parser;

import lombok.extern.slf4j.Slf4j;
import util.PathConsts;
import util.FormatsConsts;
import util.TextReader;

import java.io.IOException;
import java.util.Objects;

/**
 * @author Guo Weize
 * @date 2021/2/25
 */
@Slf4j
public final class ModelTextParser {

    /**
     * entry of the function of parsing the model text file.
     */
    public static void parseModelFile() {
        TextReader.readFile(PathConsts.MODEL_TEXT_FILE);
        ModelJsonGenerator.initialization();
        try {
            ModelJsonGenerator.generateModel();
        } catch (IOException e) {
            log.error("Can not write file: " + PathConsts.MODEL_JSON_FILE, e);
        }
        log.info("Finish parse model definition file: " + PathConsts.MODEL_TEXT_FILE);
    }

    /**
     * parsing all entities in the model text file.
     * @throws IOException if there is a syntax error
     */
    static void parseEntities() throws IOException {
        String type = TextReader.nextToken();
        while (! Objects.equals(type, TextReader.EMPTY_STRING)) {
            String name = TextReader.nextToken();
            TextReader.nextTokenWithTest(FormatsConsts.OPEN_BRACE);
            ModelJsonGenerator.generateEntity(type, name);
            type = TextReader.nextToken();
        }
    }

    /**
     * parsing all fields in one entity.
     * @throws IOException if there is a syntax error
     */
    static void parseFields() throws IOException {
        String fieldName = TextReader.nextToken();
        while (! Objects.equals(fieldName, FormatsConsts.CLOSE_BRACE)) {
            StringBuilder fieldType = new StringBuilder();
            String temp = TextReader.nextToken();
            while (! Objects.equals(temp, FormatsConsts.SEMICOLON)) {
                fieldType.append(temp);
                temp = TextReader.nextToken();
            }
            ModelJsonGenerator.generateField(fieldName, fieldType.toString());
            fieldName = TextReader.nextToken();
        }
    }

}
