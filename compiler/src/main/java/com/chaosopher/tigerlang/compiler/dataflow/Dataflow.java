package com.chaosopher.tigerlang.compiler.dataflow;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.graph.Node;
import com.chaosopher.tigerlang.compiler.graph.NodeList;

/**
 * Abstract base class that provides dataflow support for the classical data flow operations.
 */
public abstract class Dataflow<T> {

    protected final CFG cfg;
    private final DataflowMeet dataflowMeet;
    private final DataflowDir dataflowDir;
    protected final GenKillSets<T> genKillSets;
    private HashMap<BasicBlock, Set<T>> minMap = new HashMap<>();
    private HashMap<BasicBlock, Set<T>> moutMap = new HashMap<>();

    protected Dataflow(final CFG cfg, GenKillSets<T> genKillSets, final DataflowMeet dataflowMeet, final DataflowDir dataflowDir) {
        this.cfg = cfg;
        this.dataflowMeet = dataflowMeet;
        this.dataflowDir = dataflowDir;
        this.genKillSets = genKillSets;
    }
    
    private NodeList getDirectedNodeList() {
        return this.dataflowDir == DataflowDir.FORWARD ? 
            this.cfg.nodes() :
            this.cfg.nodes().reverse();
    }

    protected NodeList getDirectedAdjacentNodeList(Node node) {
        return this.dataflowDir == DataflowDir.FORWARD ?
            node.succ() :
            node.pred();
    }

    protected void processNode(Node node, Set<T> in, Map<BasicBlock, Set<T>> outMap) {
        BasicBlock b = this.cfg.get(node);
        Set<T> out = new HashSet<>();
        out.addAll(in);
        out.removeAll(this.genKillSets.getKill(b));
        out.addAll(this.genKillSets.getGen(b));
        outMap.put(b, out);
    }

    protected void meetUnion(Node node, Set<T> in, Map<BasicBlock, Set<T>> inMap, Map<BasicBlock, Set<T>> outMap) {
        BasicBlock b = this.cfg.get(node);
        for (Node adj : this.getDirectedAdjacentNodeList(node)) {
            BasicBlock ab = this.cfg.get(adj);
            in.addAll(outMap.get(ab));
        }
        inMap.put(b, in);
    }

    protected void meetIntersection(Node node, Set<T> in, Map<BasicBlock, Set<T>> inMap, Map<BasicBlock, Set<T>> outMap) {
        BasicBlock b = this.cfg.get(node);
        NodeList preds;
        if( (preds = this.getDirectedAdjacentNodeList(node)) != null) {
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

    protected void generate() {
        boolean changed = true;
        
        do {
            changed = false;
            for(Node node : this.getDirectedNodeList()) {
                // will contain all expression on first iteration
                BasicBlock b = this.cfg.get(node);
                Set<T> outPrev = this.moutMap.get(b);
                Set<T> inPrev = this.minMap.get(b);

                Set<T> in = new HashSet<>();
                Set<T> out = new HashSet<>();

                this.doGenerate(node, in, this.minMap, this.moutMap);

                var c1 = in.equals(inPrev);
                var c2 = out.equals(outPrev);
                changed = changed || !c1 || !c2;
            }
            if (!changed)
                break;
        } while (true);
    }

    protected void meet(Node node, Set<T> in, Map<BasicBlock, Set<T>> inMap, Map<BasicBlock, Set<T>> outMap) {
        switch (this.dataflowMeet) {
            case INTERSECTION:
                meetIntersection(node, in, inMap, outMap);
            case UNION:
                meetUnion(node, in, inMap, outMap);
            default:
                throw new Error(String.format("Unsupported meet operator %d", this.dataflowMeet));
        }
    }

    protected abstract void doGenerate(Node node, Set<T> in, Map<BasicBlock, Set<T>> inMap, Map<BasicBlock, Set<T>> outMap);
    
}