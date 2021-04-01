package com.chaosopher.tigerlang.compiler.dataflow;

import java.util.Map;
import java.util.Set;

import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.graph.Node;

abstract class ReverseDataFlow<T> extends Dataflow<T> {

    protected ReverseDataFlow(CFG cfg, GenKillSets<T> genKillSets, DataflowMeet dataflowMeet, DataflowDir dataflowDir) {
        super(cfg, genKillSets, dataflowMeet, DataflowDir.REVERSE);
    }

    @Override
    protected void doGenerate(Node node, Set<T> in, Map<BasicBlock, Set<T>> inMap, Map<BasicBlock, Set<T>> outMap) {
        this.meet(node, in, inMap, outMap);
        this.processNode(node, in, outMap);
    }
}