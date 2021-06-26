package codeGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Guo Weize
 * @date 2021/4/30
 */
public final class GeneralJavaHeaderGenerator {

    /**
     * Generate Java file imports.
     */
    static String generateImports() {
        return "import basicTypes.BaseEntity;\nimport basicTypes.primitive.*;\nimport basicTypes.collection.*;\n\n";
    }

    /**
     * Generate Java file Javadoc.
     */
    static String generateJavadoc(String fileName, String referredFile) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return String.format("/**\n * Auto-generated Java file: %s\n *\n * @author %s\n * @date %s\n */\n",
            fileName, referredFile, formatter.format(date));
    }

}
