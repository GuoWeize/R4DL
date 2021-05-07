import base.type.BaseEntity;
import base.type.primitive.*;
import base.type.collection.*;

/**
 * Auto-generated Java file: condition.java
 *
 * @author /Users/gwz/Desktop/Code/R4DL/src/main/resources/definitionFile/model.json
 * @date 2021/05/07 11:11:23
 */
public final class condition extends BaseEntity {
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
            BaseEntity.equal(output, that.output),
            BaseEntity.equal(restriction, that.restriction),
            BaseEntity.equal(input, that.input),
            BaseEntity.equal(agent, that.agent),
            BaseEntity.equal(operation, that.operation)
        );
    }
}