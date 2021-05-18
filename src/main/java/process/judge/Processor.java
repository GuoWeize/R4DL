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
import java.util.*;
import java.util.stream.Collectors;

/**
 * Recognize relationships between requirements.
 *
 * @author Guo Weize
 * @date 2021/2/19
 */
@Slf4j
public final class Processor {

    private static final int REVERSIBLE = 2;
    private static final Set<String> reversibleRules = new HashSet<>();
    private static final Map<String, String> RULE_2_LIST_ARG = new HashMap<>();
    private static final Map<Method, List<String>> METHOD_2_ARGUMENTS = new HashMap<>();
    private static final Set<String> BLACKLIST = Set.of(
        "wait", "equals", "toString", "hashCode", "getClass", "notify", "notifyAll"
    );
    private static final Map<String, List<List<BaseEntity>>> RELATIONSHIPS = new HashMap<>();

    public static void initialization() {
        RULE_2_LIST_ARG.clear();
        METHOD_2_ARGUMENTS.clear();
        reversibleRules.clear();
    }

    public static void addReversibleRule(String name) {
        reversibleRules.add(name);
    }

    public static void addListArg(String name, String argument) {
        RULE_2_LIST_ARG.putIfAbsent(name, argument);
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
                args = getListArgs(RULE_2_LIST_ARG.get(ruleName));
            } else if (reversibleRules.contains(entry.getKey().getName())) {
                args = getReversibleArgs(entry.getValue());
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
        List<Set<BaseEntity>> results = new ArrayList<>();
        for (String type: argsTypes) {
            results.add(EntityParser.ENTITIES.getOrDefault(type, new HashSet<>()));
        }
        return distinct(Sets.cartesianProduct(results));
    }

    private static Set<List<BaseEntity>> getReversibleArgs(List<String> argsTypes) {
        Set<List<BaseEntity>> results = new HashSet<>();
        if (argsTypes.size() != REVERSIBLE) {
            log.error(String.format("Can not parse reversible arguments of %s.", argsTypes.size()));
            throw new IllegalArgumentException();
        }
        if (! Objects.equals(argsTypes.get(0), argsTypes.get(1))) {
            log.error("Can not parse reversible arguments of different types.");
            throw new IllegalArgumentException();
        }
        List<BaseEntity> requirements = new ArrayList<>(EntityParser.ENTITIES.get(argsTypes.get(0)));
        int size = requirements.size();
        for (int i = 0; i < size - 1; i++) {
            BaseEntity req1 = requirements.get(i);
            for (int j = i + 1; j < size; j++) {
                BaseEntity req2 = requirements.get(i);
                results.add(List.of(req1, req2));
            }
        }
        return results;
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
