package com.chaosopher.tigerlang.compiler.dataflow;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.BINOP;
import com.chaosopher.tigerlang.compiler.tree.CALL;
import com.chaosopher.tigerlang.compiler.tree.CJUMP;
import com.chaosopher.tigerlang.compiler.tree.CloningTreeVisitor;
import com.chaosopher.tigerlang.compiler.tree.Exp;
import com.chaosopher.tigerlang.compiler.tree.JUMP;
import com.chaosopher.tigerlang.compiler.tree.MEM;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.tree.TEMP;

public class TreeDeatomizer extends CloningTreeVisitor {

    private final HashMap<Temp, Exp> defs = new HashMap<>();
    private final HashSet<Stm> delete = new HashSet<>();
    private final HashMap<Temp, Stm> defStatement = new HashMap<>();

    public TreeDeatomizer(Hashtable<Temp, Exp> temps) {

    private Exp replace(Exp left) {
        Exp original = this.exp;
        if(left instanceof TEMP) {
            Temp t = ((TEMP)left).temp;
            if(this.defs.containsKey(t)) {
                left = this.defs.get(t);
                this.delete.add(this.defStatement.get(t));
                this.defStatement.remove(t);
                this.defs.remove(t);
            }
        }
        left.accept(this);
        Exp result = this.exp;
        this.exp = original;
        return result;
    }

    @Override
    public void visit(BINOP op) {
        this.replace(op.left).accept(this);
        Exp leftClone = this.exp;
        this.replace(op.right).accept(this);
        Exp rightClone = this.exp;
        this.exp = new BINOP(op.binop, leftClone, rightClone);
    }

    @Override
    public void visit(MOVE op) {
        if(op.dst instanceof TEMP && (op.src instanceof BINOP || op.src instanceof MEM)) {
            // get dst temp
            Temp tempDst = ((TEMP)op.dst).temp;
            // store entry for dest temp -> src expression.
            this.defs.put(tempDst, op.src);
            // store entry for dest temp -> move operation.
            this.defStatement.put(tempDst, op);
        }
        op.accept(this);
    }

/*
    @Override
    public void visit(MOVE op) {
        // if destination is a temp in our list,
        // drop the move, but store its source
        if(op.dst instanceof TEMP) {
            TEMP dstTemp = (TEMP)op.dst;
            if(this.temps.containsKey(dstTemp.temp)) {
                this.sources.put(dstTemp.temp, op.src);
                // set stm to null so its not added to statement list
                this.stm = null;
                return;
            }
        }
        super.visit(op);
    }

    @Override
    public void visit(TEMP op) {
        if(this.temps.containsKey(op.temp)) {
            // clone expression by visiting it
            // which will also add it to the 
            // tree instead of the original temp.
            Exp exp = this.sources.get(op.temp);
            this.temps.remove(op.temp);
            exp.accept(this);
        } else {
            super.visit(op);
        }
    }
*/
    /**
     * Reverses the statement list.
     * @param source
     * @return
     */
    private StmList reverse(StmList source) {
        StmList reversed = new StmList(source.head);
        for(; source.tail != null; source = source.tail) {
            reversed = new StmList(source.head, reversed);
        }
        return reversed;
    }

    @Override
    public void visit(StmList stmList) {
        StmList cloned = null;
        for(;stmList != null; stmList = stmList.tail) {
            stmList.head.accept(this);
            Stm clonedStm = this.stm;
            // check if the original stm was marked for deletion.
            if(!this.delete.contains(stmList.head)) {
                cloned = StmList.append(cloned, clonedStm);
            }
        }
        this.stm = cloned;
    }
}