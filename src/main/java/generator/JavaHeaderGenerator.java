package generator;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Guo Weize
 * @date 2021/4/30
 */
public final class JavaHeaderGenerator {

    private static final String BASE_ENTITY = "import types.BaseEntity;\n";
    private static final String PRIMITIVE = "import types.primitive.*;\n";
    private static final String COLLECTION = "import types.collection.*;\n";
    private static final String IMPORTS = BASE_ENTITY + PRIMITIVE + COLLECTION + "\n";

    private static String packageName = "";

    static void setPackageName(String name) {
        packageName = name;
    }

    /**
     * Generate Java file imports.
     */
    static String generateImports() {
        return "package " + packageName + ";\n\n" + IMPORTS;
    }

    /**
     * Generate Java file Javadoc.
     */
    static String generateJavadoc(String fileName) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return String.format(
            "/**\n * Auto-generated Java file: %s\n *\n * @author Guo Weize\n * @date %s\n */\n",
            fileName,
            formatter.format(date)
        );
    }
}
