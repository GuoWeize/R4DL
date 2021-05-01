import base.type.BaseEntity;
import base.type.primitive.*;
import base.type.collection.*;

/**
 * Auto-generated Java file: condition.java
 *
 * @author XXX file
 * @date 20XX/X/XX
 */
public class condition extends BaseEntity {
    public SetEntity<entity> output;
    public SetEntity<StringEntity> restriction;
    public SetEntity<entity> input;
    public entity agent;
    public operation operation;

    @Override
    public String getType() {
        return "condition";
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public boolean isRequirement() {
        return false;
    }

    @Override
    public BoolEntity equal(BaseEntity entity) {
        if (! getType().equals(entity.getType())) {
            return BoolEntity.FALSE;
        }
        condition that = (condition)entity;
        return BoolEntity.and(
            agent.equal(that.agent),
            operation.equal(that.operation),
            input.equal(that.input),
            output.equal(that.output),
            restriction.equal(that.restriction)
        );
    }
}