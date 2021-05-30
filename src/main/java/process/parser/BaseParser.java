package process.parser;

import lombok.extern.slf4j.Slf4j;
import util.PathConsts;
import util.TextReader;

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
    private static final String TRUE = "true";
    private static final String FALSE = "false";
    protected static final String DELIMITER = ", ";

    public static void run() {
        parseTextFile(true);
        parseTextFile(false);
    }

    public static void parseTextFile(boolean isModel) {
        String filePath = isModel ? PathConsts.MODEL_TEXT_FILE: PathConsts.RULE_TEXT_FILE;
        TextReader.readFile(filePath);
        try {
            String result = isModel ? ModelTextParser.parse(): RuleTextParser.parse();
            String outputFilePath = isModel ? PathConsts.MODEL_JSON_FILE: PathConsts.RULE_JSON_FILE;
            Files.write(Paths.get(outputFilePath), result.getBytes(StandardCharsets.UTF_8));
        }
        catch (IOException e) {
            log.error("Can not write file: " + filePath, e);
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

    protected static String nameConvert(String name) {
        return "$" + name;
    }


}
