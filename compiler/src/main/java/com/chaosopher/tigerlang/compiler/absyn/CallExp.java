package com.chaosopher.tigerlang.compiler.absyn;

import com.chaosopher.tigerlang.compiler.symbol.Symbol;

public class CallExp extends Exp {
    public Symbol func;
    public ExpList args;
    public Absyn def;

    public CallExp(int p, Symbol f, ExpList a) {
        pos = p;
        func = f;
        args = a;
    }

    @Override
    public void accept(AbsynVisitor visitor) {
        visitor.visit(this);
    }

    public void setDef(Absyn exp) {
        this.def = exp;
    }
}