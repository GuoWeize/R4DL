package exceptions;

/**
 * @author Guo Weize
 * @date 2021/4/28
 */
public final class TypeInvalidException extends IllegalArgumentException {

    public TypeInvalidException(String reason) {
        super("Type is invalid.");
        System.out.println(reason);
    }

    public TypeInvalidException(String real, String expectation) {
        super("Type is not matched.");
        System.out.printf("Type gotten is :\"%s\", but requires %s.%n", real, expectation);
    }

    public TypeInvalidException(String real, Iterable<String> expectations) {
        super("Type is not matched");
        String exp = String.join("\" & \"", expectations);
        System.out.printf("Type gotten is :\"%s\", but requires %s.%n", real, exp);
    }

}
