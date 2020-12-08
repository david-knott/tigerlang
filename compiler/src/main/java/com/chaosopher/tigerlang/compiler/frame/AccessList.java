package com.chaosopher.tigerlang.compiler.frame;

public class AccessList {

    /**
     * Appends access t onto the end of TempList me. If TempList me is null and
     * access t is not, a new TempList with t as head is created a returned to the
     * caller.
     * 
     * @param me
     * @param t
     * @return
     */
    public static AccessList append(AccessList me, Access t) {
        if (me == null && t == null) {
            return null;
        }
        if (me == null && t != null) {
            return new AccessList(t);
        }
        if (me.tail == null) {
            return new AccessList(me.head, new AccessList(t));
        }
        return new AccessList(me.head, AccessList.append(me.tail, t));
    }

    public Access head;
    public AccessList tail;

    public AccessList(Access h) {
        head = h;
        tail = null;
    }

    public AccessList(Access h, AccessList t) {
        head = h;
        tail = t;
    }
}