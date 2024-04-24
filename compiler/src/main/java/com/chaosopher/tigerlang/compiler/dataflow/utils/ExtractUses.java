package com.chaosopher.tigerlang.compiler.dataflow.utils;

import java.util.HashSet;
import java.util.Set;

import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.DefaultTreeVisitor;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.TEMP;

/**
 * Helper class to extract temporaries that are r-values.
 */
public class ExtractUses extends DefaultTreeVisitor {

    private final Set<Temp> uses = new HashSet<>();

    public static Set<Temp> getUses(Stm stm) {
        ExtractUses extracUses = new ExtractUses();
        stm.accept(extracUses);
        return extracUses.uses;
    }

    @Override
    public void visit(MOVE op) {
        op.src.accept(this);
        if(!(op.dst instanceof TEMP)) {
            op.dst.accept(this);
        }
    }

    @Override
    public void visit(TEMP op) {
        this.uses.add(op.temp);
    }
}