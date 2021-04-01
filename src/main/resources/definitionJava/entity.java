import base.type.BaseEntity;
import base.type.primitive.*;
import base.type.collection.*;

public class entity extends BaseEntity {
    public BoolEntity isAll;
    public entity entirety;
    public StringEntity base;
    public SetEntity<StringEntity> modifier;

    @Override
    public String getType() { return "entity"; }

    @Override
    public boolean isPrimitive() { return false; }

    @Override
    public boolean isRequirement() { return false; }

    @Override
    public BoolEntity equal(BaseEntity entity) {
        if (! getType().equals(entity.getType())) {
            return BoolEntity.FALSE;
        }
        entity that = (entity) entity;
        return BoolEntity.valueOf(
                   isAll.equal(that.isAll).getValue() &&
                   entirety.equal(that.entirety).getValue() &&
                   base.equal(that.base).getValue() &&
                   modifier.equal(that.modifier).getValue()
        );
    }
}