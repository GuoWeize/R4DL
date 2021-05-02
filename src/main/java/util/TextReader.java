package util;

import exceptions.TokenInvalidException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Guo Weize
 * @date 2021/2/25
 */
public final class TextReader {

    private static FileInputStream file;
    private static final int END_OF_FILE = -1;
    public static final String EMPTY_STRING = "";

    private static String rollBackToken = "";
    private static int charRead = END_OF_FILE;
    private static int previousRead = END_OF_FILE;
    private final static Set<Integer> MULTIPLE_CHARS_SIGNAL_HEAD =
        FormatsConsts.MULTIPLE_CHARS_SIGNAL.stream()
        .map(s -> (int) s.charAt(0))
        .collect(Collectors.toCollection(HashSet::new));

    private static final Set<Integer> ENTITY_CHAR = Stream.of(
        '$', '.', '_', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
        'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'
    ).map(character -> (int)character ).collect(Collectors.toCollection(HashSet::new));

    private static final Set<Integer> SYMBOL_CHAR = Stream.of(
        '`', '!', '"', '#', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '/', ':', ';',
        '<', '=', '>', '?', '@', '[', '\\', ']', '^', '`', '{', '|', '}', '~'
    ).map(character -> (int)character ).collect(Collectors.toCollection(HashSet::new));

    /**
     * read the specific file
     * @param filePath path of the file
     */
    public static void readFile(String filePath) {
        try {
            file = new FileInputStream(filePath);
            charRead = END_OF_FILE;
            previousRead = END_OF_FILE;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void rollBack(String token) {
        rollBackToken = token;
    }

    /**
     * read the next token in the file
     * @return the next token read
     */
    public static String nextToken() {
        try {
            if (! Objects.equals(rollBackToken, EMPTY_STRING)) {
                String token = rollBackToken;
                rollBackToken = EMPTY_STRING;
                return token;
            }
            String result;
            if (previousRead != END_OF_FILE) {
                result = readEntity(previousRead);
                if (! Objects.equals(result, EMPTY_STRING)) {
                    return result;
                }
                return readSymbol(charRead);
            }
            charRead = file.read();
            while (! SYMBOL_CHAR.contains(charRead) && ! ENTITY_CHAR.contains(charRead)) {
                charRead = file.read();
                if (charRead == END_OF_FILE) {
                    return EMPTY_STRING;
                }
            }
            result = readEntity(charRead);
            if (! Objects.equals(result, EMPTY_STRING)) {
                return result;
            }
            return readSymbol(charRead);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EMPTY_STRING;
    }

    private static String readEntity(int head) throws IOException {
        StringBuilder result = new StringBuilder();
        if (ENTITY_CHAR.contains(head)) {
            while (ENTITY_CHAR.contains(charRead)) {
                result.append((char) charRead);
                charRead = file.read();
            }
            if (SYMBOL_CHAR.contains(charRead)) {
                previousRead = charRead;
            }
            return result.toString();
        }
        return EMPTY_STRING;
    }

    private static String readSymbol(int head) throws IOException {
        if (MULTIPLE_CHARS_SIGNAL_HEAD.contains(head)) {
            charRead = file.read();
            String temp = Character.toString((char) head) + (char) charRead;
            if (FormatsConsts.MULTIPLE_CHARS_SIGNAL.contains(temp)) {
                previousRead = END_OF_FILE;
                return temp;
            } else {
                while (! SYMBOL_CHAR.contains(charRead) && ! ENTITY_CHAR.contains(charRead)) {
                    charRead = file.read();
                }
                previousRead = charRead;
                return Character.toString((char) head);
            }
        }
        previousRead = END_OF_FILE;
        return Character.toString((char) head);
    }

    public static void nextTokenWithTest(String expectation) throws TokenInvalidException {
        String testString = nextToken();
        if (! Objects.equals(testString, expectation)) {
            throw new TokenInvalidException(testString, expectation);
        }
    }

    public static void main(String[] args) {
        readFile(PathConsts.RULE_TEXT_FILE);
        String token = nextToken();
        while (! Objects.equals(token, EMPTY_STRING)) {
            System.out.print(token);
            System.out.print(" ");
            token = nextToken();
        }
    }

}
