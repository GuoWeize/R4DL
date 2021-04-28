package base.type.collection;

import base.type.BaseEntity;
import base.type.primitive.BoolEntity;
import base.type.primitive.IntEntity;
import exception.TypeInvalidException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Set class that store data by keys.<p>
 * There are 5 specific methods:
 * {@code allMatch}, {@code anyMatch}, {@code contains}, {@code include}, {@code add}.
 *
 * @author Guo Weize
 */
public final class SetEntity<E extends BaseEntity> extends BaseCollectionEntity {

    /** Entity type of this set */
    private String type = TYPE_UNDEFINED;

    /** Storage data structure of SetEntity */
    private final Set<E> entities = new HashSet<>();

    public SetEntity() {}

    public SetEntity(String type, Set<E> entities){
        this.type = type;
        this.entities.addAll(entities);
    }

    @SafeVarargs
    public SetEntity(String type, E... entities) {
        this.type = type;
        this.entities.addAll(Arrays.asList(entities));
    }

    /**
     * Add an entity to this set, change inner stored data.
     * @param entity to be added to this set.
     * @throws TypeInvalidException if entity has illegal type.
     */
    public void add(E entity) {
        if (TYPE_UNDEFINED.equals(type)) {
            type = entity.getType();
        }
        checkType(entity.getType(), type);
        entities.add(entity);
    }

    /**
     * Return whether all entities in set satisfy the specific condition.
     * @param condition that entity shall satisfy.
     * @return True if all entities satisfy the condition, otherwise False.
     */
    public BoolEntity allMatch(Function<E, BoolEntity> condition) {
        for (E entity: entities) {
            if (! condition.apply(entity).getValue()) {
                return BoolEntity.FALSE;
            }
        }
        return BoolEntity.TRUE;
    }

    /**
     * Return whether any entity in set satisfies the specific condition.
     * @param condition that entity shall satisfy.
     * @return True if any entity satisfies the condition, otherwise False.
     */
    public BoolEntity anyMatch(Function<E, BoolEntity> condition) {
        for (E entity: entities) {
            if (condition.apply(entity).getValue()) {
                return BoolEntity.TRUE;
            }
        }
        return BoolEntity.FALSE;
    }

    /**
     * Return whether this set contain the specific entity or not.
     * @param entity the specific entity.
     * @return True if this set contain the specific entity, otherwise False.
     * @throws TypeInvalidException if entity has illegal type.
     */
    public BoolEntity contains(BaseEntity entity) {
        checkType(entity.getType(), type);
        return anyMatch(entity::equal);
    }

    /**
     * Return whether this set include another set or not.
     * @param set another set.
     * @return True if this set include another set, otherwise False.
     * @throws TypeInvalidException if entity has illegal type.
     */
    public BoolEntity include(SetEntity<?> set) {
        return set.allMatch(this::contains);
    }

    @Override
    public IntEntity size() {
        return IntEntity.valueOf(entities.size());
    }

    @Override
    public BoolEntity equal(BaseEntity entity) {
        if (! entity.getType().equals(this.getType())) {
            return BoolEntity.FALSE;
        }
        SetEntity<?> set = (SetEntity<?>) entity;
        if (this.entities.size() != set.entities.size()) {
            return BoolEntity.FALSE;
        }
        return include(set);
    }

    @Override
    public String getItemType() {
        return type;
    }

    @Override
    public String getType() {
        return "set[" + getItemType() + "]";
    }

    @Override
    public String toString() {
        return "{" +
                entities.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", ")) +
                "}";
    }
}
