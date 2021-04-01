package com.chaosopher.tigerlang.compiler.dataflow.def;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.chaosopher.tigerlang.compiler.dataflow.DataflowMeet;
import com.chaosopher.tigerlang.compiler.dataflow.ForwardDataFlow;
import com.chaosopher.tigerlang.compiler.dataflow.GenKillSets;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.graph.Node;
import com.chaosopher.tigerlang.compiler.graph.NodeList;
import com.chaosopher.tigerlang.compiler.tree.QuadruplePrettyPrinter;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.util.Assert;

class ReachingDefintionsDataFlow extends ForwardDataFlow<Integer> {

    public static ReachingDefintionsDataFlow analyze(CFG cfg, GenKillSets<Integer> genKillSets) {
        ReachingDefintionsDataFlow reachingDefinitions = new ReachingDefintionsDataFlow(cfg, genKillSets);
        reachingDefinitions.generate();
        return reachingDefinitions;
    }
    protected ReachingDefintionsDataFlow(CFG cfg, GenKillSets<Integer> genKillSets) {
        super(cfg, genKillSets, DataflowMeet.UNION);
    }
    @Override
    protected void initialise(CFG cfg, Map<BasicBlock, Set<Integer>> inMap, Map<BasicBlock, Set<Integer>> outMap) {
        for(Node node : cfg.nodes()) {
            BasicBlock b = this.cfg.get(node);
            inMap.put(b, new HashSet<>());
            outMap.put(b, new HashSet<>());
        }
    }
}
public class ReachingDefinitions {

    public static ReachingDefinitions analyze(CFG cfg, GenKillSets<Integer> genKillSets) {
        ReachingDefinitions reachingDefinitions = new ReachingDefinitions(cfg, genKillSets);
        reachingDefinitions.generate();
        return reachingDefinitions;
    }

    private final CFG cfg;
    private final GenKillSets<Integer> genKillSets;
    private final HashMap<BasicBlock, Set<Integer>> inMap = new HashMap<>();
    private final HashMap<BasicBlock, Set<Integer>> outMap = new HashMap<>();
    private int totalIterations;

    private ReachingDefinitions(CFG cfg, GenKillSets<Integer> genKillSets) {
        this.cfg = cfg;
        this.genKillSets = genKillSets;
    }

    public Set<Integer> getIn(BasicBlock basicBlock) {
        return this.inMap.get(basicBlock);
    }

    public Set<Integer> getOut(BasicBlock basicBlock) {
        return this.outMap.get(basicBlock);
    }

	public Set<Integer> getIn(BasicBlock basicBlock, Stm stm) {
     ///   BasicBlock basicBlock = this.cfg.getBlockReference(stm);
        StmList stmList = basicBlock.first;
        // set of all definitions that reach the start of this block
        Set<Integer> blockIn =  new HashSet<>();
        blockIn.addAll(this.inMap.get(basicBlock));
        for (; stmList != null; stmList = stmList.tail) {
            // for each statement, we get the gen and kill
            Stm s = stmList.head;
            if(s == stm) {
                return blockIn;
            }
            // reconstitute kill and gen from in.
            Set<Integer> gen = this.genKillSets.getGen(s);
            Set<Integer> kill = this.genKillSets.getKill(s);
            // remove items that were killed.
            blockIn.removeAll(kill);
            // add items that were generated.
            blockIn.addAll(gen);
        }
        Assert.unreachable("Statment was not contained in any block");
        return null;
	}

	public Set<Integer> getOut(BasicBlock basicBlock, Stm stm) {
        // could probably get in set for stm and apply gen and kill
        Set<Integer> blockOut = new HashSet<>();
        blockOut.addAll(this.getIn(basicBlock, stm));
        Set<Integer> gen = this.genKillSets.getGen(stm);
        // current block generates these
        Set<Integer> kill = this.genKillSets.getKill(stm);
        blockOut.removeAll(kill);
        blockOut.addAll(gen);
        return blockOut;
	}

    public void generate() {
        // initialise in[n] and out[n] to be empty sets. 
        for(NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
            BasicBlock b = this.cfg.get(nodes.head);
            this.inMap.put(b, new HashSet<>());
            this.outMap.put(b, new HashSet<>());
        }
        // compute in[n] and out[n] for each block.
        boolean changed = true;
        do
        {
            changed = false;
            for(NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
                BasicBlock b = this.cfg.get(nodes.head);
                // get previous iteration result
                Set<Integer> outPrev = (Set<Integer>)outMap.get(b); 
                Set<Integer> inPrev = (Set<Integer>)inMap.get(b);
                // get IN set using nodes n predecessors using OR join
                Set<Integer> in = new HashSet<>();
                for(NodeList preds = nodes.head.pred(); preds != null; preds = preds.tail) {
                    BasicBlock pb = this.cfg.get(preds.head);
                    // union meet
                    in.addAll(outMap.get(pb));
                }
                // update in(s) for this block
                inMap.put(b, in);
                // get gen and kill definition ids for this block.
                Set<Integer> gen = (Set<Integer>)this.genKillSets.getGen(b); 
                Set<Integer> kill = (Set<Integer>)this.genKillSets.getKill(b); 
                // create new set for in.
                Set<Integer> out = new HashSet<>();
                // add in(s) to out(s).
                out.addAll(in);
                // in(s) MINUS kill(s)
                out.removeAll(kill);
                // gen(s) OR ( in(s) MINUS kill(s) )
                out.addAll(gen);
                // update out(s) for this block.
                outMap.put(b, out);
                // check if sets have changed since last iteration.
                var c1 = inMap.get(b).equals(inPrev);
                var c2 = outMap.get(b).equals(outPrev);
                changed = changed || !c1 || !c2;
            }
            this.totalIterations++;
            if(!changed /*|| this.maxIterations == this.totalIterations*/) break;
        } while(true);
    }
    
	public Integer getDefinitionId(Stm op) {
        return this.genKillSets.getDefinitionId(op);
	}

    public void serialize(PrintStream printStream) {
            printStream.println("# Reaching Definitions ( iterations:" + this.totalIterations  + ") #");
            for(NodeList nodeList = this.cfg.nodes(); nodeList != null; nodeList = nodeList.tail) {
            printStream.println("## Block ##");
            BasicBlock basicBlock = this.cfg.get(nodeList.head);
            printStream.print(basicBlock.hashCode() + "");
            printStream.println();
            printStream.print("In:");
            printStream.println(this.getIn(basicBlock));
            printStream.print("Out:");
            printStream.println(this.getOut(basicBlock));
            printStream.println("### Statements ###");
            for(StmList stmList = basicBlock.first; stmList != null; stmList = stmList.tail) {
                Integer defId = this.getDefinitionId(stmList.head);
                printStream.print(defId + ":");
                stmList.head.accept(new QuadruplePrettyPrinter(printStream));
                Set<Integer> inSet = this.getIn(basicBlock, stmList.head);
                printStream.print(" In:");
                printStream.print(inSet);
                Set<Integer> outSet = this.getOut(basicBlock, stmList.head);
                printStream.print(" out:");
                printStream.print(outSet);
                printStream.println();
            }
            printStream.println("--------");
        }
    }
}