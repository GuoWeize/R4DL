package parser;

import lombok.extern.slf4j.Slf4j;
import util.ModeEnum;
import util.PathConsts;
import util.TextReader;
import util.TypeEnum;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Guo Weize
 * @date 2021/5/30
 */
@Slf4j
public abstract class BaseParser {

    protected static final String DELIMITER = ", ";

    public static void run() {
        parseTextFile(ModeEnum.MODEL);
        parseTextFile(ModeEnum.RULE);
    }

    public static void parseTextFile(final ModeEnum mode) {
        String filePath = PathConsts.file(mode, TypeEnum.LANGUAGE);
        TextReader.readFile(filePath);
        try {
            String result = (mode == ModeEnum.MODEL) ? ModelTextParser.parse("basic"): RuleTextParser.parse("basic");
            String outputFilePath = PathConsts.file(mode, TypeEnum.JSON);
            Files.write(Paths.get(outputFilePath), result.getBytes(StandardCharsets.UTF_8));
        }
        catch (IOException e) {
            log.error("Can not read file: " + filePath, e);
        }
        log.info("Finish parse rule definition file: " + filePath);
    }

}
