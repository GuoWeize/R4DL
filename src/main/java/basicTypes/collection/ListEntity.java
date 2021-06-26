package basicTypes.collection;

import basicTypes.BaseEntity;
import basicTypes.primitive.BoolEntity;
import basicTypes.primitive.IntEntity;
import exceptions.TypeInvalidException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * List class that store data by indexes.<p>
 * There are 6 specific methods:
 * {@code allMatch}, {@code anyMatch}, {@code contains}, {@code include}, {@code add}, {@code get}.
 *
 * @author Guo Weize
 * @date 2021/02/01
 */
public final class ListEntity<E extends BaseEntity> extends BaseCollectionEntity {

    /** Entity type of this list */
    private String type = TYPE_UNDEFINED;

    /** Storage data structure of ListEntity */
    private final List<E> entities = new ArrayList<>();

    public ListEntity() {}

    public ListEntity(String type, List<E> entities){
        this.type = type;
        this.entities.addAll(entities);
    }

    /**
     * Get entity at the specific index
     * @param index the specific index number, must in bounds.
     * @return entity at the specified position in this list.
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public E get(BaseEntity index) {
        checkMatched(index.getType(), "integer");
        return entities.get(((IntEntity)index).getValue());
    }

    /**
     * Add an entity to the end of this list, change inner stored data.
     * @param entity to be added to this list.
     * @throws TypeInvalidException if entity has illegal type.
     */
    public void add(E entity) {
        if (Objects.equals(type, TYPE_UNDEFINED)) {
            type = entity.getType();
        }
        checkMatched(entity.getType(), type);
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
     * @throws TypeInvalidException if entity has illegal type.
     */
    public BoolEntity contains(BaseEntity entity) {
        checkMatched(entity.getType(), type);
        return anyMatch(entity::equal);
    }

    /**
     * Return whether this list include another list or not.
     * @param list another list.
     * @return True if this list include another list, otherwise False.
     * @throws TypeInvalidException if entity has illegal type.
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
        if (! Objects.equals(entity.getType(), this.getType())) {
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

    public static ListEntity<? extends BaseEntity> newInstance() {
        return new ListEntity<>();
    }

    @Override
    public String getItemType() {
        return type;
    }

    @Override
    public String getType() {
        return String.format("list[%s]", getItemType());
    }

    @Override
    public String toString() {
        return String.format("[%s]", entities.stream().map(Object::toString).collect(Collectors.joining(DELIMITER)));
    }
}
