package com.chaosopher.tigerlang.compiler.translate;

import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.temp.LabelFactory;
import com.chaosopher.tigerlang.compiler.tree.Stm;

class ExContext extends TranslateContext {
    com.chaosopher.tigerlang.compiler.tree.Exp exp;

    ExContext(LabelFactory labelFactory, com.chaosopher.tigerlang.compiler.tree.Exp e) {
        super(labelFactory);
        exp = e;
    }

    @Override
    com.chaosopher.tigerlang.compiler.tree.Exp unEx() {
        return this.exp;
    }

    @Override
    Stm unNx() {
        return new com.chaosopher.tigerlang.compiler.tree.EXPS(exp);
    }

    /**
     * Converts an expression that yields
     * a result into a conditional that
     * jumps to true or false depending
     * on whether expresion evalues to 1 or
     * not.
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