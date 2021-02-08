package base.type.collection;

import base.type.primitive.BoolEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListEntityTest {
    private ListEntity<BoolEntity> l;

    @BeforeEach
    void setUp() {
        BoolEntity b = new BoolEntity(true);
        BoolEntity b2 = new BoolEntity(false);
        l = new ListEntity<>("boolean", b, b2);
    }

    @Test
    void demo() {
        assertEquals(ListEntity.class, l.getClass());
    }

    @Test
    void contains() {
        BoolEntity b = new BoolEntity(true);
        assertTrue(l.contains(b).getValue());
    }

    @Test
    void getItemType() {
        l = new ListEntity<>("boolean");
        assertEquals("boolean", l.getItemType());
    }

    @Test
    void getType() {
        assertEquals("list[boolean]", l.getType());
    }

    @Test
    void testToString() {
        assertEquals("[true, false]", l.toString());
    }

    @Test
    void testEquals() {
        BoolEntity b = new BoolEntity(true);
        BoolEntity b2 = new BoolEntity(false);

        ListEntity<BoolEntity> l_new = new ListEntity<>("boolean", b, b2);
        assertTrue(l.equal(l_new).getValue());
        l_new = new ListEntity<>("boolean", b, b);
        assertFalse(l.equal(l_new).getValue());
        l_new = new ListEntity<>("boolean", b2, b);
        assertFalse(l_new.equal(l).getValue());
    }
}