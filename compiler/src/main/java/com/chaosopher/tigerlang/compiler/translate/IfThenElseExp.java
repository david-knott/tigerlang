package com.chaosopher.tigerlang.compiler.translate;

import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.Stm;

/**
 * A subclass to handle if then else expressions. Where condition is the if
 * condition, aa is the expression in the then block and bb is the optional
 * expression in the else block
 * 
 * IR Code
 */
class IfThenElseExp extends Exp {

    Exp testExp, a, b;
    // begining of the the clause
    Label trueLabel = new Label();
    // begining of the else clause
    Label falseLabel = new Label();
    // both branches jump to this when the complete
    Label joinLabel = new Label();

    IfThenElseExp(Exp tst, Exp aa, Exp bb) {
        testExp = tst;
        a = aa;
        b = bb;
        if(b == null)
        b = new Ex(new com.chaosopher.tigerlang.compiler.tree.CONST(0));
    }

    /**
     * Returns the result where the then and else have the same type that is not
     * void.
     */
    @Override
    com.chaosopher.tigerlang.compiler.tree.Exp unEx() {
        var r = Temp.create();
        return new com.chaosopher.tigerlang.compiler.tree.ESEQ(
            new com.chaosopher.tigerlang.compiler.tree.SEQ(
                new com.chaosopher.tigerlang.compiler.tree.SEQ(
                    //evaluate test expression and just to true or false
                    testExp.unCx(trueLabel, falseLabel), 
                    new com.chaosopher.tigerlang.compiler.tree.SEQ(
                        new com.chaosopher.tigerlang.compiler.tree.LABEL(trueLabel), // add label t, evaluate expression a put resuult into register r.
                        new com.chaosopher.tigerlang.compiler.tree.SEQ( // eval then expression
                                new com.chaosopher.tigerlang.compiler.tree.MOVE( // move ex result into r
                                    new com.chaosopher.tigerlang.compiler.tree.TEMP(r), 
                                    a.unEx()
                                ),
                                new com.chaosopher.tigerlang.compiler.tree.SEQ(
                                    new com.chaosopher.tigerlang.compiler.tree.JUMP(joinLabel),  //go to join label
                                    new com.chaosopher.tigerlang.compiler.tree.SEQ(
                                        new com.chaosopher.tigerlang.compiler.tree.LABEL(falseLabel), // add label f
                                        new com.chaosopher.tigerlang.compiler.tree.SEQ(new com.chaosopher.tigerlang.compiler.tree.MOVE( // eval expression b put result into register r
                                                new com.chaosopher.tigerlang.compiler.tree.TEMP(r), 
                                                b.unEx() // into register r
                                        ), 
                                        new com.chaosopher.tigerlang.compiler.tree.JUMP(joinLabel)) //jump to join label.
                                    )
                                )
                        )
                    )
                ), 
                new com.chaosopher.tigerlang.compiler.tree.LABEL(joinLabel) //join label
            ), 
            new com.chaosopher.tigerlang.compiler.tree.TEMP(r) //return temp to to calling expression.
        );
    }

    @Override
    Stm unNx() {
        return new com.chaosopher.tigerlang.compiler.tree.SEQ(
            new com.chaosopher.tigerlang.compiler.tree.SEQ(
                testExp.unCx(trueLabel, falseLabel), // eval cx with labels t and f
                new com.chaosopher.tigerlang.compiler.tree.SEQ(
                    new com.chaosopher.tigerlang.compiler.tree.LABEL(trueLabel), // add label t
                    new com.chaosopher.tigerlang.compiler.tree.SEQ( // eval then express
                        a.unNx(), 
                        new com.chaosopher.tigerlang.compiler.tree.SEQ(
                            new com.chaosopher.tigerlang.compiler.tree.JUMP(joinLabel), 
                            new com.chaosopher.tigerlang.compiler.tree.SEQ(
                                new com.chaosopher.tigerlang.compiler.tree.LABEL(falseLabel), // add label
                                    new com.chaosopher.tigerlang.compiler.tree.SEQ(
                                        b.unNx(), // into register r
                                        new com.chaosopher.tigerlang.compiler.tree.JUMP(joinLabel))
                            )
                        )
                    )
                )
            ),
            new com.chaosopher.tigerlang.compiler.tree.LABEL(joinLabel)
        );
    }

    /**
     * This handle the case when an IF is being used in an IF.
     * 
     * Used by & and | operators. Label tt is the position where conditions in else
     * or then must jump if they evaluate to true. Label ff is the position where
     * code must jump to if expression evaluates to false.
     * 
     * In the case of & we have an expression of the form
     * if a > b & c > d then 1 else 0
     * This translates to
     * if ( if a > b then c > d else 0 ) 
     * Which is -> if (a > b) then eval(c > d) ( 1: 0) else 0
     * the sub if returns either 0 or 1 => use unEx for cond
     * 
     * test.Uncx - relop ( a > b ) if true jump to then relop ( c > b ) if false put 0 into temp
     * then.uncx - relop ( c > b ) if true put 0 into temp if false put 1 into temp
     * 
     * trueLabel and falseLabel are labels to the then and else expressions ( a > b /  c > b)
     * tt and ff are where a > b / c > b must jump to if they are true or false
     */
    @Override
    Stm unCx(Label tt, Label ff) {
        return new com.chaosopher.tigerlang.compiler.tree.SEQ(
            testExp.unCx(trueLabel, falseLabel), // eval test expression (AND: if (a > b) then (b > c) else (0) )
            new com.chaosopher.tigerlang.compiler.tree.SEQ(
                new com.chaosopher.tigerlang.compiler.tree.LABEL(trueLabel), // add label t
                    new com.chaosopher.tigerlang.compiler.tree.SEQ( // eval then express
                        a.unCx(tt, ff), 
                        new com.chaosopher.tigerlang.compiler.tree.SEQ(
                                new com.chaosopher.tigerlang.compiler.tree.LABEL(falseLabel), // add
                                b.unCx(tt, ff) 
                            )
                        )
                    )
        );
    }
}