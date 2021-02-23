package base.type.collection;

import base.type.BaseEntity;
import base.type.primitive.BoolEntity;
import base.type.primitive.IntEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * List class that store data by indexes.<p>
 * There are 6 specific methods:
 * {@code allMatch}, {@code anyMatch}, {@code contains}, {@code include}, {@code add}, {@code get}.
 *
 * @author Guo Weize
 */
public final class ListEntity<E extends BaseEntity> extends BaseCollectionEntity {

    /** Entity type of this list */
    private String type = "";

    /** Storage data structure of ListEntity */
    private final List<E> entities = new ArrayList<>();

    public ListEntity(String type, List<E> entities){
        this.type = type;
        this.entities.addAll(entities);
    }

    @SafeVarargs
    public ListEntity(String type, E... entities) {
        this.type = type;
        this.entities.addAll(Arrays.asList(entities));
    }

    public ListEntity() {}

    /**
     * Get entity at the specific index
     * @param index the specific index number, must in bounds.
     * @return entity at the specified position in this list.
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public E get(int index) {
        return entities.get(index);
    }
    public E get(IntEntity index) {
        return entities.get(index.getValue());
    }

    /**
     * Add an entity to the end of this list, change inner stored data.
     * @param entity to be added to this list.
     * @throws IllegalArgumentException if entity has illegal type.
     */
    public void add(E entity) {
        if ("".equals(type)) {
            type = entity.getType();
        }
        else if (! type.equals(entity.getType())) {
            throw new IllegalArgumentException();
        }
        entities.add(entity);
    }

    /**
     * Return whether all entities in list satisfy the specific condition.
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
     * Return whether any entity in list satisfies the specific condition.
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
     * Return whether this list contain the specific entity or not.
     * @param entity the specific entity.
     * @return True if this list contain the specific entity, otherwise False.
     * @throws IllegalArgumentException if entity has illegal type.
     */
    public BoolEntity contains(BaseEntity entity) {
        if (! type.equals(entity.getType())) {
            throw new IllegalArgumentException();
        }
        return anyMatch(entity::equal);
    }

    /**
     * Return whether this list include another list or not.
     * @param list another list.
     * @return True if this list include another list, otherwise False.
     * @throws IllegalArgumentException if entity has illegal type.
     */
    public BoolEntity include(ListEntity<?> list) {
        return list.allMatch(this::contains);
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
        ListEntity<?> list = (ListEntity<?>) entity;
        if (this.entities.size() != list.entities.size()) {
            return BoolEntity.FALSE;
        }
        for (int i = 0; i < entities.size(); i++) {
            if (! entities.get(i).equal(list.entities.get(i)).getValue()) {
                return BoolEntity.FALSE;
            }
        }
        return BoolEntity.TRUE;
    }

    @Override
    public String getItemType() {
        return type;
    }

    @Override
    public String getType() {
        return "list[" + getItemType() + "]";
    }

    @Override
    public String toString() {
        return "[" +
                entities.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", ")) +
                "]";
    }
}
