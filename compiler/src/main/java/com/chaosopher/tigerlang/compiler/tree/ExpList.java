package com.chaosopher.tigerlang.compiler.tree;

public class ExpList {

    public static ExpList append(ExpList me, Exp t) {
        if (me == null) {
            return new ExpList(t);
        }
        if (me.tail == null) {
            return new ExpList(me.head, new ExpList(t));
        }
        return new ExpList(me.head, ExpList.append(me.tail, t));
    }

    /**
     * Return this set in reverse.
     * 
     * @return a new linked list with this lists elements in reverse.
     */
    public static ExpList reverse(ExpList me) {
        if(me == null) {
            return null;
        }
        if (me.tail == null) {
            return new ExpList(me.head);
        }
        return ExpList.append(ExpList.reverse(me.tail), me.head);
    }

    public static int size(ExpList me) {
        if(me == null) return 0;
        return me.size();

    }

    public Exp head;
    public ExpList tail;

    public ExpList(Exp h) {
        head = h;
        tail = null;
    }

    public ExpList(Exp h, ExpList t) {
        head = h;
        tail = t;
    }

    public ExpList append(com.chaosopher.tigerlang.compiler.tree.Exp exp) {
        if (exp == null)
            throw new IllegalArgumentException("Exp cannot be null");
        var last = this;
        while (last.tail != null) {
            last = last.tail;
        }
        last.tail = new ExpList(exp, null);
        return last.tail;
    }

    public int size() {
        int i = 0;
        for(ExpList me = this; me != null; me = me.tail) {
            i++;
        }
        return i;
    }

    @Override
    public boolean equals(Object obj) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public int hashCode() {
        throw new RuntimeException("Not implemented");
    }
}
