package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author Guo Weize
 * @date 2021/6/13
 */
public class ThesaurusReader {

    private static final Map<String, Set<String>> SYNONYMS = new HashMap<>(2048);
    private static final Map<String, Set<String>> ANTONYMS = new HashMap<>(2048);

    private static void readDictionary(String filePath, Map<String, Set<String>> dict) throws IOException {
        FileInputStream inputStream = new FileInputStream(filePath);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            String[] keyword2list = str.split(": ");
            String keyword = keyword2list[0];
            String[] words = keyword2list[1].split(", ");
            dict.put(keyword, Set.of(words));
        }
        inputStream.close();
        bufferedReader.close();
    }

    public static void initialization() {
        try {
            readDictionary(PathConsts.SYNONYM_FILE_NAME, SYNONYMS);
            readDictionary(PathConsts.ANTONYM_FILE_NAME, ANTONYMS);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isSynonym(String string1, String string2) {
        String str1 = string1.toLowerCase(Locale.US);
        String str2 = string2.toLowerCase(Locale.US);
        return SYNONYMS.getOrDefault(str1, new HashSet<>()).contains(str2)
            || SYNONYMS.getOrDefault(str2, new HashSet<>()).contains(str1);
    }

    public static boolean isAntonym(String string1, String string2) {
        String str1 = string1.toLowerCase(Locale.US);
        String str2 = string2.toLowerCase(Locale.US);
        return ANTONYMS.getOrDefault(str1, new HashSet<>()).contains(str2)
            || ANTONYMS.getOrDefault(str2, new HashSet<>()).contains(str1);
    }

    public static void main(String[] args) {
        initialization();
        System.out.println(isSynonym("create", "make"));
        System.out.println(isAntonym("create", "Destroy"));
    }

}
