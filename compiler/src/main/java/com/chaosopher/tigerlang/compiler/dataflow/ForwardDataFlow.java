package com.chaosopher.tigerlang.compiler.dataflow;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.graph.Node;
import com.chaosopher.tigerlang.compiler.graph.NodeList;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.util.Assert;

public abstract class ForwardDataFlow<T> extends Dataflow<T> {

    protected ForwardDataFlow(CFG cfg, GenKillSets<T> genKillSets, DataflowMeet dataflowMeet) {
        super(cfg, genKillSets, dataflowMeet);
    }

    @Override
    protected NodeList getDirectedNodeList() {
        return this.cfg.nodes();
    }

    @Override
    protected StmList getStatementList(BasicBlock basicBlock) {
        return basicBlock.first;
    }

    @Override
    public Set<T> getIn(BasicBlock basicBlock, Stm stm) {
        StmList stmList = this.getStatementList(basicBlock);
        // set of all definitions that reach the start of this block
        Set<T> blockIn =  new HashSet<>();
        blockIn.addAll(this.minMap.get(basicBlock));
        for (; stmList != null; stmList = stmList.tail) {
            // for each statement, we get the gen and kill
            Stm s = stmList.head;
            if(s == stm) {
                return blockIn;
            }
            // reconstitute kill and gen from in.
            Set<T> gen = this.genKillSets.getGen(s);
            // 
            Set<T> kill = this.genKillSets.getKill(s);
            // remove items that were killed.
            blockIn.removeAll(kill);
            // add items that were generated.
            blockIn.addAll(gen);
        }
        Assert.unreachable("Statment was not contained in any block");
        return null;
    }

    @Override
    public Set<T> getOut(BasicBlock basicBlock, Stm stm) {
        // could probably get in set for stm and apply gen and kill
        Set<T> blockOut = new HashSet<>();
        blockOut.addAll(this.getIn(basicBlock, stm));
        Set<T> gen = this.genKillSets.getGen(stm);
        // current block generates these
        Set<T> kill = this.genKillSets.getKill(stm);
        blockOut.removeAll(kill);
        blockOut.addAll(gen);
        return blockOut;
    }

    /**
     * Override fills the out set with (in - kill(b)) + gen(b) and updates the outMap.
     */
    @Override
    protected void processNode(Node node, Set<T> in, Set<T> out, Map<BasicBlock, Set<T>> inMap, Map<BasicBlock, Set<T>> outMap) {
        BasicBlock b = this.cfg.get(node);
        out.addAll(in);
        /* transfer function */
        out.removeAll(this.genKillSets.getKill(b));
        out.addAll(this.genKillSets.getGen(b));
        outMap.put(b, out);
    }

    /**
     * Override populates the in set and inMap hasmap with union of predecessor nodes out sets.
     */
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

    /**
     * Override populates the in set and inMap hasmap with intersection of predecessor nodes out sets.
     */
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