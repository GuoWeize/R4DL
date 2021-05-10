package base.dynamics;

import base.type.BaseEntity;
import base.type.primitive.StringEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuilderTest {

    @BeforeEach
    void setUp() {
        Compiler.run();
    }

    @Test
    void setField() {
        BaseEntity operation1 = Builder.newInstance("Operation");
        Builder.setField(operation1, "reaction", StringEntity.valueOf("test"));
        BaseEntity b1 = Builder.getField(operation1, "reaction");
        assert b1 != null;
        assertEquals("\"test\"", b1.toString());
    }

    @Test
    void newInstance() {
        BaseEntity operation1 = Builder.newInstance("Operation");
        assertNotNull(operation1);
        assertEquals("Operation", operation1.getType());
    }
}