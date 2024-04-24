package com.chaosopher.tigerlang.compiler.dataflow.dead;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.chaosopher.tigerlang.compiler.dataflow.live.Liveness;
import com.chaosopher.tigerlang.compiler.dataflow.utils.ExtractDefs;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.CloningTreeVisitor;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;

/**
 * Removes move statements where the definition temp is not used.
 */
public class DeadCodeRemoval extends CloningTreeVisitor {

    private final Liveness livenessDataFlow;
    private final List<Stm> deadCode = new ArrayList<>();

    public DeadCodeRemoval(final Liveness livenessDataFlow) {
        this.livenessDataFlow = livenessDataFlow;
    }

    /**
     * Examines move statements to see if they are dead. If a
     * move temp is never used, it can be removed.
     */
    @Override
    public void visit(MOVE op) {
        Set<Temp> defs = ExtractDefs.getDefs(op);
        assert defs.size() == 1;
        // check if defs are live out at statement exit
        if(!this.livenessDataFlow.getOut(op).containsAll(defs)) {
            this.deadCode.add(op);
        }
        // clone it
        super.visit(op);
    }

    /**
     * Removes stm that are dead.
     */
    @Override
    public void visit(StmList stmList) {
        StmList clone = null, temp = null;
        for(StmList next = stmList; next != null; next = next.tail) {
            next.head.accept(this);
            if(this.deadCode.contains(next.head)) {
                continue;
            }
            Stm cloned = this.getStm();
            if(clone == null) {
                clone = temp = new StmList(cloned);
            } else {
                temp.tail = (temp = new StmList(cloned));
            }
        }
        this.setStmList(clone);
    }
}
