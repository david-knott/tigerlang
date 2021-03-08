package com.chaosopher.tigerlang.compiler.dataflow.def;

import java.io.PrintStream;
import java.util.BitSet;
import java.util.HashMap;

import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.dataflow.DataFlowSet;
import com.chaosopher.tigerlang.compiler.graph.NodeList;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.QuadruplePrettyPrinter;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.util.Assert;

class ReachingDefinitions {

    public static ReachingDefinitions analyze(CFG cfg, GenKillSets genKillSets) {
        ReachingDefinitions reachingDefinitions = new ReachingDefinitions(cfg, genKillSets);
        reachingDefinitions.generate();
        return reachingDefinitions;
    }

    private final CFG cfg;
    private final GenKillSets genKillSets;
    private final HashMap<BasicBlock, DataFlowSet<Integer>> inMap = new HashMap<>();
    private final HashMap<BasicBlock, DataFlowSet<Integer>> outMap = new HashMap<>();
    private final int maxIterations = 10;
    private int totalIterations;

    private ReachingDefinitions(CFG cfg, GenKillSets genKillSets) {
        this.cfg = cfg;
        this.genKillSets = genKillSets;
    }

    public DataFlowSet<Integer> getIn(int defId) {
        return this.getIn(this.genKillSets.getDefinition(defId));
    }

    public DataFlowSet<Integer> getOut(int defId) {
        return this.getOut(this.genKillSets.getDefinition(defId));
    }

    public DataFlowSet<Integer> getIn(BasicBlock basicBlock) {
        return this.inMap.get(basicBlock);
    }

    public DataFlowSet<Integer> getOut(BasicBlock basicBlock) {
        return this.outMap.get(basicBlock);
    }

	public DataFlowSet<Integer> getIn(Stm stm) {
        BasicBlock basicBlock = this.cfg.getBlockReference(stm);
        StmList stmList = basicBlock.first;
        // set of all definitions that reach the start of this block
        DataFlowSet<Integer> blockIn =  new DataFlowSet<>();
        blockIn.or(this.inMap.get(basicBlock));
        for (; stmList != null; stmList = stmList.tail) {
            // for each statement, we get the gen and kill
            Stm s = stmList.head;
            if(s == stm) {
                return blockIn;
            }
            // reconstitute kill and gen from in.
            DataFlowSet<Integer> gen = this.genKillSets.getGen(s);
            DataFlowSet<Integer> kill = this.genKillSets.getKill(s);
            // remove items that were killed.
            blockIn.andNot(kill);
            // add items that were generated.
            blockIn.or(gen);
        }
        Assert.unreachable("Statment was not contained in any block");
        return null;
	}

	public DataFlowSet<Integer> getOut(Stm stm) {
        // could probably get in set for stm and apply gen and kill
        DataFlowSet<Integer> blockOut = new DataFlowSet<>();
        blockOut.or(this.getIn(stm));
        DataFlowSet<Integer> gen = this.genKillSets.getGen(stm);
        // current block generates these
        DataFlowSet<Integer> kill = this.genKillSets.getKill(stm);
        blockOut.andNot(kill);
        blockOut.or(gen);
        return blockOut;
	}

    public void generate() {
        // initialise in[n] and out[n] to be empty sets. 
        for(NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
            BasicBlock b = this.cfg.get(nodes.head);
            this.inMap.put(b, new DataFlowSet<>());
            this.outMap.put(b, new DataFlowSet<>());
        }
        // compute in[n] and out[n] for each block.
        boolean changed = true;
        do
        {
            changed = false;
            for(NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
                BasicBlock b = this.cfg.get(nodes.head);
                // get previous iteration
                DataFlowSet<Integer> outPrev = (DataFlowSet<Integer>)outMap.get(b); 
                DataFlowSet<Integer> inPrev = (DataFlowSet<Integer>)inMap.get(b);
                // get IN set using nodes n predecessors using OR join
                DataFlowSet<Integer> pout = new DataFlowSet<>();
                for(NodeList preds = nodes.head.pred(); preds != null; preds = preds.tail) {
                    BasicBlock pb = this.cfg.get(preds.head);
                    pout.or(outMap.get(pb));
                }
                inMap.put(b, pout);
                // get OUT set, IN set 
                DataFlowSet<Integer> gen = (DataFlowSet<Integer>)this.genKillSets.getGen(b); 
                DataFlowSet<Integer> kill = (DataFlowSet<Integer>)this.genKillSets.getKill(b); 
                // create new set for in.
                DataFlowSet<Integer> in = new DataFlowSet<>();
                // in(s)
                in.or(inPrev);
                // in(s) MINUS kill(s)
                in.andNot(kill);
                // gen(s) OR ( in(s) MINUS kill(s) )
                in.or(gen);
                outMap.put(b, in);
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
                DataFlowSet<Integer> inSet = this.getIn(stmList.head);
                printStream.print(" In:");
                printStream.print(inSet);
                DataFlowSet<Integer> outSet = this.getOut(stmList.head);
                printStream.print(" out:");
                printStream.print(outSet);
                printStream.println();
            }
            printStream.println("--------");
        }
    }
}