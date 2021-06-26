package basicTypes;

import basicTypes.primitive.BoolEntity;
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
     * Only entities of requirement types can be processed by next steps.
     * @return whether this entity is a requirement type
     */
    public abstract boolean isRequirement();

    /**
     * An Interface of no-static method of {@code equal}.
     * @param entity another entity
     * @return A "true" BoolEntity if two entities are equal, "false" BoolEntity otherwise
     */
    public abstract BoolEntity equal(BaseEntity entity);

    /**
     * Check whether entity1 is equal to entity2, which means all fields of two entities are equal.<br>
     * If there is null, return a "false" BoolEntity.
     * @param entity1 an entity that extends {@link BaseEntity}
     * @param entity2 another entity that extends {@link BaseEntity}
     * @return A "true" BoolEntity if two entities are equal, "false" BoolEntity otherwise
     */
    public static BoolEntity equal(BaseEntity entity1, BaseEntity entity2) {
        if (entity1 == null || entity2 == null) {
            return BoolEntity.FALSE;
        }
        return entity1.equal(entity2);
    }

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

    public final BoolEntity isNull(Object entity) {
        return BoolEntity.valueOf(entity == null);
    }
}
