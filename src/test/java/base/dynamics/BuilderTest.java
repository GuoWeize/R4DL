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
        Object operation1 = Builder.newInstance("Operation");
        Builder.setField("Operation", "reaction", operation1, new StringEntity("test"));
        BaseEntity b1 = (BaseEntity) Builder.getField("Operation", "reaction", operation1);
        assert b1 != null;
        assertEquals("\"test\"", b1.toString());
    }

    @Test
    void newInstance() {
        Object operation1 = Builder.newInstance("Operation");
        assertNotNull(operation1);
        BaseEntity obj = (BaseEntity) operation1;
        assertEquals("Operation", obj.getType());
    }
}