package com.chaosopher.tigerlang.compiler.dataflow;

import java.io.PrintStream;
import java.util.BitSet;
import java.util.HashMap;

import javax.swing.plaf.basic.BasicHTML;

import com.chaosopher.tigerlang.compiler.tree.LABEL;
import com.chaosopher.tigerlang.compiler.graph.NodeList;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.QuadruplePrettyPrinter;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.tree.TEMP;
import com.chaosopher.tigerlang.compiler.util.Assert;


public class GenKillSets {

    private final HashMap<Stm, Integer> defs = new HashMap<>();
    private final HashMap<Temp, BitSet> defTemps = new HashMap<>();
    private final HashMap<BasicBlock, BitSet> genMap = new HashMap<>();
    private final HashMap<BasicBlock, BitSet> killMap = new HashMap<>();
    private final HashMap<BasicBlock, BitSet> inMap = new HashMap<>();
    private final HashMap<BasicBlock, BitSet> outMap = new HashMap<>();
    private final HashMap<Stm, BasicBlock> blockReference = new HashMap<>();
    private final CFG cfg;
    private final int maxIterations;
    private int totalIterations;

    public GenKillSets(CFG cfg) {
        this.cfg = cfg;
        this.maxIterations = -1;
        this.generate();
    }
   
    GenKillSets(CFG cfg, int maxIterations) {
        this.cfg = cfg;
        this.maxIterations = maxIterations;
        this.generate();
    }
 
    public int getIterations() {
        return Math.abs(this.totalIterations);
    }

    public CFG getCfg() {
        return this.cfg;
    }

    public BitSet getKill(BasicBlock basicBlock) {
        return this.killMap.get(basicBlock);
    }

    private boolean isDefinition(Stm stm) {
        return (stm instanceof MOVE && ((MOVE)stm).dst instanceof TEMP);
    }

	public BitSet getKill(Stm stm) {
        BitSet kill = new BitSet();
        if(this.isDefinition(stm)) {
            MOVE move = (MOVE) stm;// get destination temp.
            Temp temp = ((TEMP) move.dst).temp;
            int defId = this.defs.get(stm);
            // all other definitions except this are killed
            BitSet redefinitions = (BitSet)defTemps.get(temp).clone();
            redefinitions.set(defId, false);
            kill.or(redefinitions);
        }
        return kill; 
	}

    public BitSet getGen(BasicBlock basicBlock) {
        return this.genMap.get(basicBlock);
    }

	public BitSet getGen(Stm head) {
        BitSet gen = new BitSet();
        if(this.isDefinition(head)) {
            // get the defintion id for the statement.
            int defId = this.defs.get(head);
            // add defId to gen set for this block.
            gen.set(defId);
        }
        return gen;
	}

    public BitSet getIn(BasicBlock basicBlock) {
        return this.inMap.get(basicBlock);
    }

    public BitSet getOut(BasicBlock basicBlock) {
        return this.outMap.get(basicBlock);
    }

    public Integer getDefinitionId(Stm stm) {
        return this.defs.get(stm);
    }
    
    public BitSet getDefinitions(Temp temp) {
        return this.defTemps.get(temp);
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
            // we are only interested in MOVE with a destination thats a temp.
            if(s instanceof MOVE && ((MOVE)s).dst instanceof TEMP) {
                // reconstitute kill and gen from in.
                BitSet gen = this.getGen(s);
                // current block generates these
                BitSet kill = this.getKill(s);
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
            if(s instanceof MOVE && ((MOVE)s).dst instanceof TEMP) {
                // reconstitute kill and gen from in.
                BitSet gen = this.getGen(s);
                // current block generates these
                BitSet kill = this.getKill(s);
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



    /*
    public BitSet getOut(Stm stm) {
        BasicBlock basicBlock = this.blockReference.get(stm);
        StmList stmList = basicBlock.first;
        // set of all definitions that reach the start of this block
        BitSet blockIn = (BitSet)(this.inMap.get(basicBlock).clone());
        HashMap<Temp, BitSet> blockDefTemps = new HashMap<>();
        for (; stmList != null; stmList = stmList.tail) {
            // for each statement, we get the gen and kill
            Stm s = stmList.head;
            // we are only interested in MOVE with a destination thats a temp.
            if(s instanceof MOVE && ((MOVE)s).dst instanceof TEMP) {
                // reconstitute kill and gen from in.
                MOVE move = (MOVE)s;
                // get destination temp.
                Temp temp = ((TEMP)move.dst).temp;
                // get the defintion id for the statement.
                int defId = this.defs.get(s);
                // this statement generates a new def
                blockIn.set(defId);
                // remove any previous definition of temp in this block.
                blockIn.andNot(blockDefTemps.get(temp)); 
                // add new def to set of defs for temp.
                if(!blockDefTemps.containsKey(temp)) {
                    blockDefTemps.put(temp, new BitSet());
                }
                blockDefTemps.get(temp).set(defId);
            }

            
        }
        Assert.unreachable("Statment was not contained in any block");
        return null;
    }

    public BitSet getIn(Stm stm) {
        BasicBlock basicBlock = this.blockReference.get(stm);
        StmList stmList = basicBlock.first;
        // set of all definitions that reach the start of this block
        BitSet blockIn = (BitSet)(this.inMap.get(basicBlock).clone());
        BitSet blockOut = (BitSet)blockIn.clone();
        HashMap<Temp, BitSet> blockDefTemps = new HashMap<>();
        for (; stmList != null; stmList = stmList.tail) {
            // for each statement, we get the gen and kill
            Stm s = stmList.head;
            // we are only interested in MOVE with a destination thats a temp.
            if(s instanceof MOVE && ((MOVE)s).dst instanceof TEMP) {
                // reconstitute kill and gen from in set.
                MOVE move = (MOVE)s;
                // get destination temp.
                Temp temp = ((TEMP)move.dst).temp;
                // get the defintion id for the statement.
                int defId = this.defs.get(s);
                // this statement generates a new def
                blockOut.set(defId);
                // remove any previous definition of temp in this block.
                blockOut.andNot(blockDefTemps.get(temp)); 
                // add new def to set of defs for temp.
                if(!blockDefTemps.containsKey(temp)) {
                    blockDefTemps.put(temp, new BitSet());
                }
                blockDefTemps.get(temp).set(defId);
                
            }

            if(s == stm) {
                return blockIn;
            }
        }
        Assert.unreachable("Statment was not contained in any block");
        return null;
    }*/

    private void initSets(BasicBlock basicBlock) {
        StmList stmList = basicBlock.first;
        for (; stmList != null; stmList = stmList.tail) {
            this.blockReference.put(stmList.head, basicBlock);
            Stm s = stmList.head;
            // we are only interested in MOVE with a destination thats a temp.
            if(this.isDefinition(s)) {
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
            if(this.isDefinition(s)) {
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
                // add refernce to gen map for statements
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
            if(this.isDefinition(s)) {
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
            this.totalIterations++;
            if(!changed || this.maxIterations == this.totalIterations) break;
        } while(true);
    }
}