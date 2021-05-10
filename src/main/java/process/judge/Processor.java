package process.judge;

import base.dynamics.Compiler;
import base.type.BaseEntity;
import base.type.collection.ListEntity;
import base.type.primitive.BoolEntity;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import process.requirement.EntityParser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Recognize relationships between requirements.
 *
 * @author Guo Weize
 * @date 2021/2/19
 */
@Slf4j
public final class Processor {
    private static final Map<String, String> RULE_2_LISTARG = new HashMap<>();
    private static final Map<Method, List<String>> METHOD_2_ARGUMENTS = new HashMap<>();
    private static final Set<String> BLACKLIST = Set.of(
        "wait", "equals", "toString", "hashCode", "getClass", "notify", "notifyAll"
    );
    private static final Map<String, List<List<BaseEntity>>> RELATIONSHIPS = new HashMap<>();

    public static void initialization() {
        RULE_2_LISTARG.clear();
        METHOD_2_ARGUMENTS.clear();
    }

    public static void addListArg(String name, String argument) {
        RULE_2_LISTARG.putIfAbsent(name, argument);
    }

    public static void run() {
        initialArgs();
        judgeRules();
        outputResult();
    }

    private static void initialArgs() {
        for (Method rule: Compiler.getRuleClass().getMethods()) {
            if (! BLACKLIST.contains(rule.getName())) {
                List<String> args = Arrays.stream(rule.getParameterTypes())
                    .map(Class::getName)
                    .collect(Collectors.toList());
                METHOD_2_ARGUMENTS.put(rule, args);
            }
        }
    }

    private static void judgeRules() {
        log.info("Judgement started.");
        for (var entry: METHOD_2_ARGUMENTS.entrySet()) {
            Set<List<BaseEntity>> args;
            if (entry.getValue().contains("base.type.collection.ListEntity")) {
                String ruleName = entry.getKey().getName();
                args = getListArgs(RULE_2_LISTARG.get(ruleName));
            } else {
                args = getArgs(entry.getValue());
            }
            judgeRule(entry.getKey(), args);
        }
        log.info("Judgement finished.");
    }

    private static void outputResult() {
        int types = RELATIONSHIPS.size();
        int num = 0;
        for (var entry: RELATIONSHIPS.entrySet()) {
            num += entry.getValue().size();
        }
        log.warn(String.format("%s relationships in %s types are detected.", num, types));
        for (var entry: RELATIONSHIPS.entrySet()) {
            String relationships = entry.getValue().stream()
                .map(Processor::reqs2ID)
                .collect(Collectors.joining(", "));
            log.warn(String.format("%s: {%s}", entry.getKey(), relationships));
        }
    }

    private static String reqs2ID(List<BaseEntity> reqs) {
        return "["
            + reqs.stream()
            .map(req -> req.getType() + "@" + EntityParser.getID(req))
            .collect(Collectors.joining(", "))
            + "]";
    }

    private static Set<List<BaseEntity>> distinct(Set<List<BaseEntity>> arguments) {
        Set<List<BaseEntity>> result = new HashSet<>();
        for (List<BaseEntity> args: arguments) {
            Set<BaseEntity> set = new HashSet<>(args);
            if (set.size() != args.size()) {
                continue;
            }
            result.add(args);
        }
        return result;
    }

    private static Set<List<BaseEntity>> getListArgs(String argType) {
        Set<BaseEntity> requirements = EntityParser.ENTITIES.get(argType);
        List<Set<BaseEntity>> reqs = new ArrayList<>();
        Set<List<BaseEntity>> result = new HashSet<>();
        for (int i = 0; i < requirements.size(); i++) {
            reqs.add(requirements);
            Set<List<BaseEntity>> distinctive = distinct(Sets.cartesianProduct(reqs));
            result.addAll(distinctive);
        }
        return result;
    }

    private static Set<List<BaseEntity>> getArgs(List<String> argsTypes) {
        List<Set<BaseEntity>> requirements = new ArrayList<>();
        for (String type: argsTypes) {
            requirements.add(EntityParser.ENTITIES.get(type));
        }
        return distinct(Sets.cartesianProduct(requirements));
    }

    private static void judgeRule(Method rule, Set<List<BaseEntity>> requirements) {
        String relationshipName = rule.getName();
        try {
            for (List<BaseEntity> reqArgs: requirements) {
                Object result;
                if (rule.getParameterTypes()[0].getName().contains("base.type.collection.ListEntity")) {
                    result = rule.invoke(null, new ListEntity<>(reqArgs.get(0).getType(), reqArgs));
                } else {
                    result = rule.invoke(null, reqArgs.toArray());
                }
                if (((BoolEntity)result).getValue()) {
                    if (! RELATIONSHIPS.containsKey(relationshipName)) {
                        RELATIONSHIPS.put(relationshipName, new ArrayList<>());
                    }
                    RELATIONSHIPS.get(relationshipName).add(reqArgs);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("Can not process judgement.", e);
        }
    }

}
