package com.chaosopher.tigerlang.compiler.intel;

import com.chaosopher.tigerlang.compiler.frame.Access;
import com.chaosopher.tigerlang.compiler.tree.BINOP;
import com.chaosopher.tigerlang.compiler.tree.CONST;
import com.chaosopher.tigerlang.compiler.tree.Exp;
import com.chaosopher.tigerlang.compiler.tree.MEM;

class InFrame extends Access {
    int offset;

    InFrame(int os) {
        System.out.println("Inframe Var " + os);
        offset = os;
    }

    /**
     * Returns the memory access for this variable relative
     * to the frame pointer. If this access is being accessed
     * from a nested function, we use a frame pointer calculated
     * from the static links.
     */
    @Override
    public Exp exp(Exp framePtr) {
       return new MEM(new BINOP(BINOP.PLUS, framePtr, new CONST(offset)));
    }
}