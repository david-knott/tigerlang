package com.chaosopher.tigerlang.compiler.dataflow.live;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.dataflow.utils.ExtractDefs;
import com.chaosopher.tigerlang.compiler.dataflow.utils.ExtractUses;
import com.chaosopher.tigerlang.compiler.graph.NodeList;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.LABEL;
import com.chaosopher.tigerlang.compiler.tree.QuadruplePrettyPrinter;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;


/**
 * Gen kill sets for liveness analysis.
 */
public class GenKillSets {
    
    public static GenKillSets analyse(CFG cfg) {
        GenKillSets genKillSets = new GenKillSets(cfg);
        genKillSets.generate();
        return genKillSets;
    }

    private final HashMap<Stm, Integer> defs = new HashMap<>();
    private final HashMap<BasicBlock, Set<Temp>> genMap = new HashMap<>();
    private final HashMap<BasicBlock, Set<Temp>> killMap = new HashMap<>();
    private final CFG cfg;

    private GenKillSets(CFG cfg) {
        this.cfg = cfg;
    }

    public Set<Temp> getKill(BasicBlock basicBlock) {
        return this.killMap.get(basicBlock);
    }

	public Set<Temp> getKill( Stm stm) {
        Set<Temp> killBlock = new HashSet<>();
        return this.getKill(killBlock, stm);
    }

    public Set<Temp> getKill(Set<Temp> kill, Stm s) {
        Set<Temp> defs = ExtractDefs.getDefs(s);
        kill.addAll(defs);
        return kill; 
	}

    /**
     * Returns the set of all temporaries 
     * @param basicBlock
     * @return
     */
    public Set<Temp> getGen(BasicBlock basicBlock) {
        return this.genMap.get(basicBlock);
    }

	public Set<Temp> getGen(Stm s) {
        Set<Temp> gen = new HashSet<>();
        return this.getGen(gen, s);
    }

	public Set<Temp> getGen(Set<Temp> gen, Stm s) {
        // kill any previous items
        gen.removeAll(this.getKill(s));
        // add new gens to to set.
        gen.addAll(ExtractUses.getUses(s));
        return gen;
	}
 
    private void generate() {
        HashMap<BasicBlock, Set<Temp>> genMap = new HashMap<>();
        HashMap<BasicBlock, Set<Temp>> killMap = new HashMap<>();
    
        genMap.clear();
        killMap.clear();
        // create def ids for each statement that is not a label.
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
        // find all expressions
        for (NodeList nodes = this.cfg.nodes().reverse(); nodes != null; nodes = nodes.tail) {
            BasicBlock basicBlock = this.cfg.get(nodes.head);
            for (StmList stmList = basicBlock.first; stmList != null; stmList = stmList.tail) {
                this.initExpressions(stmList.head);
            }
        }
        // calculate kill set for each basic block.
        for(NodeList nodes = this.cfg.nodes().reverse(); nodes != null; nodes = nodes.tail) {
            BasicBlock b = this.cfg.get(nodes.head);
            this.calculateKillSet(b);
        }
        // calculate gen set for each basic block.
        for(NodeList nodes = this.cfg.nodes().reverse(); nodes != null; nodes = nodes.tail) {
            BasicBlock b = this.cfg.get(nodes.head);
            this.calculateGenSet(b);
        }
    }

    private void calculateGenSet(BasicBlock basicBlock) {
        // reverse statements in basic block.
        StmList stmListRev = null;
        for(StmList stmList = basicBlock.first; stmList != null; stmList = stmList.tail) {
            stmListRev = new StmList(stmList.head, stmListRev);
        }
        Set<Temp> genBlock = new HashSet<>();
        for (; stmListRev != null; stmListRev = stmListRev.tail) {
            Stm s = stmListRev.head;
            this.getGen(genBlock, s);
        }
        genMap.put(basicBlock, genBlock);
    }

    private void calculateKillSet(BasicBlock basicBlock) {
        // reverse statements in basic block.
        StmList stmListRev = null;
        for(StmList stmList = basicBlock.first; stmList != null; stmList = stmList.tail) {
            stmListRev = new StmList(stmList.head, stmListRev);
        }
        Set<Temp> killBlock = new HashSet<>();
        for (; stmListRev != null; stmListRev = stmListRev.tail) {
            this.getKill(killBlock, stmListRev.head);
        }
        killMap.put(basicBlock, killBlock);
    }

    private void initExpressions(Stm head) {
    }

    public void serialize(PrintStream printStream) {
        for(NodeList nodeList = this.cfg.nodes(); nodeList != null; nodeList = nodeList.tail) {
            printStream.println("## Block ##");
            BasicBlock basicBlock = this.cfg.get(nodeList.head);
            Set<Temp> genBlock = this.getGen(basicBlock);
            printStream.print("gen: { ");
            for(Temp genB : genBlock) {
                printStream.print(genB);
                printStream.print(", ");
            }
            printStream.print("}");
            Set<Temp> killBlock = this.getKill(basicBlock);
            printStream.print(" kill: { ");
            for(Temp killB : killBlock) {
                printStream.print(killB);
                printStream.print(", ");
            }
            printStream.print("}");
            printStream.println();
            printStream.println("### Statements ###");
            for(StmList stmList = basicBlock.first; stmList != null; stmList = stmList.tail) {
            //    Integer defId = this.getDefinitionId(stmList.head);
             //   printStream.print(defId + ":");
                stmList.head.accept(new QuadruplePrettyPrinter(printStream));
                Set<Temp> genStatement = this.getGen(stmList.head);
                printStream.print(" gen: { ");
                for(Temp genS : genStatement) {
                    printStream.print(genS);
                    printStream.print(", ");
                }
                printStream.print("}");
                Set<Temp> killStatement = this.getKill(stmList.head);
                printStream.print(" kill: { ");
                for(Temp killS : killStatement) {
                    printStream.print(killS);
                    printStream.print(", ");
                }
                printStream.print("}");
                printStream.println();
            }
            printStream.println("--------");
        }
    }
}
