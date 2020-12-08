package com.chaosopher.tigerlang.compiler.intel;

import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.util.Assert;

/**
 * Represents a memory access with a displacement. The displacement
 * can either be positive or negative. The memory address is stored
 * in a register.
 */
public class IndirectWithDisplacementExpression {

    private final BinopOffsetExpression binopOffsetExpression;

    public IndirectWithDisplacementExpression(BinopOffsetExpression binopOffsetExpression) {
        Assert.assertIsTrue(binopOffsetExpression.binop.binop == Tree.BINOP.PLUS
                || binopOffsetExpression.binop.binop == Tree.BINOP.MINUS);
        this.binopOffsetExpression = binopOffsetExpression;
    }

    public int displacement() {
        return binopOffsetExpression.binop.binop == Tree.BINOP.PLUS ? this.binopOffsetExpression.offset
                : -this.binopOffsetExpression.offset;
    }

    public Temp temp() {
        return this.binopOffsetExpression.base;
    }
}
