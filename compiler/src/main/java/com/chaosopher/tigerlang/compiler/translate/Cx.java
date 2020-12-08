package com.chaosopher.tigerlang.compiler.translate;

import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.temp.Temp;

/**
 * An abstract class that represents an expression that yields a boolean value.
 * This is used to control logic flow in the program.
 * 
 * This class is subclassed by RelCx which is used for comparision operations
 */
abstract class Cx extends Exp {

    @Override
    Tree.Exp unEx() {
        Label t = Label.create();
        Label f = Label.create();
        Temp r = Temp.create();
        // move const 1 into temp r
        // call conditional statement with labels
        // add label false
        // move const 0 into register r
        // add label true
        // create emit temp with register r
        return new Tree.ESEQ(
                new Tree.SEQ(new Tree.MOVE(new Tree.TEMP(r), new Tree.CONST(1)), new Tree.SEQ(unCx(t, f),
                        new Tree.SEQ(new Tree.LABEL(f),
                                new Tree.SEQ(new Tree.MOVE(new Tree.TEMP(r), new Tree.CONST(0)), new Tree.LABEL(t))))),
                new Tree.TEMP(r));
    }

    @Override
    Tree.Stm unNx() {
        Label a = new Label();
        return new Tree.SEQ(unCx(a, a), new Tree.LABEL(a));
    }
}