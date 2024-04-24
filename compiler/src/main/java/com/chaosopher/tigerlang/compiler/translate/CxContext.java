package com.chaosopher.tigerlang.compiler.translate;

import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.temp.LabelFactory;
import com.chaosopher.tigerlang.compiler.temp.Temp;

/**
 * An abstract class that represents an expression that yields a boolean value.
 * This is used to control logic flow in the program.
 * 
 * This class is subclassed by RelCx which is used for comparision operations
 */
abstract class CxContext extends TranslateContext {

    public CxContext(LabelFactory labelFactory) {
        super(labelFactory);
    }

    @Override
    com.chaosopher.tigerlang.compiler.tree.Exp unEx() {
        Label t = this.labelFactory.create();
        Label f = this.labelFactory.create();
        Temp r = Temp.create();
        // move const 1 into temp r
        // call conditional statement with labels
        // add label false
        // move const 0 into register r
        // add label true
        // create emit temp with register r
        return new com.chaosopher.tigerlang.compiler.tree.ESEQ(
                new com.chaosopher.tigerlang.compiler.tree.SEQ(new com.chaosopher.tigerlang.compiler.tree.MOVE(new com.chaosopher.tigerlang.compiler.tree.TEMP(r), new com.chaosopher.tigerlang.compiler.tree.CONST(1)), new com.chaosopher.tigerlang.compiler.tree.SEQ(unCx(t, f),
                        new com.chaosopher.tigerlang.compiler.tree.SEQ(new com.chaosopher.tigerlang.compiler.tree.LABEL(f),
                                new com.chaosopher.tigerlang.compiler.tree.SEQ(new com.chaosopher.tigerlang.compiler.tree.MOVE(new com.chaosopher.tigerlang.compiler.tree.TEMP(r), new com.chaosopher.tigerlang.compiler.tree.CONST(0)), new com.chaosopher.tigerlang.compiler.tree.LABEL(t))))),
                new com.chaosopher.tigerlang.compiler.tree.TEMP(r));
    }

    @Override
    com.chaosopher.tigerlang.compiler.tree.Stm unNx() {
        Label a = this.labelFactory.create();
        return new com.chaosopher.tigerlang.compiler.tree.SEQ(unCx(a, a), new com.chaosopher.tigerlang.compiler.tree.LABEL(a));
    }
}