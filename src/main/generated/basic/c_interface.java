package basic;

import types.BaseEntity;
import types.primitive.*;
import types.collection.*;

/**
 * Auto-generated Java file: c_interface.java
 *
 * @author Guo Weize
 * @date 2021/11/09 17:09:49
 */
public final class c_interface extends BaseEntity {
    public c_functional f_caller = null;
    public c_functional f_callee = null;

    @Override
    public String getType() {
        return "interface";
    }

    @Override
    public BoolEntity equal(BaseEntity entity) {
        if (! getType().equals(entity.getType())) {
            return BoolEntity.FALSE;
        }
        c_interface that = (c_interface)entity;
        return BoolEntity.and(
            f_caller.equal(that.f_caller),
            f_callee.equal(that.f_callee)
        );
    }
    public static c_interface newInstance() {
        return new c_interface();
    }

}
