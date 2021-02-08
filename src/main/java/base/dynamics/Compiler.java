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

/**
 * @author Guo Weize
 */
public final class Compiler {
    public static final String PROJECT_PATH = System.getProperty("user.dir");
    public static final String JAVA_FILE_PATH = PROJECT_PATH + "/src/main/resources/definitionJava/";
    public static final String CLASS_FILE_PATH = PROJECT_PATH + "/src/main/resources/definitionClass/";
    public static URLClassLoader loader;
    static {
        try {
            loader = new URLClassLoader(new URL[]{new URL("file://" + CLASS_FILE_PATH)});
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void compile(List<String> classNames) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        classNames = classNames.stream().map( (name) -> JAVA_FILE_PATH + name + ".java" ).collect(Collectors.toList());
        classNames.add(0, CLASS_FILE_PATH);
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