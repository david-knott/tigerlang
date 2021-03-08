package com.chaosopher.tigerlang.compiler.tree;

/**
 * Temporary is abstract machine similar to a register in a real machine.
 */
public class TEMP extends Exp {
    public com.chaosopher.tigerlang.compiler.temp.Temp temp;

    public TEMP(com.chaosopher.tigerlang.compiler.temp.Temp t) {
        temp = t;
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
        return TreeKind.TEMP;
    }

    @Override
    public int getArity() {
        return 0;
    }

    @Override
    public IR getNthChild(int index) {
        throw new Error("Not supported");
    }

    public String toString() {
        return String.format("temp : { temp : %s }", temp.toString());
    }
}
