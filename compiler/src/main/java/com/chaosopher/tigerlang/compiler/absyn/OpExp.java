package com.chaosopher.tigerlang.compiler.absyn;

public class OpExp extends Exp {
    public final static int PLUS = 0, MINUS = 1, MUL = 2, DIV = 3, EQ = 4, NE = 5, LT = 6, LE = 7, GT = 8, GE = 9;
    public Exp left, right;
    public int oper;

    public OpExp(int p, Exp l, int o, Exp r) {
        pos = p;
        left = l;
        oper = o;
        right = r;
    }

    @Override
    public void accept(AbsynVisitor visitor) {
        visitor.visit(this);
    }
}
