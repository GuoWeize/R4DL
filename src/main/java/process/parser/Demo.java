package process.parser;

import process.definition.GeneralJavaHeaderGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Guo Weize
 * @date 2021/2/22
 */
public class Demo {
    public int a = 1;

    public static void main(String[] args) {
        List<Demo> l = List.of(new Demo());
        int q = l.get(0).a;
        System.out.println(q);
    }
}
