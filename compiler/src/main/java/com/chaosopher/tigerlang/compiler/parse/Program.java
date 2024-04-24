package com.chaosopher.tigerlang.compiler.parse;

import com.chaosopher.tigerlang.compiler.absyn.DecList;
import com.chaosopher.tigerlang.compiler.absyn.FunctionDec;
import com.chaosopher.tigerlang.compiler.symbol.Symbol;

public class Program {

    public com.chaosopher.tigerlang.compiler.absyn.Exp absyn;

    public Program(com.chaosopher.tigerlang.compiler.absyn.Exp absyn) {
        this.absyn = absyn;
    }

    public Program(DecList decList) {
        throw new Error("Program(DecList) not implemented.");
    }

    /**
     * Returns the program as a sequence of declarations, wrapped in a function
     * call, which links into the runtime main.
     * @return
     */
    public DecList getDecList() {
        return new DecList(
            new FunctionDec(0, Symbol.symbol("_main"), null, null, this.absyn, null), 
            null
        );
    }
}