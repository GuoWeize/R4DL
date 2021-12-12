package api;

import dynamics.Builder;
import dynamics.Compiler;
import dynamics.Processor;
import generator.JavaClassGenerator;
import generator.JavaRuleGenerator;
import parser.ModelTextParser;
import parser.RuleTextParser;
import parser.TypeManager;
import serializator.EntityManager;

import java.util.List;

public class Demo {

    public static void main(String[] args) {
        String packageName = "basic";
        String datasetName = "PURE";

        Processor.initialization();
        Builder.initialization();
        TypeManager.initialization();

        ModelTextParser.parsePackage(packageName);
        RuleTextParser.parsePackage(packageName);

        JavaClassGenerator.generateClassFiles(packageName);
        JavaRuleGenerator.generateRuleFile(packageName);

        Compiler.loadPackage(packageName);

        EntityManager.readFiles(datasetName, List.of("entity", "operation", "requirement"));

        Processor.run();
    }

}
