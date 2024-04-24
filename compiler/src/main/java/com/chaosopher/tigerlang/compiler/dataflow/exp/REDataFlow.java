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
 * Computes reachable expressions for the supplied control flow graph.
 * Given an expression a*b defined at l, this reaches line l1 if no part
 * of a*b is recomputed along any path from l to l1 or if a or b is not
 * recomputed.
 * 
 * While propagating available expression set E across block, 
 * For statement s, what expression definitions reach s ?
 * s { 1, 2, 3 } - expression definition 1, 2 ,3 reach s.
 * if there is a statement s1 where expression e is recomputed
 * any previous definition for e is removed and s1 defintion is added
 * if there is a statement s2 which recomputes a variable in e, any 
 * definitions of e are removed.
 * 
 * at each program point (edge) in cfg we are concerned what definitions
 * reach these points. A statment / block modifieds these definitions 
 * for the next edge.
 *
 * We need to relate definition ids with expressions. Using gen and kill
 * for expressions, if a statement s1 generates an expression e1, this means
 * it also generates a exp defintion s1 : e1, s1 is on the outgoing egde. 
 *
 * if it kills an expression e1, all definitions for e1 are removed on the outgoing edge.
 *
 * If statement s1 recomputes an expression e1, any old reference to e1 is removed and a new reference is added for s1
 */

 /*
class REDataFlow extends AEDataFlow {

    public static REDataFlow analyze(CFG cfg, GenKillSets<Exp> aeGenKillSets, GenKillSets<Integer> reGenKillSets) {
        REDataFlow reachingExpressions = new REDataFlow(cfg, aeGenKillSets, reGenKillSets);
        reachingExpressions.generate();
        return reachingExpressions;
    }

    private GenKillSets<Integer> reGenKillSets;

    protected REDataFlow(CFG cfg, GenKillSets<Exp> aeGenKillSets, GenKillSets<Integer> reGenKillSets) {
        super(cfg, aeGenKillSets);
        this.reGenKillSets = reGenKillSets;
    }

    @Override
    protected void loopComplete(Node node, Set<Exp> in, Set<Exp> out, HashMap<BasicBlock, Set<Exp>> minMap2,
            HashMap<BasicBlock, Set<Exp>> moutMap2) {

        BasicBlock basicBlock = this.cfg.get(node);
        Set<Integer> genSet = reGenKillSets.getGen(basicBlock);
        System.out.println(genSet);
        Set<Integer> killSet = reGenKillSets.getKill(basicBlock);
        System.out.println(killSet);
        
        // in contains updated in set for basic block

        // out contains updated out set for basic block

        //minMap2 and moutMap2 contain the hash of basic blocks and related in oand out sets respectively.

    }
}
*/
/**
 * Computes available expressions for the supplied control flow graph.
 */
class REDataFlow extends ForwardDataFlow<Integer> {

    private final Set<Integer> dataFlowSet = new HashSet<>();
    
    public static REDataFlow analyze(CFG cfg, GenKillSets<Integer> genKillSets) {
        REDataFlow reachingDefinitions = new REDataFlow(cfg, genKillSets);
        reachingDefinitions.generate();
        return reachingDefinitions;
    }

    protected REDataFlow(CFG cfg, GenKillSets<Integer> genKillSets) {
        super(cfg, genKillSets, DataflowMeet.UNION);
    }

    private Set<Integer> getAllAvailableExpressions() {
        //return new HashSet<Integer>(this.dataFlowSet);
        return new HashSet<>();

    }

    @Override
    protected void initialise(CFG cfg, Map<BasicBlock, Set<Integer>> inMap, Map<BasicBlock, Set<Integer>> outMap) {
        NodeList nodeList = cfg.nodes();
        Node start = nodeList.head;
        BasicBlock startBlock = this.cfg.get(start);
        inMap.put(startBlock, new HashSet<>());
        nodeList = nodeList.tail;
        for (; nodeList != null; nodeList = nodeList.tail) {
            BasicBlock b = this.cfg.get(nodeList.head);
            for (StmList stmList = b.first; stmList != null; stmList = stmList.tail) {
                Exp exp = ExtractExp.getExp(stmList.head);
                if (exp != null) {
                    this.dataFlowSet.add(this.genKillSets.getDefinitionId(stmList.head));
                }
            }
            inMap.put(b, this.getAllAvailableExpressions());
            Set<Integer> initOut = new HashSet<>();
            initOut.addAll(getAllAvailableExpressions());
            initOut.removeAll(this.genKillSets.getKill(b));
            initOut.addAll(this.genKillSets.getGen(b));
            outMap.put(b, initOut);
        }
        outMap.put(startBlock, this.getAllAvailableExpressions());
    }
}