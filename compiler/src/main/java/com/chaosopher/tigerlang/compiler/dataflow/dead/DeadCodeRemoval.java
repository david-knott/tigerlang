package com.chaosopher.tigerlang.compiler.dataflow.dead;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.chaosopher.tigerlang.compiler.dataflow.live.LivenessDataFlow;
import com.chaosopher.tigerlang.compiler.dataflow.utils.ExtractDefs;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.DefaultTreeVisitor;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;

/**
 * Removes move statements where the definition temp is not used.
 */
public class DeadCodeRemoval extends DefaultTreeVisitor {

    private final LivenessDataFlow livenessDataFlow;
    private final List<Stm> deadCode = new ArrayList<>();

    public DeadCodeRemoval(final LivenessDataFlow livenessDataFlow) {
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
        if(this.livenessDataFlow.getOut(op).containsAll(defs)) {
            this.deadCode.add(op);
        }
        super.visit(op);
    }

    /**
     * Removes stm that are dead.
     */
    @Override
    public void visit(StmList stmList) {
        // remove statements in deadCode

        // need to call the visit methods.
        StmList previous = stmList;
        StmList next = stmList.tail;
        for(; next != null; next = next.tail) {
            if(this.deadCode.contains(next.head)) {
                previous.tail = next.tail;
            } else {
                previous = next;
            }
        }
    }
}
