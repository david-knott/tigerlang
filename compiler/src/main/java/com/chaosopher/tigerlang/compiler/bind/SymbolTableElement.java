package com.chaosopher.tigerlang.compiler.bind;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.types.Type;

class SymbolTableElement {
    final Type type;
    final Absyn exp;

    public SymbolTableElement(Type type) {
        this.type = type;
        this.exp = null;
    }

    public SymbolTableElement(Type type, Absyn exp) {
        this.type = type;
        this.exp = exp;
    }
}