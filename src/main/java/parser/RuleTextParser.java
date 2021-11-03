package parser;

import exceptions.TokenInvalidException;
import lombok.extern.slf4j.Slf4j;
import util.TextReader;
import util.Token;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Guo Weize
 * @date 2021/4/3
 */
@Slf4j
public final class RuleTextParser extends BaseParser {

    /**
     * 规则定义 ::= { 自定义运算定义 } { 关系识别规则定义 } 关系识别规则定义
     * 自定义运算定义 ::= "function" 运算命名 "(" 参数列表 ")" ":" 类型标识 对象
     */
    static String parse() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        List<StringBuilder> definitions = new ArrayList<>();
        while (true) {
            Token type = TextReader.nextToken();
            String isRule;
            if (type.isKeyword("function")) {
                isRule = "false";
            }
            else if (type.isKeyword("rule")) {
                isRule = "true";
            }
            else {
                break;
            }
            StringBuilder definition = new StringBuilder();
            definition.append("{").append("\"?\":");
            definition.append(isRule).append(",");
            Token name = TextReader.nextTokenWithTest(Token.Type.identifier);
            definition.append("\"#\":").append("\"").append(name.value).append("\",");

            TextReader.nextTokenWithTest(Token.openParen);
            definition.append("\"$\":[").append(parameters()).append("],");
            TextReader.nextTokenWithTest(Token.closeParen);

            definition.append("\"*\":").append(ModelTextParser.typeName()).append(",");
            TextReader.nextTokenWithTest(Token.colon);
            definition.append("\"~\":").append(object());

            definition.append("}");
            definitions.add(definition);
        }
        sb.append(String.join(",", definitions));
        sb.append("]");
        return sb.toString();
    }

    private static StringBuilder parameters() throws TokenInvalidException {
        StringBuilder sb = new StringBuilder();
        Token forward = TextReader.nextToken();
        if (forward.is(Token.closeParen)) {
            TextReader.rollback(forward);
            return sb;
        }
        TextReader.rollback(forward);
        while (true) {
            sb.append(ModelTextParser.typeName());
            forward = TextReader.nextToken();
            if (forward.is(Token.closeParen)) {
                TextReader.rollback(forward);
                break;
            }
            else if (forward.is(Token.comma)) {
                sb.append(",");
            }
            else {
                throw new TokenInvalidException(forward.type, Token.Type.comma);
            }
        }
        return sb;
    }

    /**
     * 对象 ::= "null" | 字面值 | 自定义类型对象 | 运算调用 | 参数 | 变量 | 对象的字段 | 数组对象取值 | 映射对象取值
     * 字面值 ::= 布尔值 | 整数 | 浮点数 | 字符串
     */
    private static StringBuilder object() throws TokenInvalidException {
        StringBuilder sb = new StringBuilder();
        Token forward = TextReader.nextToken();
        if (forward.isKeyword("null")) { // "null"
            sb.append("null");
        }
        else if (forward.isKeyword("true") || forward.isKeyword("false")) { // 布尔值
            sb.append(forward.value);
        }
        else if (forward.isNumber()) { // 整数 | 浮点数
            sb.append(forward.value);
        }
        else if (forward.isString()) { // 字符串
            sb.append("\"").append(forward.value).append("\"");
        }
        else if (forward.is(Token.dollar)) { // 参数 ::= "$" 参数位置序号 "$"
            forward = TextReader.nextTokenWithTest(Token.Type.number);
            sb.append("{\"$\":").append(forward.value).append("}");
            TextReader.nextTokenWithTest(Token.dollar);
        }
        else if (forward.is(Token.openParen)) { // ( 对象的字段 数组对象取值 映射对象取值
            TextReader.rollback(forward);
            sb.append(parenObject());
        }
        else if (forward.is(Token.openBrace)) { // { 自定义类型对象
            TextReader.rollback(forward);
            sb.append(braceObject());
        }
        else { // 变量 ? 运算调用 ?
            TextReader.rollback(forward);
            sb.append(identifierOrFunction());
        }
        return sb;
    }

    /**
     * 自定义类型对象 ::= "{" 标识符 标识符 ":" 字段的值 { ";" 标识符 ":" 字段的值 } "}"
     */
    private static StringBuilder braceObject() throws TokenInvalidException {
        StringBuilder sb = new StringBuilder();
        TextReader.nextTokenWithTest(Token.openBrace);
        sb.append("{");
        Token typeName = TextReader.nextTokenWithTest(Token.Type.identifier);
        sb.append("\"*\": \"").append(typeName).append("\",");
        while (true) {
            Token fieldName = TextReader.nextTokenWithTest(Token.Type.identifier);
            sb.append("\"").append(fieldName).append("\":");
            TextReader.nextTokenWithTest(Token.colon);
            sb.append(object());
            Token forward = TextReader.nextToken();
            if (forward.is(Token.closeBrace)) {
                break;
            }
            else if (forward.is(Token.semicolon)) {
                sb.append(",");
            }
            else {
                throw new TokenInvalidException(forward.type, Token.Type.semicolon);
            }
        }
        sb.append("}");
        return sb;
    }

    /**
     * 对象的字段 ::= "(" 对象 "." 字段命名 ")"
     * 数组对象取值 ::= "(" 对象 "[" 对象的索引 "]" ")"
     * 映射对象取值 ::= "(" 对象 "[" 键对象    "]" ")"
     */
    private static StringBuilder parenObject() throws TokenInvalidException {
        StringBuilder sb = new StringBuilder();
        TextReader.nextTokenWithTest(Token.openParen);
        StringBuilder object = object();
        Token forward = TextReader.nextToken();
        if (forward.is(Token.point)) {
            forward = TextReader.nextTokenWithTest(Token.Type.identifier);
            sb.append("{\"&\":").append(object).append(",\".\":\"").append(forward).append("\"}");
            TextReader.nextTokenWithTest(Token.closeParen);
        }
        else if (forward.is(Token.openBracket)) {
            StringBuilder index = object();
            TextReader.nextTokenWithTest(Token.closeBracket);
            sb.append("{\"&\":").append(object).append(",\"@\":").append(index).append("}");
            TextReader.nextTokenWithTest(Token.closeParen);
        }
        return sb;
    }

    /**
     * 变量 ::= 标识符
     * 运算调用？
     */
    private static StringBuilder identifierOrFunction() throws TokenInvalidException {
        StringBuilder sb = new StringBuilder();
        Token name = TextReader.nextToken();
        if (! name.isIdentifier()) { // 运算调用
            sb.append(functionCall(name));
            return sb;
        }
        Token forward = TextReader.nextToken();
        if (forward.is(Token.openParen)) { // 运算调用
            TextReader.rollback(forward);
            sb.append(functionCall(name));
            return sb;
        }
        TextReader.rollback(forward);
        sb.append("{\"%\":\"").append(name).append("\"}");
        return sb;
    }

    /**
     * 一元运算 ::= 一元运算符 "(" 对象 ")"
     * 二元运算 ::= 二元运算符 "(" 对象 "," 对象 ")"
     * 多元运算 ::= 多元运算符 "(" 对象 { "," 对象 } ")"
     */
    private static StringBuilder functionCall(Token name) throws TokenInvalidException {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"").append(name.value).append("\":[");
        TextReader.nextTokenWithTest(Token.openParen);
        Token forward = TextReader.nextToken();
        if (forward.is(Token.closeParen)) {
            return sb.append("]}");
        }
        while (true) {
            sb.append(object());
            forward = TextReader.nextToken();
            if (forward.is(Token.closeParen)) {
                break;
            }
            else if (forward.is(Token.comma)) {
                sb.append(",");
            }
            else {
                throw new TokenInvalidException(forward.type, Token.Type.comma);
            }
        }
        return sb.append("]}");
    }

}
