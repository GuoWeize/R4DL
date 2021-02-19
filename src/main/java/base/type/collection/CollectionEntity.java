package base.type.collection;

import base.type.BaseEntity;
import base.type.primitive.BoolEntity;
import base.type.primitive.IntEntity;

/**
 * Abstract base class for all collection types, including list, set and map.<p>
 * Must implement 2 methods: {@code getItemType}, {@code size}
 *
 * @author Guo Weize
 * @date 2021/2/1
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
