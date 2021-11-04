package util;

import exceptions.TokenInvalidException;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public final class TextReader {

    public static Token rollback = null;

    private static FileInputStream file;
    private static final int END_OF_FILE = -1;
    public static final String EMPTY_STRING = "";

    private static int charRollBack = END_OF_FILE;
    private static int charRead = END_OF_FILE;

    private static final Set<Integer> NUMBER_CHAR = Stream.of(
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    ).map(character -> (int)character ).collect(Collectors.toCollection(HashSet::new));
    private static final Set<Integer> LETTER_CHAR = Stream.of( '_',
        'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
        'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'
    ).map(character -> (int)character ).collect(Collectors.toCollection(HashSet::new));
    private static final Set<Integer> SYMBOL_CHAR = Stream.of(
        '.', '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '/', ':', ';',
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
            charRollBack = END_OF_FILE;
        } catch (FileNotFoundException e) {
            log.error("file: " + filePath + " not exists!", e);
        }
    }

    public static void rollback(Token token) {
        rollback = token;
    }

    private static void rollBackChar() {
        charRollBack = charRead;
    }

    private static void readChar() {
        if (charRollBack != END_OF_FILE) {
            charRead = charRollBack;
            charRollBack = END_OF_FILE;
        }
        else {
            try {
                charRead = file.read();
            } catch (IOException e) {
                log.error("Can not read token from file.", e);
            }
        }
    }

    /**
     * read the next token in the file
     * @return the next token read
     */
    public static Token nextToken() {
        if (rollback != null) {
            Token temp = rollback;
            rollback = null;
            return temp;
        }
        while (true) {
            readChar();
            if (charRead == END_OF_FILE) {
                return Token.end;
            }
            if (NUMBER_CHAR.contains(charRead)) {
                return readNumber();
            }
            else if (LETTER_CHAR.contains(charRead)) {
                return readWord();
            }
            else if (charRead == '"') {
                return readString();
            }
            else if (SYMBOL_CHAR.contains(charRead)) {
                return readSymbol();
            }
        }
    }

    private static Token readNumber() {
        StringBuilder sb = new StringBuilder();
        sb.append((char)charRead);
        boolean hasPoint = false;
        while (true) {
            readChar();
            if (charRead == '.') {
                if (hasPoint) {
                    rollBackChar();
                    return Token.number(sb.toString());
                }
                sb.append((char)charRead);
                hasPoint = true;
            }
            else if (NUMBER_CHAR.contains(charRead)) {
                sb.append((char)charRead);
            }
            else {
                rollBackChar();
                return Token.number(sb.toString());
            }
        }

    }

    private static Token readWord() {
        StringBuilder sb = new StringBuilder();
        sb.append((char)charRead);
        while (true) {
            readChar();
            if (NUMBER_CHAR.contains(charRead) || LETTER_CHAR.contains(charRead)) {
                sb.append((char)charRead);
            }
            else {
                rollBackChar();
                return Token.word(sb.toString());
            }
        }

    }

    private static Token readString() {
        StringBuilder sb = new StringBuilder();
        while (true) {
            readChar();
            if (charRead == '"') {
                return Token.string(sb.toString());
            }
            else {
                sb.append((char)charRead);
            }
        }
    }

    private static Token readSymbol() {
        if (charRead == '!') {
            readChar();
            if (charRead == '=') {
                return Token.notEqual;
            }
            else {
                rollBackChar();
                return Token.exclamation;
            }
        }
        else if (charRead == '=') {
            readChar();
            if (charRead == '=') {
                return Token.equal;
            }
            else {
                rollBackChar();
                return Token.equality;
            }
        }
        else if (charRead == '<') {
            readChar();
            if (charRead == '=') {
                return Token.lessEqual;
            }
            else {
                rollBackChar();
                return Token.less;
            }
        }
        else if (charRead == '>') {
            readChar();
            if (charRead == '=') {
                return Token.greaterEqual;
            }
            else {
                rollBackChar();
                return Token.greater;
            }
        }
        return Token.symbol(charRead);
    }

    public static void nextTokenWithTest(Token expectation) throws TokenInvalidException {
        Token next = nextToken();
        if (! Objects.equals(next.value, expectation.value)) {
            throw new TokenInvalidException(next.value, expectation.value);
        }
    }

    public static Token nextTokenWithTest(Token.Type expectation) throws TokenInvalidException {
        Token next = nextToken();
        if (next.type != expectation) {
            throw new TokenInvalidException(next.type, expectation);
        }
        return next;
    }

    public static void main(String[] args) {
        TextReader.readFile("/Users/gwz/Desktop/Code/R4DL/src/main/resources/models/basic/model.r4dl");
        Token forward = nextToken();
        while (! forward.is(Token.end)) {
            System.out.print(forward.value + " ");
            forward = nextToken();
        }
    }

}
