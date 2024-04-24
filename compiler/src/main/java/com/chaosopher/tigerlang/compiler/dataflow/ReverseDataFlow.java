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

public abstract class ReverseDataFlow<T> extends Dataflow<T> {

    protected ReverseDataFlow(CFG cfg, GenKillSets<T> genKillSets, DataflowMeet dataflowMeet) {
        super(cfg, genKillSets, dataflowMeet);
    }

    @Override
    protected NodeList getDirectedNodeList() {
        return this.cfg.nodes().reverse();
    }

    @Override
    protected StmList getStatementList(BasicBlock basicBlock) {
        StmList reversed = null;
        for(StmList stmList = basicBlock.first; stmList != null; stmList = stmList.tail) {
            reversed = new StmList(stmList.head, reversed);
        }
        return reversed;
    }

    @Override
    public Set<T> getOut(BasicBlock basicBlock, Stm stm) {
        StmList stmList = this.getStatementList(basicBlock);
        // set of all definitions that reach the start of this block
        Set<T> blockOut =  new HashSet<>();
        blockOut.addAll(this.moutMap.get(basicBlock));
        for (; stmList != null; stmList = stmList.tail) {
            // for each statement, we get the gen and kill
            Stm s = stmList.head;
            if(s == stm) {
                return blockOut;
            }
            // reconstitute kill and gen from in.
            Set<T> gen = this.genKillSets.getGen(s);
            // 
            Set<T> kill = this.genKillSets.getKill(s);
            // remove items that were killed.
            blockOut.removeAll(kill);
            // add items that were generated.
            blockOut.addAll(gen);
        }
        Assert.unreachable("Statment was not contained in any block");
        return null;
    }

    @Override
    public Set<T> getIn(BasicBlock basicBlock, Stm stm) {
        Set<T> blockIn = new HashSet<>();
        blockIn.addAll(this.getOut(basicBlock, stm));
        Set<T> gen = this.genKillSets.getGen(stm);
        // current block generates these
        Set<T> kill = this.genKillSets.getKill(stm);
        blockIn.removeAll(kill);
        blockIn.addAll(gen);
        return blockIn;
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