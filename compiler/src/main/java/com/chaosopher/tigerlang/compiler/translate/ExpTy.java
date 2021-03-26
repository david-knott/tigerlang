package com.chaosopher.tigerlang.compiler.translate;

import com.chaosopher.tigerlang.compiler.types.Type;

public class ExpTy {

    public static ExpTy ERROR = new ExpTy();

    public TranslateContext exp;
    public Type ty;

    private ExpTy() {
        this.exp = null;
        this.ty = null;
    }

    public ExpTy(TranslateContext e, Type t) {
        exp = e;
        ty = t;
    }
}

