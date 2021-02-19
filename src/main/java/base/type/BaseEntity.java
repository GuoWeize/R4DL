package base.type;

import base.type.primitive.BoolEntity;

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
     *
     * @return a string of this entity's type
     */
    public abstract String getType();

    /**
     * Primitive types include: BoolEntity, FloatEntity, IntEntity, StringEntity.
     * @return whether this entity is primitive type
     */
    public abstract boolean isPrimitive();

    /**
     * Only entities of requirement types can be processed by next steps.
     * @return whether this entity is requirement type
     */
    public abstract boolean isRequirement();

    /**
     * Whether this entity is equal to another entity.<p>
     * That means all fields of two entities are equal.
     * @param entity another entity
     * @return A "true" BoolEntity if two entities are equal, "false" BoolEntity otherwise
     */
    public abstract BoolEntity equal(BaseEntity entity);
}
