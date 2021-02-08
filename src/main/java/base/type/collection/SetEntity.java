package base.type.collection;

import base.type.BaseEntity;
import base.type.primitive.BoolEntity;
import base.type.primitive.IntEntity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Guo Weize
 */
public final class SetEntity<E extends BaseEntity> extends CollectionEntity {

    private String type = "";
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

    public BoolEntity include(SetEntity<?> set) {
        return set.allMatch(this::contains);
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
        SetEntity<?> set = (SetEntity<?>) entity;
        if (this.entities.size() != set.entities.size()) {
            return False;
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
