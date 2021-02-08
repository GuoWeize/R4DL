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
 * @author Guo Weize
 */
public final class ListEntity<E extends BaseEntity> extends CollectionEntity {

    private String type = "";
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

    public E get(int index) {
        return entities.get(index);
    }

    public void add(E entity) {
        if ("".equals(type)) {
            type = entity.getType();
        }
        entities.add(entity);
    }

    public BoolEntity allMatch(Function<E, BoolEntity> function) {
        for (E entity: entities) {
            if (! function.apply(entity).getValue()) {
                return False;
            }
        }
        return True;
    }

    public BoolEntity anyMatch(Function<E, BoolEntity> function) {
        for (E entity: entities) {
            if (function.apply(entity).getValue()) {
                return True;
            }
        }
        return False;
    }

    public BoolEntity contains(BaseEntity entity) {
        if (! type.equals(entity.getType())) {
            return False;
        }
        return anyMatch(entity::equal);
    }

    public BoolEntity include(ListEntity<?> list) {
        return list.allMatch(this::contains);
    }

    @Override
    public IntEntity size() {
        return new IntEntity(entities.size());
    }

    @Override
    public BoolEntity equal(BaseEntity entity) {
        if (! entity.getType().equals(this.getType())) {
            return False;
        }
        ListEntity<?> list = (ListEntity<?>) entity;
        if (this.entities.size() != list.entities.size()) {
            return False;
        }
        for (int i = 0; i < entities.size(); i++) {
            if (! entities.get(i).equal(list.entities.get(i)).getValue()) {
                return False;
            }
        }
        return True;
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
