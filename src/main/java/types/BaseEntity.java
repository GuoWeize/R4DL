package types;

import types.primitive.BoolEntity;
import exceptions.TypeInvalidException;

import java.util.Objects;

/**
 * Abstract base class for all types.<p>
 * All entities must inheritance it and implement 4 methods:
 * {@code getType}, {@code isPrimitive}, {@code isRequirement}, {@code equal}.
 *
 * @author Guo Weize
 * @date 2021/2/1
 */
public abstract class BaseEntity {

    /**
     * Get type of this entity.<p>
     * Primitive type and customized type are their names(eg. int, bool, condition).<br>
     * Collection type are combined as follows:
     *     <li> ListEntity<E>: list[E] </li>
     *     <li> SetEntity<E>: set[E] </li>
     *     <li> MapEntity<E>: map[K, V] </li>
     * @return a string of this entity's type
     */
    public abstract String getType();

    /**
     * Check if it is null.
     * @return BoolEntity
     */
    public static BoolEntity isNull(BaseEntity entity) {
        if (entity == null) {
            return BoolEntity.TRUE;
        }
        return BoolEntity.FALSE;
    }

    /**
     * An Interface of no-static method of {@code equal}.
     * @param entity another entity
     * @return A "true" BoolEntity if two entities are equal, "false" BoolEntity otherwise
     */
    public abstract BoolEntity equal(final BaseEntity entity);

    /**
     * Check whether the real type matched the expectation.
     * @throws TypeInvalidException if not matched.
     */
    protected static void checkMatched(String real, String expectation) {
        if (! Objects.equals(real, expectation)) {
            throw new TypeInvalidException(real, expectation);
        }
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
        if (BaseEntity.class.isAssignableFrom(obj.getClass())) {
            return (this.equal((BaseEntity)obj)).getValue();
        }
        return false;
    }

}
