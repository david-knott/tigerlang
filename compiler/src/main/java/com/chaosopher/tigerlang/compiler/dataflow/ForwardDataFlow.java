package com.chaosopher.tigerlang.compiler.dataflow;

import java.util.Map;
import java.util.Set;

import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.graph.Node;

public abstract class ForwardDataFlow<T> extends Dataflow<T> {

    protected ForwardDataFlow(CFG cfg, GenKillSets<T> genKillSets, DataflowMeet dataflowMeet) {
        super(cfg, genKillSets, dataflowMeet, DataflowDir.FORWARD);
    }

    @Override
    protected void doGenerate(Node node, Set<T> in, Set<T> out, Map<BasicBlock, Set<T>> inMap, Map<BasicBlock, Set<T>> outMap) {
        this.meet(node, in, out, inMap, outMap);
        this.processNode(node, in, out, inMap, outMap);
    }
}