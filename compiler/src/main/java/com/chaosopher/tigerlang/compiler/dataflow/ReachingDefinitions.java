package com.chaosopher.tigerlang.compiler.dataflow;

import java.util.BitSet;
import java.util.HashMap;

import com.chaosopher.tigerlang.compiler.graph.NodeList;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.util.Assert;

class ReachingDefinitions {

    private final CFG cfg;
    private final GenKillSets genKillSets;
    private final HashMap<Stm, BasicBlock> blockReference = new HashMap<>();
    private final HashMap<BasicBlock, BitSet> inMap = new HashMap<>();
    private final HashMap<BasicBlock, BitSet> outMap = new HashMap<>();
    private final int maxIterations = 10;
    private int totalIterations;

    public ReachingDefinitions(CFG cfg, GenKillSets genKillSets) {
        this.cfg = cfg;
        this.genKillSets = genKillSets;
        this.generate();
    }

    public BitSet getIn(BasicBlock basicBlock) {
        return this.inMap.get(basicBlock);
    }

    public BitSet getOut(BasicBlock basicBlock) {
        return this.outMap.get(basicBlock);
    }

	public BitSet getIn(Stm stm) {
        BasicBlock basicBlock = this.blockReference.get(stm);
        StmList stmList = basicBlock.first;
        // set of all definitions that reach the start of this block
        BitSet blockIn = (BitSet)(this.inMap.get(basicBlock).clone());
        for (; stmList != null; stmList = stmList.tail) {
            // for each statement, we get the gen and kill
            Stm s = stmList.head;
            if(s == stm) {
                return blockIn;
            }
            if(this.genKillSets.isDefinition(s)) {
                // reconstitute kill and gen from in.
                BitSet gen = this.genKillSets.getGen(s);
                // current block generates these
                BitSet kill = this.genKillSets.getKill(s);
                blockIn.andNot(kill);
                blockIn.or(gen);
            }
            
        }
        Assert.unreachable("Statment was not contained in any block");
        return null;
	}

	public BitSet getOut(Stm stm) {
        BasicBlock basicBlock = this.blockReference.get(stm);
        StmList stmList = basicBlock.first;
        // set of all definitions that reach the start of this block
        BitSet blockIn = (BitSet)(this.inMap.get(basicBlock).clone());
        for (; stmList != null; stmList = stmList.tail) {
            // for each statement, we get the gen and kill
            Stm s = stmList.head;
            // we are only interested in MOVE with a destination thats a temp.
            if(this.genKillSets.isDefinition(s)) {
                // reconstitute kill and gen from in.
                BitSet gen = this.genKillSets.getGen(s);
                // current block generates these
                BitSet kill = this.genKillSets.getKill(s);
                blockIn.andNot(kill);
                blockIn.or(gen);
            }
            if(s == stm) {
                return blockIn;
            }
        }
        Assert.unreachable("Statment was not contained in any block");
        return null;
	}

    public void generate() {
        // initialise in[n] and out[n] to be empty sets. 
        for(NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
            BasicBlock b = this.cfg.get(nodes.head);
            inMap.put(b, new BitSet());
            outMap.put(b, new BitSet());
        }
        // compute in[n] and out[n] for each block.
        boolean changed = true;
        do
        {
            changed = false;
            for(NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
                BasicBlock b = this.cfg.get(nodes.head);
                BitSet outPrev = (BitSet)outMap.get(b).clone(); 
                BitSet inPrev = (BitSet)inMap.get(b).clone();
                BitSet pout = new BitSet();
                for(NodeList preds = nodes.head.pred(); preds != null; preds = preds.tail) {
                    BasicBlock pb = this.cfg.get(preds.head);
                    pout.or(outMap.get(pb));
                }
                inMap.put(b, pout);
                BitSet gen = (BitSet)this.genKillSets.getGen(b).clone(); // get this from the block, clone it first !
                BitSet kill = (BitSet)this.genKillSets.getKill(b).clone(); // get this from the block, clone it first !
                BitSet inDiff = (BitSet)inMap.get(b).clone();
                inDiff.andNot(kill);
                gen.or(inDiff);
                outMap.put(b, gen);
                var c1 = compare(inMap.get(b), inPrev);
                var c2 = compare(outMap.get(b), outPrev);
                changed = changed || c1 != 0 || c2 != 0;
            }
            this.totalIterations++;
            if(!changed || this.maxIterations == this.totalIterations) break;
        } while(true);
    }
    
    private int compare(BitSet lhs, BitSet rhs) {
        if (lhs.equals(rhs))
            return 0;
        BitSet xor = (BitSet) lhs.clone();
        xor.xor(rhs);
        int firstDifferent = xor.length() - 1;
        if (firstDifferent == -1)
            return 0;
        return rhs.get(firstDifferent) ? 1 : -1;
    }
}