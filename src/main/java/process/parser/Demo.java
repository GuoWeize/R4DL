package process.parser;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import util.Configs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Guo Weize
 * @date 2021/2/22
 */
public class Demo {
    public static void main(String[] args) throws IOException {
        RuleTextParser.parseRuleFile();
    }
}
