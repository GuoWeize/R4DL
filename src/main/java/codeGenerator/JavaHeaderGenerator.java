package codeGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Guo Weize
 * @date 2021/4/30
 */
public final class JavaHeaderGenerator {

    private static final String BASE_ENTITY = "import basicTypes.BaseEntity;\n";
    private static final String PRIMITIVE = "import basicTypes.primitive.*;\n";
    private static final String COLLECTION = "import basicTypes.collection.*;\n";
    private static final String IMPORTS = BASE_ENTITY + PRIMITIVE + COLLECTION + "\n";

    /**
     * Generate Java file imports.
     */
    static String generateImports() {
        return IMPORTS;
    }

    /**
     * Generate Java file Javadoc.
     */
    static String generateJavadoc(String fileName, String referredFile) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return String.format(
            "/**\n * Auto-generated Java file: %s\n *\n * @author %s\n * @date %s\n */\n",
            fileName,
            referredFile,
            formatter.format(date)
        );
    }
}
