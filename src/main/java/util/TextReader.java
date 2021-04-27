package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Guo Weize
 * @date 2021/2/25
 */
public final class TextReader {

    private static FileInputStream file;
    private static final int END_OF_FILE = -1;
    private static final int SPACE = 32;
    public static final String EMPTY_STRING = "";

    private static int charRead = END_OF_FILE;
    private static String previousRead = EMPTY_STRING;

    private static final Set<Integer> ENTITY_CHAR = Stream.of(
            '$', '.', '_', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
            'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'
    ).map(character -> (int)character )
            .collect(Collectors.toCollection(HashSet::new));
    private static final Set<Integer> SYMBOL_CHAR = Stream.of(
            '`', '!', '"', '#', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '/', ':', ';',
            '<', '=', '>', '?', '@', '[', '\\', ']', '^', '`', '{', '|', '}', '~'
    ).map(character -> (int)character )
            .collect(Collectors.toCollection(HashSet::new));

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
        if (charRead != -1) {
            char c = ((char) charRead);
            charRead = -1;
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
                    String multipleChars = result + Character.toString(c);
                    if (Formats.MULTIPLE_CHARS_SIGNAL.contains(multipleChars)) {
                        return multipleChars;
                    }
                    else {
                        return result.toString();
                    }
                }
                if (Formats.MULTIPLE_CHARS_SIGNAL.stream().anyMatch(word -> word.charAt(0) == finalC)) {
                    result.append((char) c);
                    multipleFlag = true;
                    c = file.read();
                    continue;
                }

                // name and other symbols
                if (SYMBOL_CHAR.contains(c)) {
                    if (result.length() > 0) {
                        charRead = c;
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
            throw new TokenNotExpectationException(testString, expectation);
        }
    }

    public static void main(String[] args) {
        readFile(Configs.RULE_TEXT_FILE);
        String token = nextToken();
        while (! EMPTY_STRING.equals(token)) {
            System.out.print(token);
            System.out.print(" ");
            token = nextToken();
        }
    }

}
