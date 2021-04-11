package util;

import java.io.IOException;

/**
 * @author Guo Weize
 * @date 2021/4/11
 */
public class LanguageFormatException extends IOException {
    public LanguageFormatException(String real, String expectation) {
        super("Expect \"" + expectation + "\", but get \"" + real + "\".");
    }
}
