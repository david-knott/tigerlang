package com.chaosopher.tigerlang.compiler.tree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.Test;

public class StmListTest {

    private EmptyStm castToEmptyStm(Object object) {
        return (EmptyStm)object;
    }

    @Test
    public void appendStm() {
        StmList list1 = new StmList(new EmptyStm("one"));
        list1.append(new EmptyStm("two"));
        list1.append(new EmptyStm("three"));
        assertTrue(list1.head instanceof EmptyStm);
        assertTrue(list1.tail.head instanceof EmptyStm);
        assertTrue(list1.tail.tail.head instanceof EmptyStm);
        assertTrue(list1.tail.tail.tail == null);
        assertEquals("one", castToEmptyStm(list1.head).toString());
        assertEquals("two", castToEmptyStm(list1.tail.head).toString());
        assertEquals("three", castToEmptyStm(list1.tail.tail.head).toString());
    }

    @Test
    public void prependStm() {
        StmList list1 = new StmList(new EmptyStm("one"));
        list1 = list1.prepend(new EmptyStm("two"));
        list1 = list1.prepend(new EmptyStm("three"));
        assertTrue(list1.head instanceof EmptyStm);
        assertTrue(list1.tail.head instanceof EmptyStm);
        assertTrue(list1.tail.tail.head instanceof EmptyStm);
        assertTrue(list1.tail.tail.tail == null);
        assertEquals("three", castToEmptyStm(list1.head).toString());
        assertEquals("two", castToEmptyStm(list1.tail.head).toString());
        assertEquals("one", castToEmptyStm(list1.tail.tail.head).toString());
    }
    
    @Test
    public void appendStmList() {
        StmList list1 = new StmList(new EmptyStm("one"), new StmList(new EmptyStm("two"), new StmList(new EmptyStm("three"))));
        StmList list2 = new StmList(new EmptyStm("four"), new StmList(new EmptyStm("five"), new StmList(new EmptyStm("six"))));
        StmList list3 = new StmList(new EmptyStm("seven"), new StmList(new EmptyStm("eight"), new StmList(new EmptyStm("nine"))));
        list1.append(list2);
        list1.append(list3);
        assertTrue(list1.head instanceof EmptyStm);
        assertTrue(list1.tail.head instanceof EmptyStm);
        assertTrue(list1.tail.tail.head instanceof EmptyStm);
        assertTrue(list1.tail.tail.tail.head instanceof EmptyStm);
        assertTrue(list1.tail.tail.tail.tail.head instanceof EmptyStm);
        assertTrue(list1.tail.tail.tail.tail.tail.head instanceof EmptyStm);
        assertTrue(list1.tail.tail.tail.tail.tail.tail.head instanceof EmptyStm);
        assertTrue(list1.tail.tail.tail.tail.tail.tail.tail.head instanceof EmptyStm);
        assertTrue(list1.tail.tail.tail.tail.tail.tail.tail.tail.head instanceof EmptyStm);
        assertTrue(list1.tail.tail.tail.tail.tail.tail.tail.tail.tail == null);
        assertEquals("one", castToEmptyStm(list1.head).toString());
        assertEquals("two", castToEmptyStm(list1.tail.head).toString());
        assertEquals("three", castToEmptyStm(list1.tail.tail.head).toString());
        assertEquals("four", castToEmptyStm(list1.tail.tail.tail.head).toString());
        assertEquals("five", castToEmptyStm(list1.tail.tail.tail.tail.head).toString());
        assertEquals("eight", castToEmptyStm(list1.tail.tail.tail.tail.tail.tail.tail.head).toString());
        assertEquals("nine", castToEmptyStm(list1.tail.tail.tail.tail.tail.tail.tail.tail.head).toString());
    }
    
    @Test
    public void SprependStmList() {
        StmList list1 = new StmList(new EmptyStm("one"), new StmList(new EmptyStm("two"), new StmList(new EmptyStm("three"))));
        StmList list2 = new StmList(new EmptyStm("four"), new StmList(new EmptyStm("five"), new StmList(new EmptyStm("six"))));
        StmList list3 = new StmList(new EmptyStm("seven"), new StmList(new EmptyStm("eight"), new StmList(new EmptyStm("nine"))));
        list1 = list1.prepend(list2);
        list1 = list1.prepend(list3);
        assertTrue(list1.head instanceof EmptyStm);
        assertTrue(list1.tail.head instanceof EmptyStm);
        assertTrue(list1.tail.tail.head instanceof EmptyStm);
        assertTrue(list1.tail.tail.tail.head instanceof EmptyStm);
        assertTrue(list1.tail.tail.tail.tail.head instanceof EmptyStm);
        assertTrue(list1.tail.tail.tail.tail.tail.head instanceof EmptyStm);
        assertTrue(list1.tail.tail.tail.tail.tail.tail.head instanceof EmptyStm);
        assertTrue(list1.tail.tail.tail.tail.tail.tail.tail.head instanceof EmptyStm);
        assertTrue(list1.tail.tail.tail.tail.tail.tail.tail.tail.head instanceof EmptyStm);
        assertTrue(list1.tail.tail.tail.tail.tail.tail.tail.tail.tail == null);
        assertEquals("seven", castToEmptyStm(list1.head).toString());
        assertEquals("eight", castToEmptyStm(list1.tail.head).toString());
        assertEquals("nine", castToEmptyStm(list1.tail.tail.head).toString());
        assertEquals("four", castToEmptyStm(list1.tail.tail.tail.head).toString());
        assertEquals("five", castToEmptyStm(list1.tail.tail.tail.tail.head).toString());
        assertEquals("two", castToEmptyStm(list1.tail.tail.tail.tail.tail.tail.tail.head).toString());
        assertEquals("three", castToEmptyStm(list1.tail.tail.tail.tail.tail.tail.tail.tail.head).toString());
    }
    
}