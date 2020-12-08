package com.chaosopher.tigerlang.compiler.translate;

import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.tree.Stm;

class Ex extends Exp {
    Tree.Exp exp;

    Ex(Tree.Exp e) {
        exp = e;
    }

    @Override
    Tree.Exp unEx() {
        return exp;
    }

    @Override
    Stm unNx() {
        return new Tree.EXP(exp);
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
        if(exp instanceof Tree.CONST){
            var c = (Tree.CONST)exp;
            if(c.value == 0){
                return new Tree.JUMP(f);
            }
            if(c.value == 1){
                return new Tree.JUMP(t);
            }
        }
        return new Tree.CJUMP(Tree.CJUMP.EQ, this.exp, new Tree.CONST(1), t, f);
    }
}