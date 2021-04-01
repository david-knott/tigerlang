package com.chaosopher.tigerlang.compiler.dataflow;

import java.util.Map;
import java.util.Set;

import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.graph.Node;
import com.chaosopher.tigerlang.compiler.graph.NodeList;

public abstract class ForwardDataFlow<T> extends Dataflow<T> {

    protected ForwardDataFlow(CFG cfg, GenKillSets<T> genKillSets, DataflowMeet dataflowMeet) {
        super(cfg, genKillSets, dataflowMeet);
    }

    @Override
    protected NodeList getDirectedNodeList() {
        return this.cfg.nodes();
    }

    @Override
    protected void processNode(Node node, Set<T> in, Set<T> out, Map<BasicBlock, Set<T>> inMap, Map<BasicBlock, Set<T>> outMap) {
        BasicBlock b = this.cfg.get(node);
        out.addAll(in);
        /* transfer function */
        out.removeAll(this.genKillSets.getKill(b));
        out.addAll(this.genKillSets.getGen(b));
        outMap.put(b, out);
    }

    @Override
    protected void meetUnion(Node node, Set<T> in, Set<T> out, Map<BasicBlock, Set<T>> inMap, Map<BasicBlock, Set<T>> outMap) {
        BasicBlock b = this.cfg.get(node);
        if(node.pred() != null) {
            for (Node adj : node.pred()) {
                BasicBlock ab = this.cfg.get(adj);
                in.addAll(outMap.get(ab));
            }
        }
        inMap.put(b, in);
    }

    @Override
    protected void meetIntersection(Node node, Set<T> in, Set<T> out, Map<BasicBlock, Set<T>> inMap, Map<BasicBlock, Set<T>> outMap) {
        BasicBlock b = this.cfg.get(node);
        NodeList preds;
        if( (preds = node.pred()) != null) {
            Set<T> predOut = null;
            for (Node pred : preds) {
                BasicBlock predBlock = this.cfg.get(pred);
                if(predOut == null) {
                    predOut = outMap.get(predBlock);
                    in.addAll(predOut);
                }
                predBlock = this.cfg.get(pred);
                predOut = outMap.get(predBlock);
                in.retainAll(predOut);
            }
        }
        inMap.put(b, in);
    }
}