package com.chaosopher.tigerlang.compiler.canon;

import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.temp.LabelFactory;
import com.chaosopher.tigerlang.compiler.tree.CJUMP;
import com.chaosopher.tigerlang.compiler.tree.JUMP;
import com.chaosopher.tigerlang.compiler.tree.LABEL;
import com.chaosopher.tigerlang.compiler.tree.SEQ;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;

class TraceSchedule {

    public StmList stms;
    BasicBlocks theBlocks;
    private LabelFactory labelFactory;

    public TraceSchedule() {
        this.labelFactory = new LabelFactory();
    }

    public Stm getTraces() {
        SEQ seqs = null;
        for(StmList stmList = this.stms; stmList != null; stmList = stmList.tail) {
            seqs = new SEQ(stmList.head, seqs);
        }
        return seqs;
    }

    java.util.Dictionary<Label, StmList> table = new java.util.Hashtable<>();

    StmList getLast(StmList block) {
        StmList l = block;
        while (l.tail.tail != null)
            l = l.tail;
        return l;
    }

    void trace(StmList l) {

        for (;;) {
            LABEL lab = (LABEL) l.head;
            table.remove(lab.label);
            StmList last = getLast(l);
            Stm s = last.tail.head;
            if (s instanceof JUMP) {
                JUMP j = (JUMP) s;
                StmList target = (StmList) table.get(j.targets.head);
                if (j.targets.tail == null && target != null) {
                    last.tail = target;
                    l = target;
                } else {
                    last.tail.tail = getNext();
                    return;
                }
            } else if (s instanceof CJUMP) {
                CJUMP j = (CJUMP) s;
                StmList t = (StmList) table.get(j.iftrue);
                StmList f = (StmList) table.get(j.iffalse);
                if (f != null) {
                    last.tail.tail = f;
                    l = f;
                } else if (t != null) {
                    last.tail.head = new CJUMP(CJUMP.notRel(j.relop), j.left, j.right, j.iffalse, j.iftrue);
                    last.tail.tail = t;
                    l = t;
                } else {
                    com.chaosopher.tigerlang.compiler.temp.Label ff = this.labelFactory.create();
                    last.tail.head = new CJUMP(j.relop, j.left, j.right, j.iftrue, ff);
                    last.tail.tail = new StmList(new LABEL(ff),
                            new StmList(new JUMP(j.iffalse), getNext()));
                    return;
                }
            } else
                throw new Error("Bad basic block in TraceSchedule");
        }
    }

    StmList getNext() {
        if (theBlocks.blocks == null)
            return new StmList(new LABEL(theBlocks.done), null);
        else {
            StmList s = theBlocks.blocks.head;
            LABEL lab = (LABEL) s.head;
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
            table.put(((LABEL) l.head.head).label, l.head);
        stms = getNext();
        table = null;
    }
}
