import base.type.BaseEntity;
import base.type.primitive.*;
import base.type.collection.*;
import java.util.stream.IntStream;

public class Rule {

    private static BoolEntity inout_include(Functional _1, Functional _2) {
        return BoolEntity.or(BoolEntity.and(_1.input.include(_2.input), _1.output.include(_2.output)), BoolEntity.and(_2.input.include(_1.input), _2.output.include(_1.output)));
    }

    private static BoolEntity inout_include(Functional _1, Condition _2) {
        return BoolEntity.or(BoolEntity.and(_1.input.include(_2.input), _1.output.include(_2.output)), BoolEntity.and(_2.input.include(_1.input), _2.output.include(_1.output)));
    }

    private static BoolEntity inout_include(Condition _1, Functional _2) {
        return BoolEntity.or(BoolEntity.and(_1.input.include(_2.input), _1.output.include(_2.output)), BoolEntity.and(_2.input.include(_1.input), _2.output.include(_1.output)));
    }

    private static BoolEntity inout_include(Condition _1, Condition _2) {
        return BoolEntity.or(BoolEntity.and(_1.input.include(_2.input), _1.output.include(_2.output)), BoolEntity.and(_2.input.include(_1.input), _2.output.include(_1.output)));
    }

    private static BoolEntity base_contradict(Functional _1, Functional _2) {
        return BoolEntity.and(_1.agent.equal(_2.agent), contradict(_1.operation, _2.operation), inout_include(_1, _2));
    }

    private static BoolEntity base_contradict(Functional _1, Condition _2) {
        return BoolEntity.and(_1.agent.equal(_2.agent), contradict(_1.operation, _2.operation), inout_include(_1, _2));
    }

    private static BoolEntity base_contradict(Condition _1, Functional _2) {
        return BoolEntity.and(_1.agent.equal(_2.agent), contradict(_1.operation, _2.operation), inout_include(_1, _2));
    }

    private static BoolEntity base_contradict(Condition _1, Condition _2) {
        return BoolEntity.and(_1.agent.equal(_2.agent), contradict(_1.operation, _2.operation), inout_include(_1, _2));
    }

    private static BoolEntity base_include(Functional _1, Functional _2) {
        return BoolEntity.and(_1.agent.equal(_2.agent), _1.operation.include(_2.operation), _1.input.include(_2.input), _1.output.include(_2.output), _1.restriction.include(_2.restriction));
    }

    private static BoolEntity base_include(Functional _1, Condition _2) {
        return BoolEntity.and(_1.agent.equal(_2.agent), _1.operation.include(_2.operation), _1.input.include(_2.input), _1.output.include(_2.output), _1.restriction.include(_2.restriction));
    }

    private static BoolEntity base_include(Condition _1, Functional _2) {
        return BoolEntity.and(_1.agent.equal(_2.agent), _1.operation.include(_2.operation), _1.input.include(_2.input), _1.output.include(_2.output), _1.restriction.include(_2.restriction));
    }

    private static BoolEntity base_include(Condition _1, Condition _2) {
        return BoolEntity.and(_1.agent.equal(_2.agent), _1.operation.include(_2.operation), _1.input.include(_2.input), _1.output.include(_2.output), _1.restriction.include(_2.restriction));
    }

    private static BoolEntity operation_event_condition(Functional _1, Functional _2) {
        return _2.event.allMatch(condition -> operation_include(_1.operation, condition));
    }

    private static BoolEntity input_output_condition(Functional _1, Functional _2) {
        return _2.input.anyMatch(inEntity -> _1.output.anyMatch(outEntity -> entity_include(outEntity, inEntity)));
    }

    private static BoolEntity operation_include(Operation _1, Operation _2) {
        return BoolEntity.or(_1.equal(_2), BoolEntity.and(_2.isAble, _1.reaction.equal(_2.reaction), not(_1.isAble)));
    }

    private static BoolEntity entity_include(Entity _1, Entity _2) {
        return BoolEntity.or(BoolEntity.and(_1.base.equal(_2.base), _1.modifier.include(_2.modifier)), _1.equal(_2.entirety));
    }

    private static BoolEntity operation_contradict(Operation _1, Operation _2) {
        return BoolEntity.and(_1.reaction.equal(_2.reaction), not(_1.isAble), not(_2.isAble), not(_1.isNot.equal(_2.isNot)));
    }

    public static BoolEntity operation_inconsistency(Functional _1, Functional _2) {
        return BoolEntity.and(_1.event.equal(_2.event), base_contradict(_1, _2));
    }

    public static BoolEntity restriction_inconsistency(Functional _1, Functional _2) {
        return BoolEntity.and(_1.event.equal(_2.event), _1.agent.equal(_2.agent), _1.operation.equal(_2.operation), inout_include(_1, _2), (! _1.restriction.equal(_2.restriction) ));
    }

    public static BoolEntity event_inconsistency(Functional _1) {
        return _1.event.allMatch(event1 -> _1.event.anyMatch(event2 -> base_contradict(event1, event2)));
    }

    public static BoolEntity operation_inclusion(Functional _1, Functional _2) {
        return BoolEntity.and(_1.event.equal(_2.event), base_include(_1, _2));
    }

    public static BoolEntity event_inclusion(Functional _1, Functional _2) {
        return BoolEntity.and(_1.event.include(_2.event), _1.agent.equal(_2.agent), _1.operation.equal(_2.operation), _1.input.include(_2.input), _1.output.include(_2.output), _1.restriction.equal(_2.restriction));
    }

    public static BoolEntity operation_event_interlock(Functional... _1) {
        return BoolEntity.valueOf(IntStream.range(0, Arrays.asList(_1).size()-1).allMatch(i -> { ListEntity<Functional> _list = new ListEntity<>("Functional", Arrays.asList(_1)); return (operation_event_condition(_list.get(i), _list.get(i+1))).getValue(); }));
    }

    public static BoolEntity input_output_interlock(Functional... _1) {
        return BoolEntity.valueOf(IntStream.range(0, Arrays.asList(_1).size()-1).allMatch(i -> { ListEntity<Functional> _list = new ListEntity<>("Functional", Arrays.asList(_1)); return (input_output_condition(_list.get(i), _list.get(i+1))).getValue(); }));
    }

}
