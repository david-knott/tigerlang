package com.chaosopher.tigerlang.compiler.tree;

public class ESEQ extends Exp {
    public Stm stm;
    public Exp exp;

    public ESEQ(Stm s, Exp e) {
        stm = s;
        exp = e;
    }

    public ExpList kids() {
        throw new Error("kids() not applicable to ESEQ");
    }

    public Exp build(ExpList kids) {
        throw new Error("build() not applicable to ESEQ");
    }

    @Override
    public void accept(TreeVisitor treeVisitor) {
        treeVisitor.visit(this);
    }

    @Override
    public int getOperator() {
        throw new Error();
    }

    @Override
    public int getArity() {
        throw new Error();
    }

    @Override
    public IR getNthChild(int index) {
        throw new Error();
    }
}
