package base.dynamics;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import util.PathConsts;

/**
 * @author Guo Weize
 * @date 2021/2/1
 */
public final class Compiler {
    public static final Map<String, Class<?>> ENTITY_CLASSES = new HashMap<>(16);
    public static Class<?> ruleClass;

    private static URLClassLoader loader;
    static {
        try {
            loader = new URLClassLoader(new URL[]{new URL("file://" + PathConsts.DYNAMICS_CLASS_PATH)});
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void compile(List<String> classNames) {
        ENTITY_CLASSES.clear();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<String> paras = classNames.stream()
            .map( (name) -> PathConsts.DYNAMICS_JAVA_CODE_PATH + name + ".java" )
            .collect(Collectors.toList());
        paras.add(0, PathConsts.DYNAMICS_CLASS_PATH);
        paras.add(0, "-d");
        String[] arguments = paras.toArray(new String[0]);
        int result = compiler.run(null, null, null, arguments);
        System.out.println(result == 0 ? "Compile succeed.": "Compile failed.");

        try {
            for (String className: classNames) {
                if (Objects.equals(className, "_rule_")) {
                    ruleClass = loader.loadClass(className);
                } else {
                    ENTITY_CLASSES.put(className, loader.loadClass(className));
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Builder.initialization();
    }

}