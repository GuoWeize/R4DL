import base.type.BaseEntity;
import base.type.primitive.*;
import base.type.collection.*;

public class Condition extends BaseEntity {
    public SetEntity<Entity> output;
    public SetEntity<StringEntity> restriction;
    public SetEntity<Entity> input;
    public Entity agent;
    public Operation operation;

    @Override
    public String getType() { return "Condition"; }

    @Override
    public boolean isPrimitive() { return false; }

    @Override
    public boolean isRequirement() { return false; }

    @Override
    public BoolEntity equal(BaseEntity entity) {
        if (! getType().equals(entity.getType())) {
            return new BoolEntity(false);
        }
        Condition that = (Condition) entity;
        return (output.equal(that.output).getValue() &&
               restriction.equal(that.restriction).getValue() &&
               input.equal(that.input).getValue() &&
               agent.equal(that.agent).getValue() &&
               operation.equal(that.operation).getValue())
               ? (new BoolEntity(true)) : (new BoolEntity(false));
    }
}