package exception;

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
        System.out.println("Type gotten is :\"" + real + "\", but requires " + expectation);
    }

}
