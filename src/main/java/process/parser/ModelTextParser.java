package process.parser;

import util.PathConsts;
import util.FormatsConsts;
import util.TextReader;

import java.io.IOException;

/**
 * @author Guo Weize
 * @date 2021/2/25
 */
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
            e.printStackTrace();
        }
    }

    /**
     * parsing all entities in the model text file.
     * @throws IOException if there is a syntax error
     */
    static void parseEntities() throws IOException {
        String type = TextReader.nextToken();
        while (! TextReader.EMPTY_STRING.equals(type)) {
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
        while (! FormatsConsts.CLOSE_BRACE.equals(fieldName)) {
            StringBuilder fieldType = new StringBuilder();
            String temp = TextReader.nextToken();
            while (! FormatsConsts.SEMICOLON.equals(temp)) {
                fieldType.append(temp);
                temp = TextReader.nextToken();
            }
            ModelJsonGenerator.generateField(fieldName, fieldType.toString());
            fieldName = TextReader.nextToken();
        }
    }

}
