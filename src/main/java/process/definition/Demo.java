package process.definition;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Guo Weize
 * @date 2021/2/22
 */
public class Demo {

    public static void main(String[] args) {
        List<Integer> l = List.of(1, 2, 3, 4, 5, 6, 7);
        System.out.println(IntStream.range(0, 5).anyMatch(i -> l.get(i) == 3));
    }
}
