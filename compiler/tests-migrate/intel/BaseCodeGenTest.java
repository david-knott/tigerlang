package com.chaosopher.tigerlang.compiler.intel;

import com.chaosopher.tigerlang.compiler.tree.Exp;

import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.BINOP;
import com.chaosopher.tigerlang.compiler.tree.IR;

public abstract class BaseCodeGenTest {
    public CodeGen createCodeGen(Emitter emitter) {
        CodeGen codeGen = new CodeGen();
        Reducer reducer = new Reducer(emitter);
        codeGen.setReducer(reducer);
        return codeGen;
    }

    public Exp getNestedBinopExp() {
        return new com.chaosopher.tigerlang.compiler.tree.BINOP(
            BINOP.PLUS,
            new com.chaosopher.tigerlang.compiler.tree.CONST(3),
            new com.chaosopher.tigerlang.compiler.tree.BINOP(
                BINOP.PLUS,
                new com.chaosopher.tigerlang.compiler.tree.CONST(3),
                new com.chaosopher.tigerlang.compiler.tree.CONST(3)
            )
        );
    }

	public Exp getBinopExp() {
        return new com.chaosopher.tigerlang.compiler.tree.BINOP(
            BINOP.PLUS,
            new com.chaosopher.tigerlang.compiler.tree.CONST(3),
            new com.chaosopher.tigerlang.compiler.tree.CONST(3)
        );
    }
    
    public Exp getTemp() {
        return new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create());
    }
}