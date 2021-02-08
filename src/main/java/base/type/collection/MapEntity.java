package base.type.collection;

import base.type.BaseEntity;
import base.type.primitive.BoolEntity;
import base.type.primitive.IntEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Guo Weize
 */
public final class MapEntity<K extends BaseEntity, V extends BaseEntity> extends CollectionEntity {

    private String keyType = "";
    private String valueType = "";
    private final Map<K, V> entities = new HashMap<>();

    public MapEntity() {}

    public MapEntity(String keyType, String valueType, Map<K, V> entities) {
        this.keyType = keyType;
        this.valueType = valueType;
        this.entities.putAll(entities);
    }

    public BoolEntity allMatch(Function<K, BoolEntity> function) {
        for (K key: entities.keySet()) {
            if (! function.apply(key).getValue()) {
                return False;
            }
        }
        return True;
    }

    public BoolEntity anyMatch(Function<K, BoolEntity> function) {
        for (K key: entities.keySet()) {
            if (function.apply(key).getValue()) {
                return True;
            }
        }
        return False;
    }

    public BoolEntity containsKey(BaseEntity key) {
        if (! keyType.equals(key.getType())) {
            return False;
        }
        return anyMatch(key::equal);
    }

    public BoolEntity containsValue(BaseEntity value) {
        if (! valueType.equals(value.getType())) {
            return False;
        }
        for (V v: entities.values()) {
            if (v.equal(value).getValue()) {
                return True;
            }
        }
        return False;
    }

    public BoolEntity contains(BaseEntity key, BaseEntity value) {
        if (! containsKey(key).getValue()) {
            return False;
        }
        return value.equal(get(key));
    }

    public BoolEntity include(MapEntity<?, ?> map) {
        return map.allMatch(key -> contains(key, map.get(key)));
    }

    public void add(K key, V value) {
        if ("".equals(keyType)) {
            keyType = key.getType();
            valueType = value.getType();
        }
        entities.put(key, value);
    }

    public V get(BaseEntity key) {
        for (Map.Entry<K, V> entry: entities.entrySet()) {
            if (entry.getKey().equal(key).getValue()) {
                return entry.getValue();
            }
        }
        return null;
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
        MapEntity<?, ?> map = (MapEntity<?, ?>) entity;
        if (this.entities.size() != map.entities.size()) {
            return False;
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
