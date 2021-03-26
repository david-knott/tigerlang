package com.chaosopher.tigerlang.compiler.translate;

import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.tree.Stm;

/**
 * Represents a statement which does not yield a value.
 */
class NxContext extends TranslateContext {
    com.chaosopher.tigerlang.compiler.tree.Stm stm;

    NxContext(com.chaosopher.tigerlang.compiler.tree.Stm s) {
        stm = s;
    }

    @Override
    com.chaosopher.tigerlang.compiler.tree.Exp unEx() {
        throw new RuntimeException("Not implemented, this should never occur");
    }

    @Override
    Stm unNx() {
        return stm;
    }

    @Override
    Stm unCx(Label t, Label f) {
        throw new RuntimeException("Not implemented, this should never occur");
    }
}