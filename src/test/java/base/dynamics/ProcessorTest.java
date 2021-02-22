package base.dynamics;

import base.type.BaseEntity;
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
        Processor.initialization();
    }

    @Test
    void process() {
        BaseEntity operation1 = Builder.newInstance("Operation");
        BaseEntity operation2 = Builder.newInstance("Operation");
        Builder.setField(operation1, "reaction", StringEntity.valueOf("test"));
        Builder.setField(operation2, "reaction", StringEntity.valueOf("passed"));

        BaseEntity functional1 = Builder.newInstance("Functional");
        BaseEntity functional2 = Builder.newInstance("Functional");
        Builder.setField(functional1, "operation", operation1);
        Builder.setField(functional2, "operation", operation2);
    }
}