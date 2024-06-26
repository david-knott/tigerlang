package com.chaosopher.tigerlang.compiler.tree;

import com.chaosopher.tigerlang.compiler.temp.Label;

public class CJUMP extends Stm {
    public int relop;
    public Exp left, right;
    public Label iftrue, iffalse;

    public CJUMP(int rel, Exp l, Exp r, Label t, Label f) {
        relop = rel;
        left = l;
        right = r;
        iftrue = t;
        iffalse = f;
    }

    public final static int EQ = 0, NE = 1, LT = 2, GT = 3, LE = 4, GE = 5, ULT = 6, ULE = 7, UGT = 8, UGE = 9;

    public ExpList kids() {
        return new ExpList(left, new ExpList(right, null));
    }

    public Stm build(ExpList kids) {
        return new CJUMP(relop, kids.head, kids.tail.head, iftrue, iffalse);
    }

    public static int notRel(int relop) {
        switch (relop) {
            case EQ:
                return NE;
            case NE:
                return EQ;
            case LT:
                return GE;
            case GE:
                return LT;
            case GT:
                return LE;
            case LE:
                return GT;
            case ULT:
                return UGE;
            case UGE:
                return ULT;
            case UGT:
                return ULE;
            case ULE:
                return UGT;
            default:
                throw new Error("bad relop in CJUMP.notRel");
        }
    }

    @Override
    public void accept(TreeVisitor treeVisitor) {
        treeVisitor.visit(this);
    }

    @Override
    public int getOperator() {
        return TreeKind.CJUMP;
    }

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CJUMP) {
            return
            ((CJUMP)obj).relop == this.relop
            && ((CJUMP)obj).left.equals(this.left)
            && ((CJUMP)obj).right.equals(this.right)
            && ((CJUMP)obj).iftrue.equals(this.iftrue)
            && ((CJUMP)obj).iffalse.equals(this.iffalse)
            ;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + this.relop;
        result = 31 * result + this.left.hashCode();
        result = 31 * result + this.right.hashCode();
        result = 31 * result + this.iftrue.hashCode();
        result = 31 * result + this.iffalse.hashCode();
        return result;
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

    @Override
    public String toString() {
        return String.format("cjump: { relop: %d,  left: %s, right %s, iftrue: %s, iffalse: %s}", this.relop, this.left, this.right, this.iftrue, this.iffalse);
    }
}
