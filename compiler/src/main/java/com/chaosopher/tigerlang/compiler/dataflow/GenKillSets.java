package com.chaosopher.tigerlang.compiler.dataflow;

import java.util.BitSet;
import java.util.HashMap;

import com.chaosopher.tigerlang.compiler.graph.NodeList;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.tree.TEMP;

public class GenKillSets {

    private final CFG cfg;

    public GenKillSets(CFG cfg) {
        this.cfg = cfg;
    }

    private HashMap<Stm, Integer> defs = new HashMap<>();

    /**
     * Returns the set of all defs generated in the basic block.
     * @param basicBlock who's gen set we are looking for
     * @return a kill set of definition ids.
     */
    private Integer getKillGenSets(BasicBlock basicBlock, HashMap<BasicBlock, BitSet> genMap, HashMap<BasicBlock, BitSet> killMap) {
        StmList stmList = basicBlock.first;
        BitSet blockGen = new BitSet();
        BitSet blockKill = new BitSet();
        HashMap<Temp, BitSet> defTemps = new HashMap<>();
        for (; stmList != null; stmList = stmList.tail) {
            Stm s = stmList.head;
            // we are only interested in MOVEs
            if(s instanceof MOVE) {
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

                /*
                BitSet comGen = new BitSet();
                comGen.and(gen);
                BitSet comGenOp2 = new BitSet();
                comGenOp2.and(blockGen);
                comGenOp2.andNot(kill);

                BitSet comKill = new BitSet();
                comKill.and(blockKill);
                comKill.and(kill);
                */

            }
        }
        System.out.println("blockGen " + blockGen);
        System.out.println("blockKill" + blockKill);
        genMap.put(basicBlock, blockGen);
        killMap.put(basicBlock, blockKill);
        return 1;
    }

    //https://karkare.github.io/cs738/lecturenotes/03DataFlowAnalysisHandout.pdf

    
    public void generate() {
        // create def ids for each statement.
        int id = 0;
        for(NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
            BasicBlock b = this.cfg.get(nodes.head);
            for (StmList stmList = b.first; stmList != null; stmList = stmList.tail) {
                Stm s = stmList.head;
                System.out.println("adding " + s);
                this.defs.put(s, id++);
            }
        }
        
        HashMap<BasicBlock, BitSet> genMap = new HashMap<>();
        HashMap<BasicBlock, BitSet> killMap = new HashMap<>();
        // gen, kill for each block
        // for moves use the temporary references to calculate gen & kill
        for(NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
            BasicBlock b = this.cfg.get(nodes.head);
            this.getKillGenSets(b, genMap, killMap);
        }

        // compute in[n] and out[n] for each block.
        HashMap<BasicBlock, BitSet> inMap = new HashMap<>();
        HashMap<BasicBlock, BitSet> outMap = new HashMap<>();
        for(NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
            BasicBlock b = this.cfg.get(nodes.head);
            inMap.put(b, new BitSet());
            outMap.put(b, new BitSet());
        }
        for(NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
            BitSet pout = new BitSet();
            for(NodeList pred = nodes.head.pred(); pred != null; pred = pred.tail) {
                BasicBlock pb = this.cfg.get(pred.head);
                pout.and(outMap.get(pb));
            }
            BasicBlock b = this.cfg.get(nodes.head);
            inMap.put(b, pout);
            BitSet gen = (BitSet)genMap.get(b).clone(); // get this from the block, clone it first !
            BitSet kill = (BitSet)killMap.get(b).clone(); // get this from the block, clone it first !
            BitSet inDiff = (BitSet)inMap.get(b).clone();
            inDiff.andNot(kill);
            gen.and(inDiff);
            outMap.put(b, gen);
        }
        for(NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
            BasicBlock b = this.cfg.get(nodes.head);
            System.out.print(b.label);
            System.out.print(" in");
            System.out.print(inMap.get(b));
            System.out.println(" out");
            System.out.println(outMap.get(b));

        }
 

    }
}