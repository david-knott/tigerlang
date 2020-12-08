package com.chaosopher.tigerlang.compiler.canon;

import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;

/**
 * Produces a canonicalization of the IR Tree
 */
public interface Canonicalization {

    StmList canon(Stm stm);
}