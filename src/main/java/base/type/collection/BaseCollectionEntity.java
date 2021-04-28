package base.type.collection;

import base.type.BaseEntity;
import base.type.primitive.IntEntity;
import exception.TypeInvalidException;

/**
 * Abstract base class for all collection types, including list, set and map.<p>
 * Must implement 2 methods: {@code getItemType}, {@code size}
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
public abstract class BaseCollectionEntity extends BaseEntity {

    protected static final String TYPE_UNDEFINED = "_undefined_";

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

    /**
     * Check whether the real type matched the expectation.
     * @throws TypeInvalidException if not matched.
     */
    protected void checkType(String real, String expectation) {
        if (! expectation.equals(real)) {
            throw new TypeInvalidException(real, expectation);
        }
    }
}
