package com.chaosopher.tigerlang.compiler.dataflow;

import java.io.PrintStream;
import java.util.BitSet;
import java.util.HashMap;

import com.chaosopher.tigerlang.compiler.graph.NodeList;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.tree.PrettyPrinter2;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.tree.TEMP;

public class GenKillSets {

    private final HashMap<Stm, Integer> defs = new HashMap<>();
    private final HashMap<BasicBlock, BitSet> genMap = new HashMap<>();
    private final HashMap<BasicBlock, BitSet> killMap = new HashMap<>();
    private final HashMap<BasicBlock, BitSet> inMap = new HashMap<>();
    private final HashMap<BasicBlock, BitSet> outMap = new HashMap<>();
    private final CFG cfg;

    public GenKillSets(CFG cfg) {
        this.cfg = cfg;
    }

    /**
     * Returns the set of all defs generated in the basic block.
     * @param basicBlock who's gen set we are looking for
     * @return a kill set of definition ids.
     */
    private Integer getKillGenSets(BasicBlock basicBlock) {
        StmList stmList = basicBlock.first;
        BitSet blockGen = new BitSet();
        BitSet blockKill = new BitSet();
        HashMap<Temp, BitSet> defTemps = new HashMap<>();
        for (; stmList != null; stmList = stmList.tail) {
            Stm s = stmList.head;
            // we are only interested in MOVE with a destination
            if(s instanceof MOVE && ((MOVE)s).dst instanceof TEMP) {
                MOVE move = (MOVE)s;
                // get destination temp.
                Temp temp = ((TEMP)move.dst).temp;
                // get the defintion id.
                int defId = this.defs.get(s);
                // add reference for temp and def
                if (!defTemps.containsKey(temp)) {
                    defTemps.put(temp, new BitSet());
                }
                defTemps.get(temp).set(defId);
                // gen(s) = { defId }
                BitSet gen = new BitSet();
                gen.set(defId);
                // kill(s) = defs(t) - { defId }
                BitSet kill = (BitSet)defTemps.get(temp).clone();
                kill.andNot(gen);

                // combine the previous and next statements kill and gen
                // previous item ( gen[p] - kill[n])
                // gen[p] is blockGen
                // gen[n] is gen
                // kill[p] is blockKill
                // kill[n] is kill

                // gen combined
                BitSet comGen = new BitSet();
                comGen.or(gen);
                BitSet comGenOp2 = new BitSet();
                comGenOp2.or(blockGen);
                comGenOp2.andNot(kill);
                comGen.or(comGenOp2);

                // kill combined
                BitSet comKill = new BitSet();
                comKill.or(blockKill);
                comKill.or(kill);

                // assign kill and gen block 
                blockGen = comGen;
                blockKill = comKill;
            }
        }
        genMap.put(basicBlock, blockGen);
        killMap.put(basicBlock, blockKill);
        return 1;
    }

    public void displayGenKill(PrintStream out) {
        for (NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
            out.println("# Block");
            BasicBlock b = this.cfg.get(nodes.head);
            for (StmList stmList = b.first; stmList != null; stmList = stmList.tail) {
                out.print(this.defs.get(stmList.head));
                out.print(":");
                stmList.head.accept(new PrettyPrinter2(System.out));
                out.println();
            }
            BitSet gen = genMap.get(b);
            BitSet kill = killMap.get(b);
            BitSet outSet = outMap.get(b);
            BitSet inSet = inMap.get(b);
            out.println("gen:" + gen);
            out.println("kill:" + kill);
            out.println("in:" + inSet);
            out.println("out:" + outSet);
        }
    }

    //https://karkare.github.io/cs738/lecturenotes/03DataFlowAnalysisHandout.pdf

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
    
    public void generate() {
        genMap.clear();
        killMap.clear();
        // create def ids for each statement.
        int id = 0;
        for(NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
            BasicBlock b = this.cfg.get(nodes.head);
            for (StmList stmList = b.first; stmList != null; stmList = stmList.tail) {
                Stm s = stmList.head;
                this.defs.put(s, id++);
            }
        }
        // calculate gen and kill for each basic block.
        for(NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
            BasicBlock b = this.cfg.get(nodes.head);
            this.getKillGenSets(b);
        }
        // compute in[n] and out[n] for each block.
        for(NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
            BasicBlock b = this.cfg.get(nodes.head);
            inMap.put(b, new BitSet());
            outMap.put(b, new BitSet());
        }
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
                BitSet gen = (BitSet)genMap.get(b).clone(); // get this from the block, clone it first !
                BitSet kill = (BitSet)killMap.get(b).clone(); // get this from the block, clone it first !
                BitSet inDiff = (BitSet)inMap.get(b).clone();
                inDiff.andNot(kill);
                gen.or(inDiff);
                outMap.put(b, gen);
                var c1 = compare(inMap.get(b), inPrev);
                var c2 = compare(outMap.get(b), outPrev);
                changed = changed || c1 != 0 || c2 != 0;
            }
            if(!changed) break;
        } while(true);
    }
}