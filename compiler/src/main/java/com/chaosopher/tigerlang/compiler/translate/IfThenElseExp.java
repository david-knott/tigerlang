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
        b = new Ex(new Tree.CONST(0));
    }

    /**
     * Returns the result where the then and else have the same type that is not
     * void.
     */
    @Override
    Tree.Exp unEx() {
        var r = Temp.create();
        return new Tree.ESEQ(
            new Tree.SEQ(
                new Tree.SEQ(
                    //evaluate test expression and just to true or false
                    testExp.unCx(trueLabel, falseLabel), 
                    new Tree.SEQ(
                        new Tree.LABEL(trueLabel), // add label t, evaluate expression a put resuult into register r.
                        new Tree.SEQ( // eval then expression
                                new Tree.MOVE( // move ex result into r
                                    new Tree.TEMP(r), 
                                    a.unEx()
                                ),
                                new Tree.SEQ(
                                    new Tree.JUMP(joinLabel),  //go to join label
                                    new Tree.SEQ(
                                        new Tree.LABEL(falseLabel), // add label f
                                        new Tree.SEQ(new Tree.MOVE( // eval expression b put result into register r
                                                new Tree.TEMP(r), 
                                                b.unEx() // into register r
                                        ), 
                                        new Tree.JUMP(joinLabel)) //jump to join label.
                                    )
                                )
                        )
                    )
                ), 
                new Tree.LABEL(joinLabel) //join label
            ), 
            new Tree.TEMP(r) //return temp to to calling expression.
        );
    }

    @Override
    Stm unNx() {
        return new Tree.SEQ(
            new Tree.SEQ(
                testExp.unCx(trueLabel, falseLabel), // eval cx with labels t and f
                new Tree.SEQ(
                    new Tree.LABEL(trueLabel), // add label t
                    new Tree.SEQ( // eval then express
                        a.unNx(), 
                        new Tree.SEQ(
                            new Tree.JUMP(joinLabel), 
                            new Tree.SEQ(
                                new Tree.LABEL(falseLabel), // add label
                                    new Tree.SEQ(
                                        b.unNx(), // into register r
                                        new Tree.JUMP(joinLabel))
                            )
                        )
                    )
                )
            ),
            new Tree.LABEL(joinLabel)
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
        return new Tree.SEQ(
            testExp.unCx(trueLabel, falseLabel), // eval test expression (AND: if (a > b) then (b > c) else (0) )
            new Tree.SEQ(
                new Tree.LABEL(trueLabel), // add label t
                    new Tree.SEQ( // eval then express
                        a.unCx(tt, ff), 
                        new Tree.SEQ(
                                new Tree.LABEL(falseLabel), // add
                                b.unCx(tt, ff) 
                            )
                        )
                    )
        );
    }
}