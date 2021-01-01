package com.chaosopher.tigerlang.compiler.absyn;

import com.chaosopher.tigerlang.compiler.symbol.Symbol;
import com.chaosopher.tigerlang.compiler.types.Constants;

public final class IntTypeDec extends TypeDec {

    public IntTypeDec() {
        super(0, Symbol.symbol("int"), null, null);
        super.setCreatedType(Constants.INT);
        super.setType(Constants.INT);
    }
}