package languageParser;

import lombok.extern.slf4j.Slf4j;
import util.ModeEnum;
import util.PathConsts;
import util.TextReader;
import util.TypeEnum;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Set;

/**
 * @author Guo Weize
 * @date 2021/5/30
 */
@Slf4j
public abstract class BaseParser {

    private static final Set<Character> NUMBERS = Set.of(
        '.', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    );
    private static final Set<String> KEYWORDS = Set.of(
        "<", ">", ",", "boolean", "integer", "float", "string", "list", "set", "map"
    );
    private static final String TRUE = "true";
    private static final String FALSE = "false";
    protected static final String DELIMITER = ", ";

    public static void run() {
        parseTextFile(ModeEnum.MODEL);
        parseTextFile(ModeEnum.RULE);
    }

    public static void parseTextFile(ModeEnum mode) {
        String filePath = PathConsts.file(mode, TypeEnum.LANGUAGE);
        TextReader.readFile(filePath);
        try {
            String result = (mode == ModeEnum.MODEL) ? ModelTextParser.parse(): RuleTextParser.parse();
            String outputFilePath = PathConsts.file(mode, TypeEnum.JSON);
            Files.write(Paths.get(outputFilePath), result.getBytes(StandardCharsets.UTF_8));
        }
        catch (IOException e) {
            log.error("Can not read file: " + filePath, e);
        }
        log.info("Finish parse rule definition file: " + filePath);
    }

    protected static boolean isNumeric(String s) {
        for (char c: s.toCharArray()) {
            if (! NUMBERS.contains(c)) {
                return false;
            }
        }
        return true;
    }

    protected static boolean isBoolean(String s) {
        return Objects.equals(s, TRUE) || Objects.equals(s, FALSE);
    }

    protected static String typeConvert(String type) {
        return KEYWORDS.contains(type) ? type: "$" + type;
    }

    protected static String identifierConvert(String identifier) {
        identifier = identifier.replaceAll("\\$", "_RDS_CHAR_DOLLAR_");
        identifier = identifier.replaceAll("\\.", "._RDS_CHAR_DOLLAR__RDS_CHAR_DOLLAR_");
        identifier = identifier.replaceAll("_RDS_CHAR_DOLLAR_", "\\$");
        if (! identifier.startsWith("$")) {
            return "$$" + identifier;
        }
        return identifier;
    }

}
