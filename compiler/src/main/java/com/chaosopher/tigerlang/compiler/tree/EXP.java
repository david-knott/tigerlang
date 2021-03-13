package com.chaosopher.tigerlang.compiler.tree;

/**
 * An expression that does not return
 * a result.
 */
public class EXP extends Stm {
    public Exp exp;

    public EXP(Exp e) {
        exp = e;
    }

    public ExpList kids() {
        return new ExpList(exp, null);
    }

    public Stm build(ExpList kids) {
        return new EXP(kids.head);
    }

    @Override
    public void accept(TreeVisitor treeVisitor) {
        treeVisitor.visit(this);
    }

    @Override
    public int getOperator() {
        return TreeKind.SXP;
    }

    @Override
    public int getArity() {
        return 1;
    }

    @Override
    public IR getNthChild(int index) {
        return this.exp;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof EXP) {
            return
            ((EXP)obj).exp.equals(this.exp)
            ;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + this.exp.hashCode();
        return result;
    }


    @Override
    public String toString() {
        return String.format("sxp: { exp : %s }", this.exp);
    }
}
