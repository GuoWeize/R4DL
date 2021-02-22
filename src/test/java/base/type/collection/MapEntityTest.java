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
        BoolEntity b1 = BoolEntity.TRUE;
        BoolEntity b2 = BoolEntity.TRUE;
        IntEntity i1 = IntEntity.valueOf(1);
        IntEntity i2 = IntEntity.valueOf(2);
        Map<IntEntity, BoolEntity> temp = new HashMap<>();
        temp.put(i1, b1);
        temp.put(i2, b2);
        m = new MapEntity<>("integer", "boolean", temp);
    }

    @Test
    void containsKey() {
        IntEntity i = IntEntity.valueOf(1);
        assertTrue(m.containsKey(i).getValue());
        i = IntEntity.valueOf(3);
        assertFalse(m.containsKey(i).getValue());
    }

    @Test
    void containsValue() {
        BoolEntity b = BoolEntity.TRUE;
        assertTrue(m.containsValue(b).getValue());
        b = BoolEntity.FALSE;
        assertFalse(m.containsValue(b).getValue());
    }

    @Test
    void testEquals() {
        BoolEntity b1 = BoolEntity.TRUE;
        BoolEntity b2 = BoolEntity.TRUE;
        IntEntity i1 = IntEntity.valueOf(1);
        IntEntity i2 = IntEntity.valueOf(2);
        Map<IntEntity, BoolEntity> temp = new HashMap<>();
        temp.put(i1, b1);
        temp.put(i2, b2);
        MapEntity<IntEntity, BoolEntity> m_new = new MapEntity<>("integer", "boolean", temp);
        assertTrue(m_new.equal(m).getValue());

        temp.clear();
        b2 = BoolEntity.FALSE;
        temp.put(i1, b1);
        temp.put(i2, b2);
        m_new = new MapEntity<>("integer", "boolean", temp);
        assertFalse(m_new.equal(m).getValue());
    }
}