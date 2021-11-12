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
 * @date 2021/2/25
 */
@Slf4j
public final class ModelTextParser extends BaseParser {

    public static void parsePackage(String packageName) {
        String modelFile = PathConsts.R4DL + packageName + PathConsts.separator + "model.r4dl";
        TextReader.readFile(modelFile);
        try {
            addToJsonFile(packageName, parse(packageName));
        } catch (IOException e) {
            log.error("Can not read file: " + modelFile, e);
        }
    }

    /**
     * 模型定义 ::= { "type" 类型定义体 } { "requirement" 类型定义体 } "requirement" 类型定义体
     * 类型定义体	::= 标识符 "{" 所有字段定义 "}"
     */
    private static String parse(String fileName) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"").append(fileName).append("\":[");
        List<StringBuilder> definitions = new ArrayList<>();
        while (true) {
            Token type = TextReader.nextToken();
            String isRequirement;
            if (type.isKeyword("type")) {
                isRequirement = "false";
            }
            else if (type.isKeyword("requirement")) {
                isRequirement = "true";
            }
            else {
                break;
            }
            StringBuilder definition = new StringBuilder();
            definition.append("{").append("\"?\":");
            definition.append(isRequirement);
            Token name = TextReader.nextTokenWithTest(Token.Type.identifier);
            definition.append(",\"#\":").append("\"").append(name.value).append("\"");
            TextReader.nextTokenWithTest(Token.openBrace);
            definition.append(fieldsDefinitions());
            TextReader.nextTokenWithTest(Token.closeBrace);
            definition.append("}");
            definitions.add(definition);
        }
        sb.append(String.join(",", definitions));
        sb.append("]}");
        return sb.toString();
    }

    /**
     * 所有字段定义 ::= 字段定义 { ";" 字段定义 }
     */
    private static StringBuilder fieldsDefinitions() throws TokenInvalidException {
        StringBuilder fields = new StringBuilder();
        while (true) {
            fields.append(", ").append(fieldDefinition());
            TextReader.nextTokenWithTest(Token.semicolon);
            Token forward = TextReader.nextToken();
            if (forward.is(Token.closeBrace)) {
                TextReader.rollback(forward);
                break;
            }
            else {
                TextReader.rollback(forward);
            }
        }
        return fields;
    }

    /**
     * 字段定义 ::= 标识符 空白 字段类型与初始化
     * 字段类型与初始化 ::= "boolean" [ "=" 布尔值 ] | "integer" [ "=" 整数 ] | "float" [ "=" 浮点数 ]
     *                    | "string" [ "=" 字符串 ] | 标识符
     */
    private static StringBuilder fieldDefinition() throws TokenInvalidException {
        StringBuilder field = new StringBuilder();
        Token name = TextReader.nextTokenWithTest(Token.Type.identifier);
        field.append("\"").append(name.value).append("\"").append(":");

        Token type = TextReader.nextToken();

        if (type.isKeyword("boolean")) {
            field.append(booleanField());
        }
        else if (type.isKeyword("integer")) {
            field.append(integerField());
        }
        else if (type.isKeyword("float")) {
            field.append(floatField());
        }
        else if (type.isKeyword("string")) {
            field.append(stringField());
        }
        else if (type.isKeyword("list") || type.isKeyword("set") || type.isKeyword("map")) {
            TextReader.rollback(type);
            field.append(collection());
        }
        else if (type.isIdentifier()) {
            field.append("\"").append(type.value).append("\"");
        }
        else {
            throw new TokenInvalidException(type.type, Token.Type.identifier);
        }
        return field;
    }

    private static StringBuilder booleanField() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"boolean\":");
        String defaultValue = "false";
        Token forward = TextReader.nextToken();
        if (forward.is(Token.equality)) {
            forward = TextReader.nextToken();
            if (forward.isKeyword("true")) {
                defaultValue = "true";
            }
        }
        else {
            TextReader.rollback(forward);
        }
        return sb.append(defaultValue).append("}");
    }

    private static StringBuilder integerField() throws TokenInvalidException {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"integer\":");
        String defaultValue = "0";
        Token forward = TextReader.nextToken();
        if (forward.is(Token.equality)) {
            forward = TextReader.nextTokenWithTest(Token.Type.number);
            defaultValue = forward.value;
        }
        else {
            TextReader.rollback(forward);
        }
        return sb.append(defaultValue).append("}");
    }

    private static StringBuilder floatField() throws TokenInvalidException {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"float\":");
        String defaultValue = "0.0";
        Token forward = TextReader.nextToken();
        if (forward.is(Token.equality)) {
            forward = TextReader.nextTokenWithTest(Token.Type.number);
            defaultValue = forward.value;
        }
        else {
            TextReader.rollback(forward);
        }
        return sb.append(defaultValue).append("}");
    }

    private static StringBuilder stringField() throws TokenInvalidException {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"string\":");
        String defaultValue = "";
        Token forward = TextReader.nextToken();
        if (forward.is(Token.equality)) {
            forward = TextReader.nextTokenWithTest(Token.Type.string);
            defaultValue = forward.value;
        }
        else {
            TextReader.rollback(forward);
        }
        return sb.append("\"").append(defaultValue).append("\"").append("}");
    }

    /**
     * 组合类型标识 ::= "list" "<" 类型标识 ">" | "set" "<" 类型标识 ">" | "map" "<" 类型标识 "," 类型标识 ">"
     */
    private static StringBuilder collection() throws TokenInvalidException {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        Token type = TextReader.nextTokenWithTest(Token.Type.keyword);
        if (type.isKeyword("list")) {
            TextReader.nextTokenWithTest(Token.less);
            sb.append("\"[]\":").append(typeName());
            TextReader.nextTokenWithTest(Token.greater);
        }
        else if (type.isKeyword("set")) {
            TextReader.nextTokenWithTest(Token.less);
            sb.append("\"()\":").append(typeName());
            TextReader.nextTokenWithTest(Token.greater);
        }
        else if (type.isKeyword("map")) {
            TextReader.nextTokenWithTest(Token.less);
            sb.append("\"{}\":{");
            sb.append("\"K\":").append(typeName()).append(",");
            TextReader.nextTokenWithTest(Token.comma);
            sb.append("\"V\":").append(typeName());
            sb.append("}");
            TextReader.nextTokenWithTest(Token.greater);
        }
        sb.append("}");
        return sb;
    }

    /**
     * 类型标识 ::= "boolean" | "integer" | "float" | "string" | 组合类型标识 | 标识符
     */
     static StringBuilder typeName() throws TokenInvalidException {
        StringBuilder sb = new StringBuilder();
        Token type = TextReader.nextToken();
        if (type.isKeyword("boolean") || type.isKeyword("integer") || type.isKeyword("float") || type.isKeyword("string")) {
            sb.append("\"").append(type.value).append("\"");
        }
        else if (type.isIdentifier()) {
            sb.append("\"").append(type.value).append("\"");
        }
        else if (type.isKeyword("list") || type.isKeyword("set") || type.isKeyword("map")) {
            TextReader.rollback(type);
            sb.append(collection());
        }
        else {
            throw new TokenInvalidException(type.type, Token.Type.identifier);
        }
        return sb;
    }

    private static void addToJsonFile(String packageName, String contents){
        String path = PathConsts.R4DL + packageName + PathConsts.separator + "model.json";
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
            log.error("Can not write file: " + path, e);
        }
    }

    public static void main(String[] args) throws IOException {
        parsePackage("basic");
    }

}
