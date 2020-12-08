package com.chaosopher.tigerlang.compiler.translate;

import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.tree.Stm;

/**
 * This class is used to represent a conditional expression that yields a
 * boolean value. It is typically used in control flow structures such as loops
 * and branches. It can also be assigned to a variable.
 * 
 * IR Code is just a simple CJUMP to the supplied labels.
 */
class RelCx extends Cx {

    Tree.Exp right;
    Tree.Exp left;
    int operator;

    RelCx(Tree.Exp l, Tree.Exp r, int op) {
        right = r;
        left = l;
        operator = op;
    }

    /**
     * Applies operator to left and right expressions and
     * jumps to either t or f depending on result.
     */
    @Override
    Stm unCx(Label t, Label f) {
        return new Tree.CJUMP(operator, left, right, t, f);
    }
}