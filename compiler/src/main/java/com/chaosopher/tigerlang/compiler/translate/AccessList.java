package com.chaosopher.tigerlang.compiler.translate;

public class AccessList {
    public Access head;
    public AccessList tail;

    public AccessList(Access hd) {
        head = hd;
    }

    public AccessList(Access hd, AccessList tail) {
        head = hd;
        this.tail = tail;
    }

    public AccessList append(Access t) {
        if (this.tail == null) {
            return new AccessList(this.head, new AccessList(t));
        }
        return new AccessList(this.head, this.tail.append(t));
    }
}
