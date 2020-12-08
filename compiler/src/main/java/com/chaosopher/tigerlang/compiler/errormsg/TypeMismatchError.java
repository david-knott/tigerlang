package com.chaosopher.tigerlang.compiler.errormsg;

import com.chaosopher.tigerlang.compiler.types.Type;

public class TypeMismatchError extends CompilerError {
    private final Type type1;
    private final Type type2;

    public TypeMismatchError(final int p, final Type l) {
        super(p);
        type1 = l;
        type2 = null;
    }

    public TypeMismatchError(final int p, final Type l, final Type r) {
        super(p);
        type1 = l;
        type2 = r;
    }

    public Type getLeft() {
        return type1;
    }

    public Type getRight() {
        return type2;
    }
}