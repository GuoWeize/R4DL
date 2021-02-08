package base.type.collection;

import base.type.primitive.BoolEntity;
import base.type.primitive.IntEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MapEntityTest {
    private MapEntity<IntEntity, BoolEntity> m;

    @BeforeEach
    void setUp() {
        BoolEntity b1 = new BoolEntity(true);
        BoolEntity b2 = new BoolEntity(true);
        IntEntity i1 = new IntEntity(1);
        IntEntity i2 = new IntEntity(2);
        Map<IntEntity, BoolEntity> temp = new HashMap<>();
        temp.put(i1, b1);
        temp.put(i2, b2);
        m = new MapEntity<>("integer", "boolean", temp);
    }

    @Test
    void containsKey() {
        IntEntity i = new IntEntity(1);
        assertTrue(m.containsKey(i).getValue());
        i = new IntEntity(3);
        assertFalse(m.containsKey(i).getValue());
    }

    @Test
    void containsValue() {
        BoolEntity b = new BoolEntity(true);
        assertTrue(m.containsValue(b).getValue());
        b = new BoolEntity(false);
        assertFalse(m.containsValue(b).getValue());
    }

    @Test
    void testEquals() {
        BoolEntity b1 = new BoolEntity(true);
        BoolEntity b2 = new BoolEntity(true);
        IntEntity i1 = new IntEntity(1);
        IntEntity i2 = new IntEntity(2);
        Map<IntEntity, BoolEntity> temp = new HashMap<>();
        temp.put(i1, b1);
        temp.put(i2, b2);
        MapEntity<IntEntity, BoolEntity> m_new = new MapEntity<>("integer", "boolean", temp);
        assertTrue(m_new.equal(m).getValue());

        temp.clear();
        b2 = new BoolEntity(false);
        temp.put(i1, b1);
        temp.put(i2, b2);
        m_new = new MapEntity<>("integer", "boolean", temp);
        assertFalse(m_new.equal(m).getValue());
    }
}