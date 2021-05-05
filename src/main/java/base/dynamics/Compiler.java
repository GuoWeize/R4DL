package base.dynamics;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
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

    private static Set<String> getAllJavaFiles() {
        Set<String> javaFileNames = new HashSet<>();
        File file = new File(PathConsts.DYNAMICS_JAVA_CODE_PATH);
        for (File f: Objects.requireNonNull(file.listFiles())) {
            String fileName = f.getName();
            if (f.isFile() && fileName.endsWith(".java")) {
                javaFileNames.add(fileName.substring(0, fileName.length() - 5));
            }
        }
        return javaFileNames;
    }

    public static void compileLoad() {
        ENTITY_CLASSES.clear();
        Set<String> allFiles = getAllJavaFiles();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<String> paras = allFiles.stream()
            .map( (name) -> PathConsts.DYNAMICS_JAVA_CODE_PATH + name + ".java")
            .collect(Collectors.toList());
        paras.add(0, PathConsts.DYNAMICS_CLASS_PATH);
        paras.add(0, "-d");
        String[] arguments = paras.toArray(new String[0]);
        int result = compiler.run(null, null, null, arguments);
        System.out.println(result == 0 ? "Compile succeed.": "Compile failed.");

        try {
            for (String className: allFiles) {
                if (Objects.equals(className, PathConsts.RULE_JAVA_CLASS)) {
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