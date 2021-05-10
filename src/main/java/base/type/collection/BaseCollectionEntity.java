package base.type.collection;

import base.type.BaseEntity;
import base.type.primitive.IntEntity;

/**
 * Abstract base class for all collection types, including list, set and map.<p>
 * Must implement 2 methods: {@code getItemType}, {@code size}
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
public abstract class BaseCollectionEntity extends BaseEntity {

    /**
     * Annotate the item type that is not defined yet.
     */
    protected static final String TYPE_UNDEFINED = "_undefined_";
    /**
     * delimiter of items in {@code toString} methods of collections
     */
    protected static final String DELIMITER = ", ";

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

    @Override
    public final boolean isPrimitive() {
        return false;
    }

    @Override
    public final boolean isRequirement() {
        return false;
    }
}
