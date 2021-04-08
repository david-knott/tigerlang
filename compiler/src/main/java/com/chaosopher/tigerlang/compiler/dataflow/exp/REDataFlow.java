package com.chaosopher.tigerlang.compiler.dataflow.exp;

import java.util.Map;
import java.util.Set;

import com.chaosopher.tigerlang.compiler.dataflow.GenKillSets;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.graph.Node;
import com.chaosopher.tigerlang.compiler.tree.Exp;

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
class REDataFlow extends AEDataFlow {

    public static REDataFlow analyze(CFG cfg, GenKillSets<Exp> genKillSets) {
        REDataFlow reachingExpressions = new REDataFlow(cfg, genKillSets);
        reachingExpressions.generate();
        return reachingExpressions;
    }

    protected REDataFlow(CFG cfg, GenKillSets<Exp> genKillSets) {
        super(cfg, genKillSets);
    }

    @Override
    protected void processNode(Node node, Set<Exp> in, Set<Exp> out, Map<BasicBlock, Set<Exp>> inMap,
            Map<BasicBlock, Set<Exp>> outMap) {
	// in and out are empty at start of each node loop.
        super.processNode(node, in, out, inMap, outMap);
    }

    @Override
    protected void meetIntersection(Node node, Set<Exp> in, Set<Exp> out, Map<BasicBlock, Set<Exp>> inMap,
            Map<BasicBlock, Set<Exp>> outMap) {
	// in and out are empty at start of each node loop.
        super.meetIntersection(node, in, out, inMap, outMap);
    }
}
