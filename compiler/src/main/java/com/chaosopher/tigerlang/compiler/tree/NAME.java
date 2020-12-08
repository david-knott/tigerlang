package com.chaosopher.tigerlang.compiler.tree;

import com.chaosopher.tigerlang.compiler.temp.Label;

public class NAME extends Exp {
    public Label label;

    public NAME(Label l) {
        label = l;
    }

    public ExpList kids() {
        return null;
    }

    public Exp build(ExpList kids) {
        return this;
    }

    @Override
    public void accept(TreeVisitor treeVisitor) {
        treeVisitor.visit(this);
    }

    @Override
    public int getOperator() {
        return TreeKind.NAME;
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
