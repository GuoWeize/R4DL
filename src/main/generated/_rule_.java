import base.type.BaseEntity;
import base.type.primitive.*;
import base.type.collection.*;
import java.util.stream.IntStream;

/**
 * Auto-generated Java file: _rule_.java
 *
 * @author /Users/gwz/Desktop/Code/R4DL/src/main/resources/definitionFile/rule.json
 * @date 2021/05/05 16:50:16
 */
public final class _rule_ {

    /**
     * in_out_include
     * @return BoolEntity
     */
    private static BoolEntity in_out_include(functional $1$, functional $2$) {
        return BoolEntity.or(BoolEntity.and($1$.input.include($2$.input), $1$.output.include($2$.output)), BoolEntity.and($2$.input.include($1$.input), $2$.output.include($1$.output)));
    }
    private static BoolEntity in_out_include(functional $1$, condition $2$) {
        return BoolEntity.or(BoolEntity.and($1$.input.include($2$.input), $1$.output.include($2$.output)), BoolEntity.and($2$.input.include($1$.input), $2$.output.include($1$.output)));
    }
    private static BoolEntity in_out_include(condition $1$, functional $2$) {
        return BoolEntity.or(BoolEntity.and($1$.input.include($2$.input), $1$.output.include($2$.output)), BoolEntity.and($2$.input.include($1$.input), $2$.output.include($1$.output)));
    }
    private static BoolEntity in_out_include(condition $1$, condition $2$) {
        return BoolEntity.or(BoolEntity.and($1$.input.include($2$.input), $1$.output.include($2$.output)), BoolEntity.and($2$.input.include($1$.input), $2$.output.include($1$.output)));
    }

    /**
     * operation_contradict
     * @return BoolEntity
     */
    private static BoolEntity operation_contradict(operation $1$, operation $2$) {
        return BoolEntity.and($1$.reaction.equal($2$.reaction), BoolEntity.not($1$.isAble), BoolEntity.not($2$.isAble), BoolEntity.not($1$.isNot.equal($2$.isNot)));
    }

    /**
     * base_contradict
     * @return BoolEntity
     */
    private static BoolEntity base_contradict(functional $1$, functional $2$) {
        return BoolEntity.and($1$.agent.equal($2$.agent), operation_contradict($1$.operation, $2$.operation), in_out_include($1$, $2$));
    }
    private static BoolEntity base_contradict(functional $1$, condition $2$) {
        return BoolEntity.and($1$.agent.equal($2$.agent), operation_contradict($1$.operation, $2$.operation), in_out_include($1$, $2$));
    }
    private static BoolEntity base_contradict(condition $1$, functional $2$) {
        return BoolEntity.and($1$.agent.equal($2$.agent), operation_contradict($1$.operation, $2$.operation), in_out_include($1$, $2$));
    }
    private static BoolEntity base_contradict(condition $1$, condition $2$) {
        return BoolEntity.and($1$.agent.equal($2$.agent), operation_contradict($1$.operation, $2$.operation), in_out_include($1$, $2$));
    }

    /**
     * operation_include
     * @return BoolEntity
     */
    private static BoolEntity operation_include(operation $1$, operation $2$) {
        return BoolEntity.or($1$.equal($2$), BoolEntity.and($1$.reaction.equal($2$.reaction), $2$.isAble, BoolEntity.not($1$.isAble)));
    }

    /**
     * base_include
     * @return BoolEntity
     */
    private static BoolEntity base_include(functional $1$, functional $2$) {
        return BoolEntity.and($1$.agent.equal($2$.agent), operation_include($1$.operation, $2$.operation), $1$.input.include($2$.input), $1$.output.include($2$.output), $1$.restriction.include($2$.restriction));
    }
    private static BoolEntity base_include(functional $1$, condition $2$) {
        return BoolEntity.and($1$.agent.equal($2$.agent), operation_include($1$.operation, $2$.operation), $1$.input.include($2$.input), $1$.output.include($2$.output), $1$.restriction.include($2$.restriction));
    }
    private static BoolEntity base_include(condition $1$, functional $2$) {
        return BoolEntity.and($1$.agent.equal($2$.agent), operation_include($1$.operation, $2$.operation), $1$.input.include($2$.input), $1$.output.include($2$.output), $1$.restriction.include($2$.restriction));
    }
    private static BoolEntity base_include(condition $1$, condition $2$) {
        return BoolEntity.and($1$.agent.equal($2$.agent), operation_include($1$.operation, $2$.operation), $1$.input.include($2$.input), $1$.output.include($2$.output), $1$.restriction.include($2$.restriction));
    }

