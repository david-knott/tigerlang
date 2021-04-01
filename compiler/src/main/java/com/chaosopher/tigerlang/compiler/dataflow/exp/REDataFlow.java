package com.chaosopher.tigerlang.compiler.dataflow.exp;

import com.chaosopher.tigerlang.compiler.dataflow.GenKillSets;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.tree.Exp;

/**
 * Computes reachable expressions for the supplied control flow graph.
 */
class REDataFlow extends AEDataFlow {

    protected REDataFlow(CFG cfg, GenKillSets<Exp> genKillSets) {
        super(cfg, genKillSets);
    }
    
}