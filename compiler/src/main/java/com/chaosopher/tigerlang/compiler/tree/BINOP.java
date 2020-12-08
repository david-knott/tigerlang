package com.chaosopher.tigerlang.compiler.tree;

public class BINOP extends Exp {
    public int binop;
    public Exp left, right;

    public BINOP(int b, Exp l, Exp r) {
        binop = b;
        left = l;
        right = r;
    }

    public final static int PLUS = 0, MINUS = 1, MUL = 2, DIV = 3, AND = 4, OR = 5, LSHIFT = 6, RSHIFT = 7, ARSHIFT = 8,
            XOR = 9;

    public ExpList kids() {
        return new ExpList(left, new ExpList(right, null));
    }

    public Exp build(ExpList kids) {
        return new BINOP(binop, kids.head, kids.tail.head);
    }

    @Override
    public void accept(TreeVisitor treeVisitor) {
        treeVisitor.visit(this);
    }

    @Override
    public int getOperator() {
        return TreeKind.BINOP;
    }

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public IR getNthChild(int index) {
        switch (index) {
            case 0:
                return this.left;
            case 1:
                return this.right;
            default:
                throw new Error("Index out of range");
        }
    }
}
