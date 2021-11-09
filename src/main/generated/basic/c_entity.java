package basic;

import types.BaseEntity;
import types.primitive.*;
import types.collection.*;

/**
 * Auto-generated Java file: c_entity.java
 *
 * @author Guo Weize
 * @date 2021/11/09 17:09:49
 */
public final class c_entity extends BaseEntity {
    public c_entity f_owner = null;
    public c_entity f_property = null;
    public BoolEntity f_isAll = BoolEntity.valueOf(false);
    public SetEntity<StringEntity> f_constraints = (SetEntity<StringEntity>) SetEntity.newInstance();
    public c_entity f_content = null;
    public StringEntity f_base = StringEntity.valueOf("");

    @Override
    public String getType() {
        return "entity";
    }

    @Override
    public BoolEntity equal(BaseEntity entity) {
        if (! getType().equals(entity.getType())) {
            return BoolEntity.FALSE;
        }
        c_entity that = (c_entity)entity;
        return BoolEntity.and(
            f_owner.equal(that.f_owner),
            f_property.equal(that.f_property),
            f_isAll.equal(that.f_isAll),
            f_constraints.equal(that.f_constraints),
            f_content.equal(that.f_content),
            f_base.equal(that.f_base)
        );
    }
    public static c_entity newInstance() {
        return new c_entity();
    }

}
