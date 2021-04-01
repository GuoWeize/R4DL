import base.type.BaseEntity;
import base.type.primitive.*;
import base.type.collection.*;

public class functional extends BaseEntity {
    public SetEntity<entity> output;
    public SetEntity<StringEntity> restriction;
    public SetEntity<entity> input;
    public entity agent;
    public SetEntity<condition> event;
    public operation operation;

    @Override
    public String getType() { return "functional"; }

    @Override
    public boolean isPrimitive() { return false; }

    @Override
    public boolean isRequirement() { return true; }

    @Override
    public BoolEntity equal(BaseEntity entity) {
        if (! getType().equals(entity.getType())) {
            return BoolEntity.FALSE;
        }
        functional that = (functional) entity;
        return BoolEntity.valueOf(
                   output.equal(that.output).getValue() &&
                   restriction.equal(that.restriction).getValue() &&
                   input.equal(that.input).getValue() &&
                   agent.equal(that.agent).getValue() &&
                   event.equal(that.event).getValue() &&
                   operation.equal(that.operation).getValue()
        );
    }
}