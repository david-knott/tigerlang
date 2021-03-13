package com.chaosopher.tigerlang.compiler.tree;

/**
 * An integer constant
 */
public class CONST extends Exp {
    public int value;

    public CONST(int v) {
        value = v;
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
        return TreeKind.CONST;
    }

    @Override
    public int getArity() {
        return 0;
    }

    @Override
    public IR getNthChild(int index) {
        throw new Error("Illegal operations");
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CONST) {
            return
            ((CONST)obj).value == this.value
            ;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + this.value;
        return result;
    }


    @Override
    public String toString() {
        return String.format("const: { val: %d}", this.value);
    }
}
