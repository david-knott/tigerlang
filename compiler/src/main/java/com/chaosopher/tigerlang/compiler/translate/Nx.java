package com.chaosopher.tigerlang.compiler.translate;

import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.tree.Stm;

/**
 * Represents an expression that does not yield a value, this is referred to as
 * a statement.
 */
class Nx extends Exp {
    Tree.Stm stm;

    Nx(Tree.Stm s) {
        stm = s;
    }

    @Override
    Tree.Exp unEx() {
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