package base.parser;

import util.Configs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Guo Weize
 * @date 2021/2/25
 */
public final class FileReader {

    public static final String EMPTY_STRING = "";
    public static final String OPEN_BRACE = "{";
    public static final String CLOSE_BRACE = "}";
    public static final String SEMICOLON = ";";

    private static FileInputStream file;
    private static int symbolChar = -1;

    private static final Set<Integer> ENTITY_CHAR = new HashSet<>();
    private static final Set<Integer> SYMBOL_CHAR = new HashSet<>();
    static {
        for (int i = '!'; i <= '~'; i++) {
            if (i == '$' || i == '.') {
                ENTITY_CHAR.add(i);
            }
            else if (i == '`') {
                SYMBOL_CHAR.add(i);
            }
            else if (i <= '/') {
                SYMBOL_CHAR.add(i);
            }
            else if (i <= '9') {
                ENTITY_CHAR.add(i);
            }
            else if (i <= '@') {
                SYMBOL_CHAR.add(i);
            }
            else if (i <= 'Z') {
                ENTITY_CHAR.add(i);
            }
            else if (i <= '^') {
                SYMBOL_CHAR.add(i);
            }
            else if (i <= 'z') {
                ENTITY_CHAR.add(i);
            }
            else { // '{' to '~'
                SYMBOL_CHAR.add(i);
            }
        }
    }

    /**
     * read the specific file
     * @param filePath path of the file
     */
    static void readFile(String filePath) {
        try {
            file = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * read the next token in the file
     * @return the next token read
     */
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
        return EMPTY_STRING;
    }

    public static void main(String[] args) {
        String filePath = Configs.MODEL_TEXT_FILE;
        readFile(filePath);
        String token = nextToken();
        while (! "".equals(token)) {
            System.out.print(token);
            System.out.print(" ");
            token = nextToken();
        }
    }

}
