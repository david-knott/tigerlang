package com.chaosopher.tigerlang.compiler.tree;

public class MEM extends Exp {
    public Exp exp;

    public MEM(Exp e) {
        exp = e;
    }

    public ExpList kids() {
        return new ExpList(exp, null);
    }

    public Exp build(ExpList kids) {
        return new MEM(kids.head);
    }

    @Override
    public void accept(TreeVisitor treeVisitor) {
        treeVisitor.visit(this);

    }

    @Override
    public int getOperator() {
        return TreeKind.MEM;
    }

    @Override
    public int getArity() {
        return 1;
    }

    @Override
    public IR getNthChild(int index) {
        return this.exp;
    }
}
