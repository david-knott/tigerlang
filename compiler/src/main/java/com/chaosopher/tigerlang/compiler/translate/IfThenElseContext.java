package com.chaosopher.tigerlang.compiler.translate;

import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.temp.LabelFactory;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.CONST;
import com.chaosopher.tigerlang.compiler.tree.ESEQ;
import com.chaosopher.tigerlang.compiler.tree.JUMP;
import com.chaosopher.tigerlang.compiler.tree.LABEL;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.SEQ;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.TEMP;

/**
 * A subclass to handle 'if then else' expressions. Where condition is the if
 * condition, aa is the expression in the then block and bb is the optional
 * expression in the else block
 * 
 * IR Code
 */
class IfThenElseContext extends TranslateContext {

    TranslateContext testExp, a, b;
    // begining of the the clause
    Label trueLabel = this.labelFactory.create();
    // begining of the else clause
    Label falseLabel = this.labelFactory.create();
    // both branches jump to this when they complete
    Label joinLabel = this.labelFactory.create();
    
    /**
     * Constructor for creating a new instance.
     * @param tst
     * @param aa
     * @param bb
     */
    IfThenElseContext(LabelFactory labelFactory, TranslateContext tst, TranslateContext aa, TranslateContext bb) {
        super(labelFactory);
        testExp = tst;
        a = aa;
        b = bb;
        if(b == null)
        b = new ExContext(this.labelFactory, new CONST(0));
    }

    /**
     * Returns the result where the then and else have the same type that is not
     * void. In this case both the then and else branches must be provided as
     * if we only provide a then, the return type is VOID.
     */
    @Override
    com.chaosopher.tigerlang.compiler.tree.Exp unEx() {
        var r = Temp.create();
        return new ESEQ(
            new SEQ(
                new SEQ(
                    //test expression as conditional, passing in references to the true and false labels
                    // the actual jump is generated inside this method.
                    testExp.unCx(trueLabel, falseLabel), 
                    new SEQ(
                        new LABEL(trueLabel), // add label t, evaluate expression a put result into register r.
                        new SEQ( // eval then expression
                                new MOVE( // move ex result into r
                                    new TEMP(r), 
                                    a.unEx()
                                ),
                                new SEQ(
                                    new JUMP(joinLabel),  //go to join label
                                    new SEQ(
                                        new LABEL(falseLabel), // add label f
                                        new SEQ(new MOVE( // eval expression b put result into register r
                                                new TEMP(r), 
                                                b.unEx() // into register r
                                        ), 
                                        new JUMP(joinLabel)) //jump to join label.
                                    )
                                )
                        )
                    )
                ), 
                new LABEL(joinLabel) //join label
            ), 
            new TEMP(r) //return temp to to calling expression.
        );
    }

    @Override
    Stm unNx() {
        return new SEQ(
            new SEQ(
                testExp.unCx(trueLabel, falseLabel), // eval cx with labels t and f
                new SEQ(
                    new LABEL(trueLabel), // add label t
                    new SEQ( // eval then express
                        a.unNx(), 
                        new SEQ(
                            new JUMP(joinLabel), 
                            new SEQ(
                                new LABEL(falseLabel), // add label
                                    new SEQ(
                                        b.unNx(), // into register r
                                        new JUMP(joinLabel))
                            )
                        )
                    )
                )
            ),
            new LABEL(joinLabel)
        );
    }

    /**
     * This handles the case when an IF is being used in an IF.
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
        return new SEQ(
            testExp.unCx(trueLabel, falseLabel), // eval test expression (AND: if (a > b) then (b > c) else (0) )
            new SEQ(
                new LABEL(trueLabel), // true label
                new SEQ( // evaluate the 'then' expression as a conditional
                    a.unCx(tt, ff), 
                    new SEQ(
                        new LABEL(falseLabel), // false label
                        b.unCx(tt, ff)  // evalue the else expression
                    )
                )
            )
        );
    }
}