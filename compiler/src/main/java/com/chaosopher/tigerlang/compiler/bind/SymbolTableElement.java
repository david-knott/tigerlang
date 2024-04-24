package com.chaosopher.tigerlang.compiler.bind;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;

class SymbolTableElement {
    final Absyn exp;
    /*
    public SymbolTableElement(Type type) {
        this.type = type;
        this.exp = null;
    }
*/
    public SymbolTableElement(Absyn exp) {
        this.exp = exp;
    }
}