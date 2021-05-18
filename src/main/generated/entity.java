import base.type.BaseEntity;
import base.type.primitive.*;
import base.type.collection.*;

/**
 * Auto-generated Java file: entity.java
 *
 * @author /Users/gwz/Desktop/Code/R4DL/src/main/resources/definitionFile/model.json
 * @date 2021/05/13 13:13:48
 */
public final class entity extends BaseEntity {
    public BoolEntity isAll;
    public entity entirety;
    public StringEntity base;
    public SetEntity<StringEntity> modifier;

    @Override
    public String getType() {
        return "entity";
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
        entity that = (entity)entity;
        return BoolEntity.and(
            BaseEntity.equal(isAll, that.isAll),
            BaseEntity.equal(entirety, that.entirety),
            BaseEntity.equal(base, that.base),
            BaseEntity.equal(modifier, that.modifier)
        );
    }
}
