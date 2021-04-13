package util;

/**
 * @author Guo Weize
 * @date 2021/4/13
 */
public final class TokenInvalidException extends LanguageFormatException {
    public TokenInvalidException(String token, String reason) {
        super("Token read is invalid.");
        System.out.println("Token read is :\"" + token + "\", but " + reason);
    }
}
