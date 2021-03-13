package com.chaosopher.tigerlang.compiler.tree;

abstract public class Exp extends IR {
    abstract public ExpList kids();

    abstract public Exp build(ExpList kids);

    @Override
    public int getOperator() {
        return TreeKind.EXP;
    }

}
