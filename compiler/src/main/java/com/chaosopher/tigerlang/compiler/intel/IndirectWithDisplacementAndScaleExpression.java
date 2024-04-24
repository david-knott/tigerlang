package com.chaosopher.tigerlang.compiler.intel;

import com.chaosopher.tigerlang.compiler.temp.Temp;

public class IndirectWithDisplacementAndScaleExpression {

    final Temp base;
    private final BinopOffsetExpression binopOffsetExpression;

	public IndirectWithDisplacementAndScaleExpression(Temp base, BinopOffsetExpression binopOffsetExpression) {
        this.base = base;
        this.binopOffsetExpression = binopOffsetExpression;
    }

    public int displacement() {
        return binopOffsetExpression.binop.binop == com.chaosopher.tigerlang.compiler.tree.BINOP.PLUS ? this.binopOffsetExpression.offset
                : -this.binopOffsetExpression.offset;
    }

    public Temp index() {
        return this.binopOffsetExpression.base;
    }

    public int wordSize() {
        return 8;
    }
}
