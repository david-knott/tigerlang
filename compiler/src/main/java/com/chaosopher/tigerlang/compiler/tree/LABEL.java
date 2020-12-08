package com.chaosopher.tigerlang.compiler.tree;

import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.temp.Label;

public class LABEL extends Stm {
    public Label label;

    public LABEL(Label l) {
        label = l;
    }

    public ExpList kids() {
        return null;
    }

    public Stm build(ExpList kids) {
        return this;
    }

    @Override
    public void accept(TreeVisitor treeVisitor) {
        treeVisitor.visit(this);

    }

    @Override
    public int getOperator() {
        return TreeKind.LABEL;
    }

    @Override
    public int getArity() {
        return 0;
    }

    @Override
    public IR getNthChild(int index) {
        throw new Error("Not supported");
    }
}
