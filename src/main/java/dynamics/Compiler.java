package dynamics;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import util.PathConsts;

/**
 * Compiler of dynamic generated Java files, and save all classes.
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
@Slf4j
public final class Compiler {

    /** Customized entity name -> its class */
    private static final Map<String, Class<?>> ENTITIES_CLASSES = new HashMap<>(16);
    /** Class of rules */
    private static Class<?> ruleClass;

    /**
     * Main method of {@link Compiler}.
     */
    public static void run() {
        ENTITIES_CLASSES.clear();
        Set<String> allFiles = getAllJavaFiles();
        compile(allFiles);
        saveClasses(allFiles);
        Builder.initialization();
    }

    public static Map<String, Class<?>> getEntitiesClasses() {
        return ENTITIES_CLASSES;
    }

    public static Class<?> getRuleClass () {
        return ruleClass;
    }

    /**
     * Compile all Java files using {@link JavaCompiler}.
     * @param allFiles all filenames to compile.
     */
    private static void compile(Set<String> allFiles) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<String> paras = allFiles.stream()
            .map( (name) -> PathConsts.DYNAMICS_JAVA_CODE_DIR + name + ".java")
            .collect(Collectors.toList());
        paras.add(0, PathConsts.DYNAMICS_CLASS_DIR);
        paras.add(0, "-d");
        String[] arguments = paras.toArray(new String[0]);
        int result = compiler.run(null, null, null, arguments);
        log.info("Dynamics generated Java files compile " + (result == 0 ? "succeed.": "failed."));
    }

    /**
     * Save compiled classes to static fields: {@code ENTITY_CLASSES} and {@code ruleClass}.
     * @param allFiles all filenames compiled.
     */
    private static void saveClasses(Set<String> allFiles) {
        try {
            URL url = new URL("file://" + PathConsts.DYNAMICS_CLASS_DIR);
            URLClassLoader loader = new URLClassLoader(new URL[]{url});
            for (String className: allFiles) {
                if (Objects.equals(className, PathConsts.RULE_JAVA_CLASS)) {
                    ruleClass = loader.loadClass(className);
                } else {
                    ENTITIES_CLASSES.put(className, loader.loadClass(className));
                }
            }
        } catch (ClassNotFoundException | MalformedURLException e) {
            log.error("Can not load compiled classes.", e);
        }
    }

    /**
     * Get all dynamic generated Java files.
     * @return a set of filenames.
     */
    private static Set<String> getAllJavaFiles() {
        Set<String> javaFilenames = new HashSet<>();
        File file = new File(PathConsts.DYNAMICS_JAVA_CODE_DIR);
        for (File f: Objects.requireNonNull(file.listFiles())) {
            String fileName = f.getName();
            if (f.isFile() && fileName.endsWith(".java")) {
                javaFilenames.add(fileName.substring(0, fileName.length() - 5));
            }
        }
        log.info("All Java files are: " + javaFilenames);
        return javaFilenames;
    }

}