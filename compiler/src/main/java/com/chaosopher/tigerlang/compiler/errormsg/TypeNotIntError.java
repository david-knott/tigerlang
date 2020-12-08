package com.chaosopher.tigerlang.compiler.errormsg;

import com.chaosopher.tigerlang.compiler.types.Type;

public class TypeNotIntError extends CompilerError {
    private final Type type2;

    public TypeNotIntError(final int p, final Type r) {
        super(p);
        type2 = r;
    }

    public Type getType() {
        return type2;
    }
}