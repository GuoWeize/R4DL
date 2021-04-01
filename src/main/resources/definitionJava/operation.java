import base.type.BaseEntity;
import base.type.primitive.*;
import base.type.collection.*;

public class operation extends BaseEntity {
    public StringEntity reaction;
    public BoolEntity isAble;
    public BoolEntity isNot;

    @Override
    public String getType() { return "operation"; }

    @Override
    public boolean isPrimitive() { return false; }

    @Override
    public boolean isRequirement() { return false; }

    @Override
    public BoolEntity equal(BaseEntity entity) {
        if (! getType().equals(entity.getType())) {
            return BoolEntity.FALSE;
        }
        operation that = (operation) entity;
        return BoolEntity.valueOf(
                   reaction.equal(that.reaction).getValue() &&
                   isAble.equal(that.isAble).getValue() &&
                   isNot.equal(that.isNot).getValue()
        );
    }
}