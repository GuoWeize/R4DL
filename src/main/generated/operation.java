import base.type.BaseEntity;
import base.type.primitive.*;
import base.type.collection.*;

/**
 * Auto-generated Java file: operation.java
 *
 * @author /Users/gwz/Desktop/Code/R4DL/src/main/resources/definitionFile/model.json
 * @date 2021/05/05 16:50:16
 */
public final class operation extends BaseEntity {
    public StringEntity reaction;
    public BoolEntity isAble;
    public BoolEntity isNot;

    @Override
    public String getType() {
        return "operation";
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
        operation that = (operation)entity;
        return BoolEntity.and(
            reaction.equal(that.reaction),
            isAble.equal(that.isAble),
            isNot.equal(that.isNot)
        );
    }
}