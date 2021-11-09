package basic;

import types.BaseEntity;
import types.primitive.*;
import types.collection.*;

/**
 * Auto-generated Java file: c_operation.java
 *
 * @author Guo Weize
 * @date 2021/11/09 17:09:49
 */
public final class c_operation extends BaseEntity {
    public StringEntity f_reaction = StringEntity.valueOf("");
    public BoolEntity f_isAble = BoolEntity.valueOf(false);
    public BoolEntity f_isNot = BoolEntity.valueOf(false);

    @Override
    public String getType() {
        return "operation";
    }

    @Override
    public BoolEntity equal(BaseEntity entity) {
        if (! getType().equals(entity.getType())) {
            return BoolEntity.FALSE;
        }
        c_operation that = (c_operation)entity;
        return BoolEntity.and(
            f_reaction.equal(that.f_reaction),
            f_isAble.equal(that.f_isAble),
            f_isNot.equal(that.f_isNot)
        );
    }
    public static c_operation newInstance() {
        return new c_operation();
    }

}
