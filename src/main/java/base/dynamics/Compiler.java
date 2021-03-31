package base.dynamics;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import util.Configs;

/**
 * @author Guo Weize
 * @date 2021/2/1
 */
public final class Compiler {
    public static URLClassLoader loader;
    static {
        try {
            loader = new URLClassLoader(new URL[]{new URL("file://" + Configs.DYNAMICS_CLASS_PATH)});
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void compile(List<String> classNames) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        classNames = classNames.stream().map( (name) -> Configs.DYNAMICS_JAVA_CODE_PATH + name + ".java" ).collect(Collectors.toList());
        classNames.add(0, Configs.DYNAMICS_CLASS_PATH);
        classNames.add(0, "-d");
        String[] arguments = classNames.toArray(new String[0]);
        int result = compiler.run(null, null, null, arguments);
        System.out.println(result == 0 ? "compile succeed": "compile failed");
    }

    public static Map<String, Class<?>> loadClass(List<String> classNames) {
        Map<String, Class<?>> result = new HashMap<>(classNames.size());
        for (String className: classNames) {
            try {
                result.put(className, loader.loadClass(className));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}