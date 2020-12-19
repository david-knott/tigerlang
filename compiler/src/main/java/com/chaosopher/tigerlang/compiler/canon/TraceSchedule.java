package com.chaosopher.tigerlang.compiler.canon;

import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.tree.StmList;

class TraceSchedule {

    public com.chaosopher.tigerlang.compiler.tree.StmList stms;
    BasicBlocks theBlocks;

    public com.chaosopher.tigerlang.compiler.tree.Stm getTraces() {
        com.chaosopher.tigerlang.compiler.tree.SEQ seqs = null;
        for(StmList stmList = this.stms; stmList != null; stmList = stmList.tail) {
            seqs = new com.chaosopher.tigerlang.compiler.tree.SEQ(stmList.head, seqs);
        }
        return seqs;
    }

    java.util.Dictionary<Label, StmList> table = new java.util.Hashtable<>();

    com.chaosopher.tigerlang.compiler.tree.StmList getLast(com.chaosopher.tigerlang.compiler.tree.StmList block) {
        com.chaosopher.tigerlang.compiler.tree.StmList l = block;
        while (l.tail.tail != null)
            l = l.tail;
        return l;
    }

    void trace(com.chaosopher.tigerlang.compiler.tree.StmList l) {

        for (;;) {
            com.chaosopher.tigerlang.compiler.tree.LABEL lab = (com.chaosopher.tigerlang.compiler.tree.LABEL) l.head;
            table.remove(lab.label);
            com.chaosopher.tigerlang.compiler.tree.StmList last = getLast(l);
            com.chaosopher.tigerlang.compiler.tree.Stm s = last.tail.head;
            if (s instanceof com.chaosopher.tigerlang.compiler.tree.JUMP) {
                com.chaosopher.tigerlang.compiler.tree.JUMP j = (com.chaosopher.tigerlang.compiler.tree.JUMP) s;
                com.chaosopher.tigerlang.compiler.tree.StmList target = (com.chaosopher.tigerlang.compiler.tree.StmList) table.get(j.targets.head);
                if (j.targets.tail == null && target != null) {
                    last.tail = target;
                    l = target;
                } else {
                    last.tail.tail = getNext();
                    return;
                }
            } else if (s instanceof com.chaosopher.tigerlang.compiler.tree.CJUMP) {
                com.chaosopher.tigerlang.compiler.tree.CJUMP j = (com.chaosopher.tigerlang.compiler.tree.CJUMP) s;
                com.chaosopher.tigerlang.compiler.tree.StmList t = (com.chaosopher.tigerlang.compiler.tree.StmList) table.get(j.iftrue);
                com.chaosopher.tigerlang.compiler.tree.StmList f = (com.chaosopher.tigerlang.compiler.tree.StmList) table.get(j.iffalse);
                if (f != null) {
                    last.tail.tail = f;
                    l = f;
                } else if (t != null) {
                    last.tail.head = new com.chaosopher.tigerlang.compiler.tree.CJUMP(com.chaosopher.tigerlang.compiler.tree.CJUMP.notRel(j.relop), j.left, j.right, j.iffalse, j.iftrue);
                    last.tail.tail = t;
                    l = t;
                } else {
                    com.chaosopher.tigerlang.compiler.temp.Label ff = new com.chaosopher.tigerlang.compiler.temp.Label();
                    last.tail.head = new com.chaosopher.tigerlang.compiler.tree.CJUMP(j.relop, j.left, j.right, j.iftrue, ff);
                    last.tail.tail = new com.chaosopher.tigerlang.compiler.tree.StmList(new com.chaosopher.tigerlang.compiler.tree.LABEL(ff),
                            new com.chaosopher.tigerlang.compiler.tree.StmList(new com.chaosopher.tigerlang.compiler.tree.JUMP(j.iffalse), getNext()));
                    return;
                }
            } else
                throw new Error("Bad basic block in TraceSchedule");
        }
    }

    com.chaosopher.tigerlang.compiler.tree.StmList getNext() {
        if (theBlocks.blocks == null)
            return new com.chaosopher.tigerlang.compiler.tree.StmList(new com.chaosopher.tigerlang.compiler.tree.LABEL(theBlocks.done), null);
        else {
            com.chaosopher.tigerlang.compiler.tree.StmList s = theBlocks.blocks.head;
            com.chaosopher.tigerlang.compiler.tree.LABEL lab = (com.chaosopher.tigerlang.compiler.tree.LABEL) s.head;
            if (table.get(lab.label) != null) {
                trace(s);
                return s;
            } else {
                theBlocks.blocks = theBlocks.blocks.tail;
                return getNext();
            }
        }
    }

    public TraceSchedule(BasicBlocks b) {
        theBlocks = b;
        for (StmListList l = b.blocks; l != null; l = l.tail)
            table.put(((com.chaosopher.tigerlang.compiler.tree.LABEL) l.head.head).label, l.head);
        stms = getNext();
        table = null;
    }
}
