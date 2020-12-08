package com.chaosopher.tigerlang.compiler.errormsg;

import com.chaosopher.tigerlang.compiler.types.Type;

public class ArgumentMismatchError extends CompilerError{
    private final Type type1;
    private final Type type2;

    public ArgumentMismatchError(final int p, final Type fml, final Type arg) {
        super(p);
        type1 = fml;
        type2 = arg;
    }

    public Type getFormal(){
        return type1;
    }

    public Type getArgument(){
        return type2;
    }
}