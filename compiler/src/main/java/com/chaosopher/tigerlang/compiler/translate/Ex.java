package com.chaosopher.tigerlang.compiler.translate;

import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.tree.Stm;

class Ex extends Exp {
    com.chaosopher.tigerlang.compiler.tree.Exp exp;

    Ex(com.chaosopher.tigerlang.compiler.tree.Exp e) {
        exp = e;
    }

    @Override
    com.chaosopher.tigerlang.compiler.tree.Exp unEx() {
        return exp;
    }

    @Override
    Stm unNx() {
        return new com.chaosopher.tigerlang.compiler.tree.EXP(exp);
    }

    /**
     * Converts an expression that yields
     * a result into a conditional that
     * jumps to true or false depending
     * on whether expresion evalues to 1 or
     * not one.
     */
    @Override
    Stm unCx(Label t, Label f) {
        if(exp instanceof com.chaosopher.tigerlang.compiler.tree.CONST){
            var c = (com.chaosopher.tigerlang.compiler.tree.CONST)exp;
            if(c.value == 0){
                return new com.chaosopher.tigerlang.compiler.tree.JUMP(f);
            }
            if(c.value != 0){
               return new com.chaosopher.tigerlang.compiler.tree.JUMP(t);
            }
        }
        return new com.chaosopher.tigerlang.compiler.tree.CJUMP(com.chaosopher.tigerlang.compiler.tree.CJUMP.EQ, this.exp, new com.chaosopher.tigerlang.compiler.tree.CONST(1), t, f);
    }
}