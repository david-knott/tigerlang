package com.chaosopher.tigerlang.compiler.dataflow.exp;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.chaosopher.tigerlang.compiler.dataflow.DataflowMeet;
import com.chaosopher.tigerlang.compiler.dataflow.ForwardDataFlow;
import com.chaosopher.tigerlang.compiler.dataflow.GenKillSets;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.dataflow.utils.ExtractExp;
import com.chaosopher.tigerlang.compiler.graph.Node;
import com.chaosopher.tigerlang.compiler.graph.NodeList;
import com.chaosopher.tigerlang.compiler.tree.Exp;
import com.chaosopher.tigerlang.compiler.tree.StmList;

/**
 * Computes available expressions for the supplied control flow graph.
 */
class AEDataFlow extends ForwardDataFlow<Exp> {

    private final Set<Exp> dataFlowSet = new HashSet<>();
    
    public static AEDataFlow analyze(CFG cfg, GenKillSets<Exp> genKillSets) {
        AEDataFlow reachingDefinitions = new AEDataFlow(cfg, genKillSets);
        reachingDefinitions.generate();
        return reachingDefinitions;
    }

    protected AEDataFlow(CFG cfg, GenKillSets<Exp> genKillSets) {
        super(cfg, genKillSets, DataflowMeet.INTERSECTION);
    }

    private Set<Exp> getAllAvailableExpressions() {
        return this.dataFlowSet;
    }

    @Override
    protected void initialise(CFG cfg, Map<BasicBlock, Set<Exp>> inMap, Map<BasicBlock, Set<Exp>> outMap) {
        NodeList nodeList = cfg.nodes();
        Node start = nodeList.head;
        BasicBlock startBlock = this.cfg.get(start);
        inMap.put(startBlock, new HashSet<>());
        //this.outMap.put(startBlock, this.genKillSets.getGen(startBlock));
        outMap.put(startBlock, this.getAllAvailableExpressions());
        // all other sets ( IN & OUT ) have all available expressions.
        nodeList = nodeList.tail;
        for (; nodeList != null; nodeList = nodeList.tail) {
            BasicBlock b = this.cfg.get(nodeList.head);
            for (StmList stmList = b.first; stmList != null; stmList = stmList.tail) {
                Exp exp = ExtractExp.getExp(stmList.head);
                if (exp != null) {
                    this.dataFlowSet.add(exp);
                }
            }
            inMap.put(b, this.getAllAvailableExpressions());
            Set<Exp> initOut = new HashSet<>();
            initOut.addAll(getAllAvailableExpressions());
            initOut.removeAll(this.genKillSets.getKill(b));
            initOut.addAll(this.genKillSets.getGen(b));
            outMap.put(b, initOut);
        }
    }
}