package exceptions;

import java.io.IOException;

/**
 * @author Guo Weize
 * @date 2021/4/13
 */
public final class TokenInvalidException extends IOException {

    public TokenInvalidException(String reason) {
        super("Token gotten is invalid.");
        System.out.println(reason);
    }

    public TokenInvalidException(String real, String expectation) {
        super("Token is not matched.");
        System.out.printf("Token gotten is: \"%s\", but requires \"%s\".%n", real, expectation);
    }

    public TokenInvalidException(String real, Iterable<String> expectations) {
        super("Token is not matched");
        String exp = String.join("\" & \"", expectations);
        System.out.printf("Token gotten is: \"%s\", but requires \"%s\".%n", real, exp);
    }

}
