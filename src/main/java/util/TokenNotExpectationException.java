package util;

/**
 * @author Guo Weize
 * @date 2021/4/13
 */
public final class TokenNotExpectationException extends LanguageFormatException {
    public TokenNotExpectationException(String real, String expectation) {
        super("Token read do not satisfy expectation.");
        System.out.println("Expect \"" + expectation + "\", but get \"" + real + "\".");
    }

    public TokenNotExpectationException(String real, Iterable<String> expectations) {
        super("Token read do not satisfy expectations.");
        String exp = String.join("\" & \"", expectations);
        System.out.println("Expect \"" + exp + "\", but get \"" + real + "\".");
    }
}
