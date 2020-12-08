package com.chaosopher.tigerlang.compiler.errormsg;

import com.chaosopher.tigerlang.compiler.symbol.Symbol;

public class VariableAssignError extends CompilerError {

    private final Symbol symbol;
    public VariableAssignError(final int p, final Symbol sym) {
        super(p);
        symbol = sym;
    }

    public Symbol getSymbol(){
        return symbol;
    }
}