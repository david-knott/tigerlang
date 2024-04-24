package com.chaosopher.tigerlang.compiler.tree;

import com.chaosopher.tigerlang.compiler.util.Assert;

public class ESEQ extends Exp {
    public Stm stm;
    public Exp exp;

    public ESEQ(Stm s, Exp e) {
        Assert.assertNotNull(s);
        Assert.assertNotNull(e);
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

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ESEQ) {
            return
            ((ESEQ)obj).exp.equals(this.exp)
            && ((ESEQ)obj).stm.equals(this.stm)
            ;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + this.exp.hashCode();
        result = 31 * result + this.stm.hashCode();
        return result;
    }
}
