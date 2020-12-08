package com.chaosopher.tigerlang.compiler.tree;

public class MOVE extends Stm {
    public Exp dst, src;

    public MOVE(Exp d, Exp s) {
        dst = d;
        src = s;
    }

    public ExpList kids() {
        if (dst instanceof MEM)
            return new ExpList(((MEM) dst).exp, new ExpList(src, null));
        else
            return new ExpList(src, null);
    }

    public Stm build(ExpList kids) {
        if (dst instanceof MEM)
            return new MOVE(new MEM(kids.head), kids.tail.head);
        else
            return new MOVE(dst, kids.head);
    }

    @Override
    public void accept(TreeVisitor treeVisitor) {
        treeVisitor.visit(this);
    }

    @Override
    public int getOperator() {
        return TreeKind.MOVE;
    }

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public IR getNthChild(int index) {
        switch (index) {
            case 0:
                return this.dst;
            case 1:
                return this.src;
            default:
                throw new Error("Index out of range");
        }
    }
}
