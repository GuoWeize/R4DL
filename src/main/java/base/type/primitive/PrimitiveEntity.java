package base.type.primitive;

import base.type.BaseEntity;

/**
 * @author gwz
 */
public abstract class PrimitiveEntity extends BaseEntity {

    @Override
    public final boolean isPrimitive() {
        return true;
    }

    @Override
    public final boolean isRequirement() {
        return false;
    }

}
