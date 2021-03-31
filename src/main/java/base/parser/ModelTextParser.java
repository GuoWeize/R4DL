package base.parser;

import util.Configs;

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
        FileReader.readFile(Configs.MODEL_TEXT_FILE);
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
        String type = FileReader.nextToken();
        while (! FileReader.EMPTY_STRING.equals(type)) {
            String name = FileReader.nextToken();
            if (! FileReader.OPEN_BRACE.equals(FileReader.nextToken())) {
                throw new IOException("need a \"{\" in file " + Configs.MODEL_TEXT_FILE);
            }
            ModelJsonGenerator.generateEntity(type, name);
            type = FileReader.nextToken();
        }
    }

    /**
     * parsing all fields in one entity.
     * @throws IOException if there is a syntax error
     */
    static void parseFields() throws IOException {
        String fieldName = FileReader.nextToken();
        while (! FileReader.CLOSE_BRACE.equals(fieldName)) {
            StringBuilder fieldType = new StringBuilder();
            String temp = FileReader.nextToken();
            while (! FileReader.SEMICOLON.equals(temp)) {
                fieldType.append(temp);
                temp = FileReader.nextToken();
            }
            ModelJsonGenerator.generateField(fieldName, fieldType.toString());
            fieldName = FileReader.nextToken();
        }
    }

}
