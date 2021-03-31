package com.chaosopher.tigerlang.compiler.dataflow.utils;

import com.chaosopher.tigerlang.compiler.tree.BINOP;
import com.chaosopher.tigerlang.compiler.tree.DefaultTreeVisitor;
import com.chaosopher.tigerlang.compiler.tree.Exp;
import com.chaosopher.tigerlang.compiler.tree.MEM;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.Stm;

/**
 * Helper class to extract mem expressions from statements.
 */
public class ExtractMemExp extends DefaultTreeVisitor {

    public static boolean isMemExp(Stm stm) {
        ExtractMemExp extractExp = new ExtractMemExp();
        stm.accept(extractExp);
        return extractExp.exp != null;
    }

    private Exp exp;

    @Override
    public void visit(MOVE op) {
        op.src.accept(this);
    }

    @Override
    public void visit(BINOP op) {
        // ignore binop.
    }

    @Override
    public void visit(MEM op) {
        if(this.exp == null) {
            this.exp = op;
        } else {
            throw new Error("More than one MEM in this statement.");
        }
    }
}