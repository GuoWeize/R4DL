package base.type.collection;

import base.type.BaseEntity;
import base.type.primitive.BoolEntity;
import base.type.primitive.IntEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Map class that store data by key-value pairs.<p>
 * There are 6 specific methods:
 * {@code allMatch}, {@code anyMatch}, {@code contains}, {@code containsKey}, {@code containsValue},
 * {@code include}, {@code add}, {@code get}.
 *
 * @author Guo Weize
 */
public final class MapEntity<K extends BaseEntity, V extends BaseEntity> extends BaseCollectionEntity {

    private String keyType = "";
    private String valueType = "";
    private final Map<K, V> entities = new HashMap<>();

    public MapEntity() {}

    public MapEntity(String keyType, String valueType, Map<K, V> entities) {
        this.keyType = keyType;
        this.valueType = valueType;
        this.entities.putAll(entities);
    }

    /**
     * Add an key-value pair to this map, change inner stored data.
     * @param key to be added to this map.
     * @param value to be added to this map.
     * @throws IllegalArgumentException if entity has illegal type.
     */
    public void add(K key, V value) {
        if ("".equals(keyType)) {
            keyType = key.getType();
            valueType = value.getType();
        }
        else if (! keyType.equals(key.getType()) || ! valueType.equals(value.getType())) {
            throw new IllegalArgumentException();
        }
        entities.put(key, value);
    }

    /**
     * Return value entity with the specific key.
     * @param key to get value.
     * @return the entity if this map contains key.
     * @throws IllegalArgumentException if this map not contains key.
     */
    public V get(BaseEntity key) {
        for (Map.Entry<K, V> entry: entities.entrySet()) {
            if (entry.getKey().equal(key).getValue()) {
                return entry.getValue();
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Return whether all keys in map satisfy the specific condition.
     * @param condition that key shall satisfy.
     * @return True if all keys satisfy the condition, otherwise False.
     */
    public BoolEntity allMatch(Function<K, BoolEntity> condition) {
        for (K key: entities.keySet()) {
            if (! condition.apply(key).getValue()) {
                return BoolEntity.FALSE;
            }
        }
        return BoolEntity.TRUE;
    }

    /**
     * Return whether any key in map satisfies the specific condition.
     * @param condition that key shall satisfy.
     * @return True if any key satisfies the condition, otherwise False.
     */
    public BoolEntity anyMatch(Function<K, BoolEntity> condition) {
        for (K key: entities.keySet()) {
            if (condition.apply(key).getValue()) {
                return BoolEntity.TRUE;
            }
        }
        return BoolEntity.FALSE;
    }

    /**
     * Return whether this map contain the specific key or not.
     * @param key the specific key.
     * @return True if this map contain the specific key, otherwise False.
     * @throws IllegalArgumentException if key has illegal type.
     */
    public BoolEntity containsKey(BaseEntity key) {
        if (! keyType.equals(key.getType())) {
            throw new IllegalArgumentException();
        }
        return anyMatch(key::equal);
    }

    /**
     * Return whether this map contain the specific value or not.
     * @param value the specific value.
     * @return True if this map contain the specific value, otherwise False.
     * @throws IllegalArgumentException if value has illegal type.
     */
    public BoolEntity containsValue(BaseEntity value) {
        if (! valueType.equals(value.getType())) {
            throw new IllegalArgumentException();
        }
        for (V v: entities.values()) {
            if (v.equal(value).getValue()) {
                return BoolEntity.TRUE;
            }
        }
        return BoolEntity.FALSE;
    }

    /**
     * Return whether this map contain the specific key-value pair.
     * @param key the specific key.
     * @param value the specific value.
     * @return True if this map contain the specific key-value pair, otherwise False.
     * @throws IllegalArgumentException if key or value has illegal type.
     */
    public BoolEntity contains(BaseEntity key, BaseEntity value) {
        if (! keyType.equals(key.getType()) || ! valueType.equals(value.getType())) {
            throw new IllegalArgumentException();
        }
        if (! containsKey(key).getValue()) {
            return BoolEntity.FALSE;
        }
        return value.equal(get(key));
    }

    /**
     * Return whether this map include another map or not.
     * @param map another map.
     * @return True if this map include another map, otherwise False.
     * @throws IllegalArgumentException if entity has illegal type.
     */
    public BoolEntity include(MapEntity<?, ?> map) {
        return map.allMatch(key -> contains(key, map.get(key)));
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
        MapEntity<?, ?> map = (MapEntity<?, ?>) entity;
        if (this.entities.size() != map.entities.size()) {
            return BoolEntity.FALSE;
        }
        return include(map);
    }

    @Override
    public String getItemType() {
        return keyType + ", " + valueType;
    }

    @Override
    public String getType() {
        return "map[" + getItemType() + "]";
    }
}
