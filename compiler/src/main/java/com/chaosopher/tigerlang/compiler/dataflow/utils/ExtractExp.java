package com.chaosopher.tigerlang.compiler.dataflow.utils;

import com.chaosopher.tigerlang.compiler.tree.BINOP;
import com.chaosopher.tigerlang.compiler.tree.DefaultTreeVisitor;
import com.chaosopher.tigerlang.compiler.tree.Exp;
import com.chaosopher.tigerlang.compiler.tree.MEM;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.Stm;

/**
 * Helper class to extract expressions from statements.
 */
public class ExtractExp extends DefaultTreeVisitor {

    public static Exp getExp(Stm stm) {
        ExtractExp extractExp = new ExtractExp();
        stm.accept(extractExp);
        return extractExp.exp;
    }

    private Exp exp;

    @Override
    public void visit(MOVE op) {
        op.src.accept(this);
    }

    @Override
    public void visit(BINOP op) {
        if(this.exp == null) {
            this.exp = op;
        } else {
            throw new Error("More than one BINOP or MEM in this statement.");
        }
    }

    @Override
    public void visit(MEM op) {
        if(this.exp == null) {
            this.exp = op;
        } else {
            throw new Error("More than one BINOP or MEM in this statement.");
        }
    }
}