package com.chaosopher.tigerlang.compiler.findescape;

import com.chaosopher.tigerlang.compiler.absyn.VarDec;

class FormalEscape extends Escape {
    com.chaosopher.tigerlang.compiler.absyn.VarDec fl;

    FormalEscape(int d, VarDec f) {
        depth = d;
        fl = f;
        fl.escape = false;
    }

    void setEscape() {
        fl.escape = true;
    }
}