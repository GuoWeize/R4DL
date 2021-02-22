package base.dynamics;

import base.type.BaseEntity;
import base.type.primitive.StringEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BuilderTest {

    @BeforeEach
    void setUp() {
        List<String> l = new ArrayList<>();
        l.add("Rule");
        l.add("Condition");
        l.add("Entity");
        l.add("Functional");
        l.add("Operation");

        Compiler.compile(l);
        var classes = Compiler.loadClass(l);
        Builder.initialization(classes);
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