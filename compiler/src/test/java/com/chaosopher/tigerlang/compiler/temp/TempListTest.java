package com.chaosopher.tigerlang.compiler.temp;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;

public class TempListTest {
    
    @Test
    public void checkOrderTrue() {
        Temp t1 = Temp.create();
        Temp t2 = Temp.create();
        Temp t3 = Temp.create();
        Temp t4 = Temp.create();
        TempList tl = null;
        tl = TempList.append(tl, t1);
        tl = TempList.append(tl, t2);
        tl = TempList.append(tl, t3);
        tl = TempList.append(tl, t4);
        TempList.checkOrder(tl);
    }

    @Test
    public void checkOrderFalse() {
        Temp t1 = Temp.create();
        Temp t2 = Temp.create();
        Temp t3 = Temp.create();
        Temp t4 = Temp.create();
        TempList tl = null;
        tl = TempList.append(tl, t2);
        tl = TempList.append(tl, t1);
        tl = TempList.append(tl, t3);
        tl = TempList.append(tl, t4);
        TempList.checkOrder(tl);
    }

    @Test
    public void sortOne() {
        Temp t1 = Temp.create();
        TempList tl = null;
        tl = TempList.append(tl, t1);
        tl = TempList.sort(tl);
        TempList.checkOrder(tl);
    }

    @Test
    public void sortTwo() {
        Temp t1 = Temp.create();
        Temp t2 = Temp.create();
        TempList tl = null;
        tl = TempList.append(tl, t2);
        tl = TempList.append(tl, t1);
        tl = TempList.sort(tl);
        TempList.checkOrder(tl);
    }

    @Test
    public void sortThree() {
        Temp t1 = Temp.create();
        Temp t2 = Temp.create();
        Temp t3 = Temp.create();
        TempList tl = null;
        tl = TempList.append(tl, t2);
        tl = TempList.append(tl, t1);
        tl = TempList.append(tl, t3);
        tl = TempList.sort(tl);
        TempList.checkOrder(tl);
    }

    @Test
    public void sortFour() {
        Temp t1 = Temp.create();
        Temp t2 = Temp.create();
        Temp t3 = Temp.create();
        Temp t4 = Temp.create();
        TempList tl = null;
        tl = TempList.append(tl, t4);
        tl = TempList.append(tl, t2);
        tl = TempList.append(tl, t1);
        tl = TempList.append(tl, t3);
        tl = TempList.sort(tl);
        TempList.checkOrder(tl);
    }

    @Test
    public void andTest() {
        Temp t16 = Temp.create();
        Temp t18 = Temp.create();
        Temp t19 = Temp.create();
        TempList t1 = null;
        TempList t2 = null;
        t1 = TempList.append(t1, t18);
        t1 = TempList.append(t1, t16);
        t2 = TempList.append(t2, t18);
        t2 = TempList.append(t2, t19);
        TempList t3 = TempList.and(t1, t2);
        TempList t4 = TempList.and(t2, t1);
        assertEquals(t3.head, t18);
        assertEquals(t4.head, t18);
    }

    @Test
    public void orTest() {
        Temp t16 = Temp.create();
        Temp t18 = Temp.create();
        Temp t19 = Temp.create();
        TempList t1 = null;
        TempList t2 = null;
        t1 = TempList.append(t1, t18);
        t1 = TempList.append(t1, t16);
        t2 = TempList.append(t2, t18);
        t2 = TempList.append(t2, t19);
        TempList t3 = TempList.or(t1, t2);
        TempList t4 = TempList.or(t2, t1);
        assertEquals(t3.head, t16);
        assertEquals(t3.tail.head, t18);
        assertEquals(t3.tail.tail.head, t19);
        assertEquals(t4.head, t16);
        assertEquals(t4.tail.head, t18);
        assertEquals(t4.tail.tail.head, t19);
    }

    @Test
    public void andNotTest() {
        Temp t16 = Temp.create();
        Temp t18 = Temp.create();
        Temp t19 = Temp.create();
        TempList t1 = null;
        TempList t2 = null;
        t1 = TempList.append(t1, t18);
        t1 = TempList.append(t1, t16);
        t2 = TempList.append(t2, t18);
        t2 = TempList.append(t2, t19);
        TempList t3 = TempList.andNot(t1, t2);
        TempList t4 = TempList.andNot(t2, t1);
        assertEquals(t3.head, t16);
        assertEquals(t4.head, t19);
    }




}