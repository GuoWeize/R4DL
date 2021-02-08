package base.type.collection;

import base.type.BaseEntity;
import base.type.primitive.BoolEntity;
import base.type.primitive.IntEntity;

/**
 * @author gwz
 */
public abstract class CollectionEntity extends BaseEntity {

    protected final BoolEntity True = new BoolEntity(true);
    protected final BoolEntity False = new BoolEntity(false);

    @Override
    public final boolean isPrimitive() {
        return false;
    }

    @Override
    public final boolean isRequirement() {
        return false;
    }

    /**
     * Get the type of items in collection.
     * @return a String
     */
    public abstract String getItemType();

    /**
     * Get the size of collection.
     * @return An IntEntity containing the size of collection
     */
    public abstract IntEntity size();

}
