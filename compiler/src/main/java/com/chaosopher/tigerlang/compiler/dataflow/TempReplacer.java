package com.chaosopher.tigerlang.compiler.dataflow;

import java.util.Hashtable;

import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.Exp;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.tree.TEMP;

/**
 * Examines each statement, if we find a statement that defines a temp,
 * store its right expression and yank it out of the list. If we find a statement that uses
 * a temp, replace that temp with the stored expression.
 */
class TempReplacer extends CloningTreeVisitor {

    private final TreeAtomizer atomizer;
    private Hashtable<Stm, Stm> removals = new Hashtable<>();
    private Hashtable<Temp,Exp> expReplacements = new Hashtable<>();
    private StmList stmList;

    public StmList getStmList() {
        return this.stmList;
    }

    public TempReplacer(TreeAtomizer atomizer) {
        this.atomizer = atomizer;
    }
     
    @Override
    public void visit(MOVE op) {
        super.visit(op);
        MOVE clonedMove = (MOVE)this.stm;
        if(clonedMove.dst instanceof TEMP && atomizer.contains(((TEMP)clonedMove.dst).temp)) {
            Exp clonedExp = clonedMove.src; 
            this.expReplacements.put(((TEMP)clonedMove.dst).temp, clonedExp);
            // put in the orignal move into the removals list
            this.removals.put(op, op);
        }
    }

    @Override
    public void visit(TEMP op) {
        if(this.expReplacements.containsKey(op.temp)) {
           this.exp = this.expReplacements.get(op.temp); 
        } else {
            super.visit(op);
        }
    }

    @Override
    public void visit(StmList stmList) {
        StmList cloned = null;
        for(;stmList != null; stmList = stmList.tail) {
            stmList.head.accept(this);
            if(this.removals.containsKey(stmList.head)) {
                this.removals.remove(stmList.head);
            } else {
                Stm clonedStm = this.stm;
                cloned = StmList.append(cloned, clonedStm);
            }
        }
        this.stmList = cloned;
    }
}