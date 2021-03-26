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
class RelCxContext extends CxContext {

    com.chaosopher.tigerlang.compiler.tree.Exp right;
    com.chaosopher.tigerlang.compiler.tree.Exp left;
    int operator;

    RelCxContext(com.chaosopher.tigerlang.compiler.tree.Exp l, com.chaosopher.tigerlang.compiler.tree.Exp r, int op) {
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
        return new com.chaosopher.tigerlang.compiler.tree.CJUMP(operator, left, right, t, f);
    }
}