package base.dynamics;

import base.type.primitive.StringEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import process.judge.Processor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProcessorTest {

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
        Processor.initialization(classes.get("Rule"));
    }

    @Test
    void process() {
        Object operation1 = Builder.newInstance("Operation");
        Object operation2 = Builder.newInstance("Operation");
        Builder.setField("Operation", "reaction", operation1, new StringEntity("test"));
        Builder.setField("Operation", "reaction", operation2, new StringEntity("passed"));

        Object functional1 = Builder.newInstance("Functional");
        Object functional2 = Builder.newInstance("Functional");
        Builder.setField("Functional", "operation", functional1, operation1);
        Builder.setField("Functional", "operation", functional2, operation2);

        Object r = Processor.process("cal", functional1, functional2);
        assertEquals("\"test\" \"passed\"", r);
    }
}