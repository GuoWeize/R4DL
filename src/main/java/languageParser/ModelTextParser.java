package languageParser;

import lombok.extern.slf4j.Slf4j;
import util.FormatsConsts;
import util.TextReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Guo Weize
 * @date 2021/2/25
 */
@Slf4j
public final class ModelTextParser extends BaseParser {

    private static final List<String> ENTITIES = new ArrayList<>();

    /**
     * entry of the function of parsing the model text file.
     * parsing all entities in the model text file.
     * @throws IOException if there is a syntax error
     */
    protected static String parse() throws IOException {
        String type = TextReader.nextToken();
        while (! Objects.equals(type, TextReader.EMPTY_STRING)) {
            String name = TextReader.nextToken();
            TextReader.nextTokenWithTest(FormatsConsts.OPEN_BRACE);
            String typeFiled = String.format("\"%s\": \"%s\"", FormatsConsts.MODEL_TYPE_FIELD, type);
            String nameFiled = String.format("\"%s\": \"%s\"", FormatsConsts.MODEL_NAME_FIELD, typeConvert(name));
            ENTITIES.add(String.format("{%s, %s%s}", typeFiled, nameFiled, ModelTextParser.parseFields()));
            type = TextReader.nextToken();
        }
        return String.format("[%s]", String.join(BaseParser.DELIMITER, ENTITIES));
    }

    /**
     * parsing all fields in one entity.
     */
    static String parseFields() {
        StringBuilder result = new StringBuilder();
        String fieldName = TextReader.nextToken();
        while (! Objects.equals(fieldName, FormatsConsts.CLOSE_BRACE)) {
            StringBuilder fieldType = new StringBuilder();
            String temp = TextReader.nextToken();
            while (! Objects.equals(temp, FormatsConsts.SEMICOLON)) {
                fieldType.append(typeConvert(temp));
                temp = TextReader.nextToken();
            }
            result.append(", \"").append(identifierConvert(fieldName))
                .append("\": \"").append(fieldType).append("\"");
            fieldName = TextReader.nextToken();
        }
        return result.toString();
    }

}
