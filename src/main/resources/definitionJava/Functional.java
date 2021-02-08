import base.type.BaseEntity;
import base.type.primitive.*;
import base.type.collection.*;

public class Functional extends BaseEntity {
    public SetEntity<Entity> output;
    public SetEntity<StringEntity> restriction;
    public SetEntity<Entity> input;
    public Entity agent;
    public SetEntity<Condition> event;
    public Operation operation;

    @Override
    public String getType() { return "Functional"; }

    @Override
    public boolean isPrimitive() { return false; }

    @Override
    public boolean isRequirement() { return true; }

    @Override
    public BoolEntity equal(BaseEntity entity) {
        if (! getType().equals(entity.getType())) {
            return new BoolEntity(false);
        }
        Functional that = (Functional) entity;
        return (output.equal(that.output).getValue() &&
               restriction.equal(that.restriction).getValue() &&
               input.equal(that.input).getValue() &&
               agent.equal(that.agent).getValue() &&
               event.equal(that.event).getValue() &&
               operation.equal(that.operation).getValue())
               ? (new BoolEntity(true)) : (new BoolEntity(false));
    }
}