package util;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Guo Weize
 * @date 2021/10/27
 */
public final class Token {

    private final static Set<String> keywords = Set.of(
        "type", "requirement", "boolean", "true", "false", "integer", "float", "string",
        "list", "set", "map", "rule", "function", "null", "size_of", "is_null",
        "and", "or", "in", "include", "inter", "union", "synonym", "antonym",
        "max", "min", "merge", "for", "all", "any", "from", "to", "substring", "find"
    );

    public enum Type {
        keyword, identifier, number, string, end,
        exclamation, equality, comma, quotes, add, sub, star, div, dollar, point, question, colon, semicolon,
        equal, notEqual, less, lessEqual, greater, greaterEqual,
        openParen, closeParen, openBracket, closeBracket, openBrace, closeBrace
    }

    public static final Token end = new Token(Type.end, "");

    public static final Token exclamation = new Token(Type.exclamation, "!");
    public static final Token equality = new Token(Type.equality, "=");
    public static final Token comma = new Token(Type.comma, ",");
    public static final Token quotes = new Token(Type.quotes, "\"");
    public static final Token add = new Token(Type.add, "+");
    public static final Token sub = new Token(Type.sub, "-");
    public static final Token star = new Token(Type.star, "*");
    public static final Token div = new Token(Type.div, "/");
    public static final Token dollar = new Token(Type.dollar, "$");
    public static final Token point = new Token(Type.point, ".");
    public static final Token question = new Token(Type.question, "?");
    public static final Token colon = new Token(Type.colon, ":");
    public static final Token semicolon = new Token(Type.semicolon, ";");
    public static final Token equal = new Token(Type.equal, "==");
    public static final Token notEqual = new Token(Type.notEqual, "!=");
    public static final Token less = new Token(Type.less, "<");
    public static final Token lessEqual = new Token(Type.lessEqual, "<=");
    public static final Token greater = new Token(Type.greater, ">");
    public static final Token greaterEqual = new Token(Type.greater, ">=");
    public static final Token openParen = new Token(Type.greater, "(");
    public static final Token closeParen = new Token(Type.greater, ")");
    public static final Token openBracket = new Token(Type.greater, "[");
    public static final Token closeBracket = new Token(Type.greater, "]");
    public static final Token openBrace = new Token(Type.greater, "{");
    public static final Token closeBrace = new Token(Type.greater, "}");

    private static final Map<Character, Token> mapping = Map.ofEntries(
        Map.entry(',', Token.comma),
        Map.entry('+', Token.add),
        Map.entry('-', Token.sub),
        Map.entry('*', Token.star),
        Map.entry('/', Token.div),
        Map.entry('$', Token.dollar),
        Map.entry('.', Token.point),
        Map.entry('?', Token.question),
        Map.entry(':', Token.colon),
        Map.entry(';', Token.semicolon),
        Map.entry('(', Token.openParen),
        Map.entry(')', Token.closeParen),
        Map.entry('[', Token.openBracket),
        Map.entry(']', Token.closeBracket),
        Map.entry('{', Token.openBrace),
        Map.entry('}', Token.closeBrace)
    );

    public Type type;
    public String value;

    private Token(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return this.type + this.value;
    }

     static Token word(String value) {
        if (keywords.contains(value)) {
            return new Token(Type.keyword, value);
        }
        else {
            return new Token(Type.identifier, value);
        }
    }

    static Token number(String value) {
        return new Token(Type.number, value);
    }

    static Token string(String value) {
        return new Token(Type.string, value);
    }

    static Token symbol(int ch) {
        return mapping.get((char) ch);
    }

    public boolean is(Token expectation) {
        return this.type == expectation.type && Objects.equals(this.value, expectation.value);
    }

    public boolean isKeyword(String word) {
        return this.type == Type.keyword && Objects.equals(this.value, word);
    }

    public boolean isIdentifier() {
        return this.type == Type.identifier;
    }

    public boolean isNumber() {
        return this.type == Type.number;
    }

    public boolean isString() {
        return this.type == Type.string;
    }
}
