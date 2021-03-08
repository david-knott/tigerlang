package com.chaosopher.tigerlang.compiler.dataflow.cfg;

import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.temp.LabelList;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;

public class BasicBlock {

    public StmList first;
    public final Label label;
    public BasicBlock tail;
    public LabelList labelList;

    public BasicBlock(Stm first, Label label) {
//        this.first = new StmList(first);
        this.label = label;
    }

    public BasicBlock(Stm first, Label label, BasicBlock tail) {
        this.label = label;
        this.tail = tail;
    }

    public void addStatement(Stm stm) { 
        if(this.first == null) {
            this.first = new StmList(stm);
        } else {
            this.first.append(stm);
        }
    }

    @Override
    public String toString() {
        BasicBlock me = this;
        String result = "";
             result+= me.label.toString() + ",";
             StmList sl = me.first;
             while(sl.tail != null) {
             //   result+= sl.head.toString() + "\n";
                sl = sl.tail;
             }
             me = me.tail;
        return result;
    }
}