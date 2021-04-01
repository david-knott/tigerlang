package com.chaosopher.tigerlang.compiler.dataflow;

import java.util.Map;
import java.util.Set;

import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.graph.Node;
import com.chaosopher.tigerlang.compiler.graph.NodeList;

abstract class ReverseDataFlow<T> extends Dataflow<T> {

    protected ReverseDataFlow(CFG cfg, GenKillSets<T> genKillSets, DataflowMeet dataflowMeet) {
        super(cfg, genKillSets, dataflowMeet);
    }

    @Override
    protected NodeList getDirectedNodeList() {
        return this.cfg.nodes().reverse();
    }

    @Override
    protected void processNode(Node node, Set<T> in, Set<T> out, Map<BasicBlock, Set<T>> inMap, Map<BasicBlock, Set<T>> outMap) {
        BasicBlock b = this.cfg.get(node);
        in.addAll(out);
        /* transfer function */
        in.removeAll(this.genKillSets.getKill(b));
        in.addAll(this.genKillSets.getGen(b));
        inMap.put(b, in);
    }

    @Override
    protected void meetUnion(Node node, Set<T> in, Set<T> out, Map<BasicBlock, Set<T>> inMap, Map<BasicBlock, Set<T>> outMap) {
        BasicBlock b = this.cfg.get(node);
        if(node.succ() != null) {
            for (Node adj : node.succ()) {
                BasicBlock ab = this.cfg.get(adj);
                out.addAll(inMap.get(ab));
            }
        }
        outMap.put(b, out);
    }

    @Override
    protected void meetIntersection(Node node, Set<T> in, Set<T> out, Map<BasicBlock, Set<T>> inMap, Map<BasicBlock, Set<T>> outMap) {
        BasicBlock b = this.cfg.get(node);
        NodeList succs;
        if( (succs = node.succ()) != null) {
            Set<T> sucIn = null;
            for (Node suc : succs) {
                BasicBlock sucBlock = this.cfg.get(suc);
                if(sucIn == null) {
                    sucIn = inMap.get(sucBlock);
                    out.addAll(sucIn);
                }
                sucBlock = this.cfg.get(suc);
                sucIn = inMap.get(sucBlock);
                out.retainAll(sucIn);
            }
        }
        outMap.put(b, out);
    }
}