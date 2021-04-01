package com.chaosopher.tigerlang.compiler.dataflow.def;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.chaosopher.tigerlang.compiler.dataflow.DataflowMeet;
import com.chaosopher.tigerlang.compiler.dataflow.ForwardDataFlow;
import com.chaosopher.tigerlang.compiler.dataflow.GenKillSets;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.graph.Node;

/**
 * Computes reaching defintions from the supplied control flow graph.
 */
class RDDataFlow extends ForwardDataFlow<Integer> {

    public static RDDataFlow analyze(CFG cfg, GenKillSets<Integer> genKillSets) {
        RDDataFlow reachingDefinitions = new RDDataFlow(cfg, genKillSets);
        reachingDefinitions.generate();
        return reachingDefinitions;
    }

    protected RDDataFlow(CFG cfg, GenKillSets<Integer> genKillSets) {
        super(cfg, genKillSets, DataflowMeet.UNION);
    }
    
    @Override
    protected void initialise(CFG cfg, Map<BasicBlock, Set<Integer>> inMap, Map<BasicBlock, Set<Integer>> outMap) {
        for(Node node : cfg.nodes()) {
            BasicBlock b = this.cfg.get(node);
            inMap.put(b, new HashSet<>());
            outMap.put(b, new HashSet<>());
        }
    }
}