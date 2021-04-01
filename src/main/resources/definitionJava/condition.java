import base.type.BaseEntity;
import base.type.primitive.*;
import base.type.collection.*;

public class condition extends BaseEntity {
    public SetEntity<entity> output;
    public SetEntity<StringEntity> restriction;
    public SetEntity<entity> input;
    public entity agent;
    public operation operation;

    @Override
    public String getType() { return "condition"; }

    @Override
    public boolean isPrimitive() { return false; }

    @Override
    public boolean isRequirement() { return false; }

    @Override
    public BoolEntity equal(BaseEntity entity) {
        if (! getType().equals(entity.getType())) {
            return BoolEntity.FALSE;
        }
        condition that = (condition) entity;
        return BoolEntity.valueOf(
                   output.equal(that.output).getValue() &&
                   restriction.equal(that.restriction).getValue() &&
                   input.equal(that.input).getValue() &&
                   agent.equal(that.agent).getValue() &&
                   operation.equal(that.operation).getValue()
        );
    }
}