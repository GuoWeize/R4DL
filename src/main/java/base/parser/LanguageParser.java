package base.parser;

import java.io.IOException;
import java.util.*;

/**
 * @author Guo Weize
 * @date 2021/2/25
 */
public class LanguageParser {

    private static void initialization() {
        Util.readModelFile();
        Generator.initialization();
    }

    public static void parseModelFile() {
        initialization();
        try {
            Generator.parseModelFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void parseEntities() throws IOException {
        String type = Util.nextToken();
        while (! "".equals(type)) {
            String name = Util.nextToken();
            if (! "{".equals(Util.nextToken())) {
                throw new InvalidPropertiesFormatException("need a \"{\"");
            }
            Generator.parseEntity(type, name);
            type = Util.nextToken();
        }
    }

    static void parseFields() throws IOException {
        String fieldName = Util.nextToken();
        while (! "}".equals(fieldName)) {
            StringBuilder fieldType = new StringBuilder();
            String temp = Util.nextToken();
            while (! ";".equals(temp)) {
                fieldType.append(temp);
                temp = Util.nextToken();
            }
            Generator.parseField(fieldName, fieldType.toString());
            fieldName = Util.nextToken();
        }
    }

    public static void main(String[] args) {
        parseModelFile();
    }

}
