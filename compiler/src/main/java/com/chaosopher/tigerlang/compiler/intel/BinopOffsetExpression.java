package com.chaosopher.tigerlang.compiler.intel;

import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.BINOP;

/**
 */
public class BinopOffsetExpression {

    /**
     * The address value stored in the register.
     */
    final Temp base;
    /**
     * The offset from the address value.
     */
    final Integer offset;
    /**
     * The Tree.BINOP element.
     */
    final BINOP binop;

    /**
     * The default constructor.
     * 
     * @param binop
     * @param base
     * @param offset
     */
    public BinopOffsetExpression(BINOP binop, Temp base, Integer offset) {
        this.base = base;
        this.offset = offset;
        this.binop = binop;
    }
}
