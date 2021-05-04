package process.definition;

import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Guo Weize
 * @date 2021/2/22
 */
public class Demo {

    public static List<String> descartes_old(List<String>... lists) {
        List<String> tempList = new ArrayList<>();
        for (List<String> list : lists) {
            if (tempList.isEmpty()) {
                tempList = list;
            } else {
                tempList = tempList.stream()
                    .flatMap(item -> list.stream().map(item2 -> item + " " + item2))
                    .collect(Collectors.toList());
            }
        }
        return tempList;
    }




    public static void main(String[] args) {
        Set<Character> charList = Set.of('a', 'b', 'c');

        Set<List<Character>> set = Sets.cartesianProduct(charList, charList, charList);
        for (List<Character> characters : set) {
            System.out.println(characters);
        }
    }
}
