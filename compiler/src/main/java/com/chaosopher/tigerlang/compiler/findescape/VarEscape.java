package com.chaosopher.tigerlang.compiler.findescape;

class VarEscape extends Escape {
    com.chaosopher.tigerlang.compiler.absyn.VarDec vd;

    VarEscape(int d, com.chaosopher.tigerlang.compiler.absyn.VarDec v) {
        depth = d;
        vd = v;
        vd.escape = false;
    }

    void setEscape() {
        vd.escape = true;
    }
}