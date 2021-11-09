package basic;

import types.BaseEntity;
import types.primitive.*;
import types.collection.*;

/**
 * Auto-generated Java file: c_condition.java
 *
 * @author Guo Weize
 * @date 2021/11/09 17:09:49
 */
public final class c_condition extends BaseEntity {
    public SetEntity<c_entity> f_output = (SetEntity<c_entity>) SetEntity.newInstance();
    public SetEntity<StringEntity> f_restriction = (SetEntity<StringEntity>) SetEntity.newInstance();
    public SetEntity<c_entity> f_input = (SetEntity<c_entity>) SetEntity.newInstance();
    public c_entity f_agent = null;
    public c_operation f_operation = null;

    @Override
    public String getType() {
        return "condition";
    }

    @Override
    public BoolEntity equal(BaseEntity entity) {
        if (! getType().equals(entity.getType())) {
            return BoolEntity.FALSE;
        }
        c_condition that = (c_condition)entity;
        return BoolEntity.and(
            f_output.equal(that.f_output),
            f_restriction.equal(that.f_restriction),
            f_input.equal(that.f_input),
            f_agent.equal(that.f_agent),
            f_operation.equal(that.f_operation)
        );
    }
    public static c_condition newInstance() {
        return new c_condition();
    }

}
