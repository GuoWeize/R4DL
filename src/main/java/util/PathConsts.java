package util;

import java.io.File;

/**
 * @author Guo Weize
 * @date 2021/3/27
 */
public final class PathConsts {

    public static final String separator = File.separator;

    // /Users/gwz/Desktop/Code/R4DL
    private static final String PROJECT_PATH = System.getProperty("user.dir");
    // /Users/gwz/Desktop/Code/R4DL/src/main/resources/r4dl/
    public static final String R4DL = PROJECT_PATH + separator + "src" + separator + "main" + separator
        + "resources" + separator + "r4dl" + separator;
    // /Users/gwz/Desktop/Code/R4DL/src/main/resources/entities/
    public static final String Entities = PROJECT_PATH + separator + "src" + separator + "main" + separator
        + "resources" + separator + "entities" + separator;
    // /Users/gwz/Desktop/Code/R4DL/src/main/generated/
    public static final String JavaCodes = PROJECT_PATH + separator + "src" + separator + "main" + separator
        + "generated" + separator;
    // /Users/gwz/Desktop/Code/R4DL/target/classes/
    public static final String JavaClasses = PROJECT_PATH + separator + "target" + separator + "classes" + separator;
    // /Users/gwz/Desktop/Code/R4DL/src/main/resources/thesaurus/
    static final String Thesaurus = PROJECT_PATH + separator + "src" + separator + "main" + separator
        + "resources" + separator + "thesaurus" + separator;

}
