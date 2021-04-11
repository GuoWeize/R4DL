package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * @author Guo Weize
 * @date 2021/2/25
 */
public final class TextReader {

    public static final String EMPTY_STRING = "";
    public static final String COMMA = ",";
    public static final String SEMICOLON = ";";
    public static final String EXCLAMATION = "!";
    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String STAR = "*";
    public static final String DIV = "/";
    public static final String LESS = "<";
    public static final String GREATER = ">";
    public static final String DOLLAR = "$";

    public static final String OPEN_BRACE = "{";
    public static final String CLOSE_BRACE = "}";
    public static final String OPEN_PAREN = "(";
    public static final String CLOSE_PAREN = ")";
    public static final String OPEN_BRACKET = "[";
    public static final String CLOSE_BRACKET = "]";

    public static final String EQUAL = "==";
    public static final String NOT_EQUAL = "!=";
    public static final String NOT_LESS = ">=";
    public static final String NOT_GREATER = "<=";
    public static final String ARROW = "->";
    private static final Set<String> MULTIPLE = new HashSet<>();
    static {
        MULTIPLE.add(EQUAL);
        MULTIPLE.add(NOT_EQUAL);
        MULTIPLE.add(NOT_LESS);
        MULTIPLE.add(NOT_GREATER);
        MULTIPLE.add(ARROW);
    }

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
    public static void readFile(String filePath) {
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
    public static String nextToken() {
        if (symbolChar != -1) {
            char c = ((char) symbolChar);
            symbolChar = -1;
            return Character.toString(c);
        }
        try {
            StringBuilder result = new StringBuilder();
            boolean multipleFlag = false;
            int c = file.read();
            while (c != -1) {
                // multiple characters
                int finalC = c;
                if (multipleFlag && result.length() == 1) {
                    String multipleChars = result.toString() + Character.toString(c);
                    if (MULTIPLE.contains(multipleChars)) {
                        return multipleChars;
                    }
                    else {
                        return result.toString();
                    }
                }
                if (MULTIPLE.stream().anyMatch(word -> word.charAt(0) == finalC)) {
                    result.append((char) c);
                    multipleFlag = true;
                    c = file.read();
                    continue;
                }

                // name and other symbols
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

    public static void nextTokenWithTest(String expectation) throws LanguageFormatException {
        String testString = nextToken();
        if (! testString.equals(expectation)) {
            throw new LanguageFormatException(testString, expectation);
        }
    }



    public static void main(String[] args) {
        String filePath = Configs.RULE_TEXT_FILE;
        readFile(filePath);
        String token = nextToken();
        while (! "".equals(token)) {
            System.out.print(token);
            System.out.print(" ");
            token = nextToken();
        }
    }

}
