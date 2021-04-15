package com.chaosopher.tigerlang.compiler.dataflow.utils;

import java.util.HashSet;
import java.util.Set;

import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.CALL;
import com.chaosopher.tigerlang.compiler.tree.CJUMP;
import com.chaosopher.tigerlang.compiler.tree.DefaultTreeVisitor;
import com.chaosopher.tigerlang.compiler.tree.MEM;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.TEMP;

/**
 * Helper class to extract temporary definition, ie l-values.
 */
public class ExtractDefs extends DefaultTreeVisitor {

    public static Set<Temp> getDefs(Stm stm) {
        ExtractDefs extractDefs = new ExtractDefs();
        stm.accept(extractDefs);
        return extractDefs.defs;
    }

    private final Set<Temp> defs = new HashSet<>();

    @Override
    public void visit(MOVE op) {
        op.dst.accept(this);
    }

    @Override
    public void visit(CJUMP cjump) {
        // do nothing as this doesn't define temporaries
    }

    @Override
    public void visit(CALL op) {
        // do nothing as this doesn't define temporaries
    }

    @Override
    public void visit(MEM op) {
        // do nothing as this doesn't define temporaries
    }

    @Override
    public void visit(TEMP op) {
        this.defs.add(op.temp);
    }

    public Set<Temp> getDefs() {
        return this.defs;
    }
}