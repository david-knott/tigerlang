package com.chaosopher.tigerlang.compiler.assem;

public class InstrListList {
    public InstrList head;
    public InstrListList tail;

    public InstrListList(InstrList h, InstrListList t) {
        head = h;
        tail = t;
    }

    public void append(InstrList list) {
        var end = this;
        for (; end.tail != null; end = end.tail)
            ;
        end.tail = new InstrListList(list, null);

    }
}