    /**
     * operation_event_condition
     * @return BoolEntity
     */
    private static BoolEntity operation_event_condition(functional $1$, functional $2$) {
        return $2$.event.allMatch(condition -> base_include($1$, condition));
    }

    /**
     * entity_include
     * @return BoolEntity
     */
    private static BoolEntity entity_include(entity $1$, entity $2$) {
        return BoolEntity.or($1$.equal($2$.entirety), BoolEntity.and($1$.base.equal($2$.base), $2$.modifier.include($1$.modifier)));
    }

    /**
     * input_output_condition
     * @return BoolEntity
     */
    private static BoolEntity input_output_condition(functional $1$, functional $2$) {
        return $2$.input.anyMatch(inEntity -> $1$.output.anyMatch(outEntity -> entity_include(outEntity, inEntity)));
    }

    /**
     * operation_inconsistency
     * @return BoolEntity
     */
    public static BoolEntity operation_inconsistency(functional $1$, functional $2$) {
        return BoolEntity.and($1$.event.equal($2$.event), base_contradict($1$, $2$));
    }

    /**
     * restriction_inconsistency
     * @return BoolEntity
     */
    public static BoolEntity restriction_inconsistency(functional $1$, functional $2$) {
        return BoolEntity.and($1$.event.equal($2$.event), $1$.agent.equal($2$.agent), $1$.operation.equal($2$.operation), in_out_include($1$, $2$), BoolEntity.not($1$.restriction.equal($2$.restriction)));
    }

    /**
     * event_inconsistency
     * @return BoolEntity
     */
    public static BoolEntity event_inconsistency(functional $1$) {
        return $1$.event.allMatch(event1 -> $1$.event.anyMatch(event2 -> base_contradict(event1, event2)));
    }

    /**
     * operation_inclusion
     * @return BoolEntity
     */
    public static BoolEntity operation_inclusion(functional $1$, functional $2$) {
        return BoolEntity.and($1$.event.equal($2$.event), base_include($1$, $2$));
    }

    /**
     * event_inclusion
     * @return BoolEntity
     */
    public static BoolEntity event_inclusion(functional $1$, functional $2$) {
        return BoolEntity.and($1$.event.include($2$.event), $1$.agent.equal($2$.agent), $1$.operation.equal($2$.operation), $1$.input.include($2$.input), $1$.output.include($2$.output), $1$.restriction.equal($2$.restriction));
    }

    /**
     * operation_event_interlock
     * @return BoolEntity
     */
    public static BoolEntity operation_event_interlock(ListEntity<functional> $1$) {
        return BoolEntity.and(operation_event_condition($1$.get(BasePrimitiveEntity.calculate($1$.size(), IntEntity.valueOf(1), "-")), $1$.get(IntEntity.valueOf(0))), BoolEntity.valueOf(IntEntity.range(IntEntity.valueOf(0), BasePrimitiveEntity.calculate($1$.size(), IntEntity.valueOf(2), "-")).allMatch(index -> ((BoolEntity)operation_event_condition($1$.get(index), $1$.get(BasePrimitiveEntity.calculate(index, IntEntity.valueOf(1), "+")))).getValue())));
    }

    /**
     * input_output_interlock
     * @return BoolEntity
     */
    public static BoolEntity input_output_interlock(ListEntity<functional> $1$) {
        return BoolEntity.and(input_output_condition($1$.get(BasePrimitiveEntity.calculate($1$.size(), IntEntity.valueOf(1), "-")), $1$.get(IntEntity.valueOf(0))), BoolEntity.valueOf(IntEntity.range(IntEntity.valueOf(0), BasePrimitiveEntity.calculate($1$.size(), IntEntity.valueOf(2), "-")).allMatch(index -> ((BoolEntity)input_output_condition($1$.get(index), $1$.get(BasePrimitiveEntity.calculate(index, IntEntity.valueOf(1), "+")))).getValue())));
    }

}
