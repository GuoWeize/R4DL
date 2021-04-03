package process.parser;

import util.Configs;
import util.FileReader;

import java.io.IOException;

/**
 * @author Guo Weize
 * @date 2021/4/3
 */
public final class RuleTextParser {

    /**
     * entry of the function of parsing the rule text file.
     */
    public static void parseRuleFile() {
        FileReader.readFile(Configs.RULE_TEXT_FILE);
        RuleJsonGenerator.initialization();
        try {
            RuleJsonGenerator.generateModel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void parseFunctions() {

    }

    static void parseRules() {

    }

}
