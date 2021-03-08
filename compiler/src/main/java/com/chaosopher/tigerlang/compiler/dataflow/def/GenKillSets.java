package com.chaosopher.tigerlang.compiler.dataflow.def;

import java.io.PrintStream;
import java.util.HashMap;

import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.dataflow.DataFlowSet;
import com.chaosopher.tigerlang.compiler.graph.NodeList;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.LABEL;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.QuadruplePrettyPrinter;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.tree.TEMP;

public class GenKillSets {

    public static GenKillSets analyse(CFG cfg) {
        GenKillSets genKillSets = new GenKillSets(cfg);
        genKillSets.generate();
        return genKillSets;
    }

    private final HashMap<Stm, Integer> defs = new HashMap<>();
    private final HashMap<Integer, Stm> revDefs = new HashMap<>();
    private final HashMap<Temp, DataFlowSet<Integer>> defTemps = new HashMap<>();
    private final HashMap<BasicBlock, DataFlowSet<Integer>> genMap = new HashMap<>();
    private final HashMap<BasicBlock, DataFlowSet<Integer>> killMap = new HashMap<>();
    private final CFG cfg;

    private GenKillSets(CFG cfg) {
        this.cfg = cfg;
    }
 
    boolean isDefinition(Stm stm) {
        return (stm instanceof MOVE && ((MOVE)stm).dst instanceof TEMP);
    }

    DataFlowSet<Integer> getGen(Integer defId) {
        return this.getGen(this.revDefs.get(defId));
    }

    DataFlowSet<Integer> getGen(BasicBlock basicBlock) {
        return this.genMap.get(basicBlock);
    }

	DataFlowSet<Integer> getGen(Stm s) {
        DataFlowSet<Integer> gen = new DataFlowSet<>();
        return this.getGen(gen, s);
    }

	private DataFlowSet<Integer> getGen(DataFlowSet<Integer> genBlock, Stm s) {
        if(this.isDefinition(s)) {
            MOVE move = (MOVE)s;
            // get destination temp.
            Temp temp = ((TEMP)move.dst).temp;
            // get the defintion id for the statement.
            int defId = this.defs.get(s);
            // remove any previous definition of gen from gen set
            genBlock.andNot(defTemps.get(temp));
            // add defId to gen set for this block.
            genBlock.add(defId);
            // add key temp -> [defId], used in kill phase.
            this.defTemps.get(temp).add(defId);
            // add refernce to gen map for statements
        }
        return genBlock;
	}

    DataFlowSet<Integer> getKill(Integer defId) {
        return this.getKill(this.revDefs.get(defId));
    }

    DataFlowSet<Integer> getKill(BasicBlock basicBlock) {
        return this.killMap.get(basicBlock);
    }

	DataFlowSet<Integer> getKill(Stm stm) {
        DataFlowSet<Integer> kill = new DataFlowSet<>();
        return getKill(kill, stm);
    }

	private DataFlowSet<Integer> getKill(DataFlowSet<Integer> kill, Stm stm) {
        if(this.isDefinition(stm)) {
            MOVE move = (MOVE) stm;// get destination temp.
            Temp temp = ((TEMP) move.dst).temp;
            int defId = this.defs.get(stm);
            // all other definitions except this are killed
            DataFlowSet<Integer> redefinitions = (DataFlowSet<Integer>)this.defTemps.get(temp);
            kill.or(redefinitions);
            kill.remove(defId);
        }
        return kill; 
	}

    Integer getDefinitionId(Stm stm) {
        return this.defs.get(stm);
    }

    Stm getDefinition(Integer defId) {
        return this.revDefs.get(defId);
    }
    
    DataFlowSet<Integer> getDefinitions(Temp temp) {
        return this.defTemps.get(temp);
    }

    private void initSets(BasicBlock basicBlock) {
        StmList stmList = basicBlock.first;
        for (; stmList != null; stmList = stmList.tail) {
            Stm s = stmList.head;
            // we are only interested in MOVE with a destination thats a temp.
            if(this.isDefinition(s)) {
                MOVE move = (MOVE)s;
                // get destination temp.
                Temp temp = ((TEMP)move.dst).temp;
                // add reference for temp and def, used in kill phase.
                defTemps.put(temp, new DataFlowSet<Integer>());
            }
        }
    }

    private void calculateGenSet(BasicBlock basicBlock) {
        StmList stmList = basicBlock.first;
        DataFlowSet<Integer> genBlock = new DataFlowSet<>();
        for (; stmList != null; stmList = stmList.tail) {
            Stm s = stmList.head;
            this.getGen(genBlock, s);
        }
        genMap.put(basicBlock, genBlock);
    }

    private void calculateKillSet(BasicBlock basicBlock) {
        StmList stmList = basicBlock.first;
        DataFlowSet<Integer> killBlock = new DataFlowSet<>();
        for (; stmList != null; stmList = stmList.tail) {
            Stm s = stmList.head;
            this.getKill(killBlock, s);
        }
        killMap.put(basicBlock, killBlock);
    }

    private void generate() {
        genMap.clear();
        killMap.clear();
        // create def ids for each statement that is not a label.
        int id = 1;
        for(NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
            BasicBlock b = this.cfg.get(nodes.head);
            for (StmList stmList = b.first; stmList != null; stmList = stmList.tail) {
                Stm s = stmList.head;
                if(!(s instanceof LABEL)) {
                    this.defs.put(s, id);
                    this.revDefs.put(id, s);
                    id++;
                }
            }
        }
        // initialise empty sets.
        for(NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
            BasicBlock b = this.cfg.get(nodes.head);
            this.initSets(b);
        }
        // calculate gen set for each basic block.
        for(NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
            BasicBlock b = this.cfg.get(nodes.head);
            this.calculateGenSet(b);
        }
        // calculate kill set for each basic block.
        for(NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
            BasicBlock b = this.cfg.get(nodes.head);
            this.calculateKillSet(b);
        }
    }

    public void serialize(PrintStream printStream) {
        for(NodeList nodeList = this.cfg.nodes(); nodeList != null; nodeList = nodeList.tail) {
            printStream.println("## Block ##");
            BasicBlock basicBlock = this.cfg.get(nodeList.head);
            printStream.print(basicBlock.hashCode() + "");
            DataFlowSet<Integer> gen = this.getGen(basicBlock);
            printStream.print(" gen:");
            printStream.print(gen);
            DataFlowSet<Integer> kill = this.getKill(basicBlock);
            printStream.print(" kill:");
            printStream.print(kill);
            printStream.println();
            printStream.println("### Statements ###");
            for(StmList stmList = basicBlock.first; stmList != null; stmList = stmList.tail) {
                Integer defId = this.getDefinitionId(stmList.head);
                printStream.print(defId + ":");
                stmList.head.accept(new QuadruplePrettyPrinter(printStream));
                DataFlowSet<Integer> genStatement = this.getGen(stmList.head);
                printStream.print(" gen:");
                printStream.print(genStatement);
                DataFlowSet<Integer> killStatement = this.getKill(stmList.head);
                printStream.print(" kill:");
                printStream.print(killStatement);
                printStream.println();
            }
            printStream.println("--------");
        }
    }
}