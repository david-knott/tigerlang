package com.chaosopher.tigerlang.compiler.absyn;

import com.chaosopher.tigerlang.compiler.symbol.Symbol;
import com.chaosopher.tigerlang.compiler.types.Constants;

public class StringTypeDec extends TypeDec {

    public static StringTypeDec instance = new StringTypeDec();

    private StringTypeDec() {
        super(0, Symbol.symbol("string"), null, null);
        super.setCreatedType(Constants.STRING);
        super.setType(Constants.STRING);
    }

}