package parser;

import exceptions.TokenInvalidException;
import lombok.extern.slf4j.Slf4j;
import util.PathConsts;
import util.TextReader;
import util.Token;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Guo Weize
 * @date 2021/4/3
 */
@Slf4j
public final class RuleTextParser extends BaseParser {

    public static void parsePackage(String packageName) {
        String modelFile = PathConsts.R4DL + packageName + PathConsts.separator + "rule.r4dl";
        TextReader.readFile(modelFile);
        try {
            addToJsonFile(packageName, parse(packageName));
        } catch (IOException e) {
            log.error("Can not read file: " + modelFile, e);
        }
    }

    /**
     * 规则定义 ::= { 自定义运算定义 } { 关系识别规则定义 } 关系识别规则定义
     * 自定义运算定义 ::= "function" 运算命名 "(" 参数列表 ")" ":" 类型标识 对象
     */
    private static String parse(String fileName) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"").append(fileName).append("\":[");
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
        sb.append("]}");
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
        else if (forward.isKeyword("for")) { // 量词运算
            sb.append(quantification());
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
        TextReader.rollback(forward);
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

    /**
     * 量词运算 ::= "for" 量词 变量 量词范围 "(" 对象 ")"
     * 量词 ::= "any" | "all"
     */
    private static StringBuilder quantification() throws TokenInvalidException {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        Token forward = TextReader.nextToken();
        if (!forward.isKeyword("any") && !forward.isKeyword("all")) {
            throw new TokenInvalidException(forward.value, List.of("any", "all"));
        }
        sb.append("\"").append(forward.value).append("\":[");
        forward = TextReader.nextTokenWithTest(Token.Type.identifier);
        sb.append("{\"%\":\"").append(forward.value).append("\"},");
        sb.append(range());
        sb.append(object());
        return sb.append("]}");
    }

    /**
     * 量词范围 ::= "in" 对象 | "from" 对象 "to" 对象
     */
    private static StringBuilder range() throws TokenInvalidException {
        StringBuilder sb = new StringBuilder();
        Token forward = TextReader.nextToken();
        if (forward.isKeyword("in")) {
            sb.append(object()).append(",");
        }
        else if (forward.isKeyword("from")) {
            sb.append(object()).append(",");
            forward = TextReader.nextToken();
            if (!forward.isKeyword("to")) {
                throw new TokenInvalidException(forward.value, "to");
            }
            sb.append(object()).append(",");
        }
        else {
            throw new TokenInvalidException(forward.value, List.of("in", "from"));
        }
        return sb;
    }

    private static void addToJsonFile(String packageName, String contents){
        String path = PathConsts.R4DL + packageName + PathConsts.separator + "rule.json";
        try {
            File file = new File(path);
            if (! file.createNewFile()) {
                log.warn(String.format("Replace \"%s\" before.", path));
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(contents);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            log.error("Can not parse r4dl file: " + path, e);
        }
        log.info("Finish parse r4dl file: " + path);
    }

    public static void main(String[] args) throws IOException {
        parsePackage("basic");
    }

}
