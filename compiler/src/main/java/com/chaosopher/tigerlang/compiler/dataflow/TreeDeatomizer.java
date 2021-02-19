package com.chaosopher.tigerlang.compiler.dataflow;

import java.util.Hashtable;

import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.Exp;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.TEMP;

public class TreeDeatomizer extends CloningTreeVisitor {

    /**
     * This hashtable contains all the temporaries that
     * were created during the atomisation of the orignal
     * HIR tree.
     */
    private final Hashtable<Temp, Exp> temps;
    private final Hashtable<Temp, Exp> sources = new Hashtable<>();

    public TreeDeatomizer(Hashtable<Temp, Exp> temps) {
        this.temps = temps;
    }

    
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
}