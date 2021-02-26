package base.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Guo Weize
 * @date 2021/2/25
 */
public class Util {
    private static FileInputStream file;
    private static int symbolChar = -1;

    private static final Set<Integer> ENTITY_CHAR = new HashSet<>();
    static {
        for (int i=48; i<=57; i++) {
            ENTITY_CHAR.add(i);
        }
        for (int i=65; i<=90; i++) {
            ENTITY_CHAR.add(i);
        }
        for (int i=97; i<=122; i++) {
            ENTITY_CHAR.add(i);
        }
        ENTITY_CHAR.add(36);
        ENTITY_CHAR.add(46);
        ENTITY_CHAR.add(95);
    }
    private static final Set<Integer> SYMBOL_CHAR = new HashSet<>();
    static {
        for (int i=33; i<=45; i++) {
            SYMBOL_CHAR.add(i);
        }
        SYMBOL_CHAR.remove(36);
        for (int i=58; i<=64; i++) {
            SYMBOL_CHAR.add(i);
        }
        for (int i=91; i<=94; i++) {
            SYMBOL_CHAR.add(i);
        }
        for (int i=123; i<=126; i++) {
            SYMBOL_CHAR.add(i);
        }
        SYMBOL_CHAR.add(47);
    }

    private static final String PROJECT_PATH = System.getProperty("user.dir");
    private static final String DEFINITION_FILE_PATH = PROJECT_PATH + "/src/main/resources/definitionFile/";

    static void readModelFile() {
        try {
            file = new FileInputStream(DEFINITION_FILE_PATH + "model.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    static String nextToken() {
        if (symbolChar != -1) {
            char c = ((char) symbolChar);
            symbolChar = -1;
            return Character.toString(c);
        }
        try {
            StringBuilder result = new StringBuilder();
            int c = file.read();
            while (c != -1) {
                if (SYMBOL_CHAR.contains(c)) {
                    if (result.length() > 0) {
                        symbolChar = c;
                        return result.toString();
                    }
                    else {
                        return Character.toString((char) c);
                    }
                }
                else if (ENTITY_CHAR.contains(c)) {
                    result.append((char) c);
                }
                else {
                    if (result.length() > 0) {
                        return result.toString();
                    }
                }
                c = file.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        readModelFile();
        String token = nextToken();
        while (! "".equals(token)) {
            System.out.print(token);
            System.out.print(" ");
            token = nextToken();
        }
    }

}
