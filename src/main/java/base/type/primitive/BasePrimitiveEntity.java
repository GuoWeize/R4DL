package base.type.primitive;

import base.type.BaseEntity;

/**
 * Abstract base class for all collection types, including boolean, integer, float and string.<p>
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
public abstract class BasePrimitiveEntity extends BaseEntity {

    @Override
    public final boolean isPrimitive() {
        return true;
    }

    @Override
    public final boolean isRequirement() {
        return false;
    }

}
