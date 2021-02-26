package base.parser;

import java.io.IOException;
import java.util.*;

/**
 * @author Guo Weize
 * @date 2021/2/25
 */
public class LanguageParser {

    private static final List<List<String>> TYPES = new ArrayList<>();
    private static final List<List<String>> REQUIREMENTS = new ArrayList<>();
    private static final Map<String, String> FIELDS = new HashMap<>();

    private static void initialization() {
        Util.readModelFile();
        String token = Util.nextToken();
        List<String> tokens = new ArrayList<>();
        boolean isType = false;
        while (token.length() > 0) {
            if ("type".equals(token)) {
                if (! tokens.isEmpty()) {
                    addTokens(tokens, isType);
                    tokens = new ArrayList<>();
                }
                isType = true;
            }
            else if ("requirement".equals(token)) {
                if (! tokens.isEmpty()) {
                    addTokens(tokens, isType);
                    tokens = new ArrayList<>();
                }
                isType = false;
            }
            else {
                tokens.add(token);
            }
            token = Util.nextToken();
        }
        addTokens(tokens, isType);
    }

    private static void addTokens(List<String> tokens, boolean isType) {
        if (isType) {
            TYPES.add(tokens);
        }
        else {
            REQUIREMENTS.add(tokens);
        }
    }

    public static void parseModelFile() {
        initialization();
        try {
            Generator.parseModelFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void parseTypes() throws IOException {
        Generator.parseType();
    }

    static void parseRequirements() throws IOException {
        Generator.parseRequirement();
    }

    static void parseEntities(boolean isType) throws IOException {
        for (List<String> entity: isType ? TYPES: REQUIREMENTS) {
            for (int i=2; i < entity.size(); i++) {


            }
            String name = entity.get(0);
            Generator.parseEntity(name);
        }
    }

    static void parseField() throws IOException {
        for (Map.Entry<String, String> entry: FIELDS.entrySet()) {
            Generator.parseField(entry.getKey(), entry.getValue());
        }
    }

    public static void main(String[] args) {
        Generator.initialization();
        parseModelFile();
    }

}
