package com.chaosopher.tigerlang.compiler.dataflow;

import java.io.PrintStream;
import java.util.BitSet;
import java.util.HashMap;

import com.chaosopher.tigerlang.compiler.tree.LABEL;
import com.chaosopher.tigerlang.compiler.graph.NodeList;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.QuadruplePrettyPrinter;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.tree.TEMP;

public class GenKillSets {

    private final HashMap<Stm, Integer> defs = new HashMap<>();
    private final HashMap<Temp, BitSet> defTemps = new HashMap<>();
    private final HashMap<BasicBlock, BitSet> genMap = new HashMap<>();
    private final HashMap<BasicBlock, BitSet> killMap = new HashMap<>();
    private final HashMap<BasicBlock, BitSet> inMap = new HashMap<>();
    private final HashMap<BasicBlock, BitSet> outMap = new HashMap<>();
    private final CFG cfg;

    public GenKillSets(CFG cfg) {
        this.cfg = cfg;
    }

    private void initSets(BasicBlock basicBlock) {
        StmList stmList = basicBlock.first;
        for (; stmList != null; stmList = stmList.tail) {
            Stm s = stmList.head;
            // we are only interested in MOVE with a destination thats a temp.
            if(s instanceof MOVE && ((MOVE)s).dst instanceof TEMP) {
                MOVE move = (MOVE)s;
                // get destination temp.
                Temp temp = ((TEMP)move.dst).temp;
                // add reference for temp and def
                defTemps.put(temp, new BitSet());
            }
        }
    }

    private void calculateGenSet(BasicBlock basicBlock) {
        StmList stmList = basicBlock.first;
        BitSet genBlock = new BitSet();
        for (; stmList != null; stmList = stmList.tail) {
            Stm s = stmList.head;
            // we are only interested in MOVE with a destination thats a temp.
            if(s instanceof MOVE && ((MOVE)s).dst instanceof TEMP) {
                MOVE move = (MOVE)s;
                // get destination temp.
                Temp temp = ((TEMP)move.dst).temp;
                // get the defintion id for the statement.
                int defId = this.defs.get(s);
                // add defId to gen set for this block.
                genBlock.set(defId);
                // remove any previous definition of gen from gen set
                genBlock.andNot(defTemps.get(temp));
                // add key temp -> [defId] 
                defTemps.get(temp).set(defId);
            }
        }
        genMap.put(basicBlock, genBlock);
    }

    /**
     * Generate a kill set.
     * @param basicBlock
     */
    private void calculateKillSet(BasicBlock basicBlock) {
        StmList stmList = basicBlock.first;
        BitSet killBlock = new BitSet();
        for (; stmList != null; stmList = stmList.tail) {
            Stm s = stmList.head;
            // kill set for this block is all redefinitions of
            // previously seen facts ( gen ) minus generated 
            // items in this block.
            if(s instanceof MOVE && ((MOVE)s).dst instanceof TEMP) {
                MOVE move = (MOVE)s;// get destination temp.
                Temp temp = ((TEMP)move.dst).temp;
                int defId = this.defs.get(s);
                // redefinitions.
                BitSet redefinitions = (BitSet)defTemps.get(temp).clone();
                redefinitions.set(defId, false);
                killBlock.or(redefinitions);

            }
        }
        killMap.put(basicBlock, killBlock);
    }

    public void displayGenKill(PrintStream out) {
        for (NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
            out.println("# Block");
            BasicBlock b = this.cfg.get(nodes.head);
            for (StmList stmList = b.first; stmList != null; stmList = stmList.tail) {
                out.print(this.defs.get(stmList.head));
                out.print(":");
                stmList.head.accept(new QuadruplePrettyPrinter(System.out));
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
        int id = 1;
        for(NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
            BasicBlock b = this.cfg.get(nodes.head);
            for (StmList stmList = b.first; stmList != null; stmList = stmList.tail) {
                Stm s = stmList.head;
                if(!(s instanceof LABEL)) {
                    this.defs.put(s, id++);
                }
            }
        }
        // calculate gen and kill for each basic block.
        for(NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
            BasicBlock b = this.cfg.get(nodes.head);
            this.initSets(b);
        }
        // calculate gen and kill for each basic block.
        for(NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
            BasicBlock b = this.cfg.get(nodes.head);
            this.calculateGenSet(b);
        }
        for(NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
            BasicBlock b = this.cfg.get(nodes.head);
            this.calculateKillSet(b);
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