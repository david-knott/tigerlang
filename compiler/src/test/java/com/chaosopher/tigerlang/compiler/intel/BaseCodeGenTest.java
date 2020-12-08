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
        return new Tree.BINOP(
            BINOP.PLUS,
            new Tree.CONST(3),
            new Tree.BINOP(
                BINOP.PLUS,
                new Tree.CONST(3),
                new Tree.CONST(3)
            )
        );
    }

	public Exp getBinopExp() {
        return new Tree.BINOP(
            BINOP.PLUS,
            new Tree.CONST(3),
            new Tree.CONST(3)
        );
    }
    
    public Exp getTemp() {
        return new Tree.TEMP(Temp.create());
    }
}