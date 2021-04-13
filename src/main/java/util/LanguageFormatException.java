package util;

import java.io.IOException;

/**
 * @author Guo Weize
 * @date 2021/4/11
 */
public abstract class LanguageFormatException extends IOException {
    public LanguageFormatException() {
        super();
    }

    public LanguageFormatException(String message) {
        super(message);
    }
}
