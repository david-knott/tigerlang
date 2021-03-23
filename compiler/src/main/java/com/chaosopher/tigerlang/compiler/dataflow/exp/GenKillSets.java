package com.chaosopher.tigerlang.compiler.dataflow.exp;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.dataflow.utils.ExtractDefs;
import com.chaosopher.tigerlang.compiler.dataflow.utils.ExtractExp;
import com.chaosopher.tigerlang.compiler.dataflow.utils.ExtractMemExp;
import com.chaosopher.tigerlang.compiler.dataflow.utils.ExtractUses;
import com.chaosopher.tigerlang.compiler.graph.NodeList;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.BINOP;
import com.chaosopher.tigerlang.compiler.tree.CALL;
import com.chaosopher.tigerlang.compiler.tree.EXP;
import com.chaosopher.tigerlang.compiler.tree.Exp;
import com.chaosopher.tigerlang.compiler.tree.LABEL;
import com.chaosopher.tigerlang.compiler.tree.MEM;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.QuadruplePrettyPrinter;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.tree.TEMP;
import com.chaosopher.tigerlang.compiler.util.Assert;

/**
 * t <- b * c,  gen : { b * c, } - kill(s), kill : expressions with t
 * t <- M[b],  gen : { M[b], } - kill(s), kill : expressions with t
 * M[a] <- b,  gen : {,} , kill : expressions of form M[x]
 * f(a1, a2, ), gen : {,}, kill : expresssions of form M[x]
 * t <- f(a1, a2, ), gen : {,}, kill : expressions with t and expresssions of form M[x]
 */
class GenKillSets {

    public static GenKillSets analyse(CFG cfg) {
        GenKillSets genKillSets = new GenKillSets(cfg);
        genKillSets.generate();
        return genKillSets;
    }

    private final Set<Exp> mems = new HashSet<>();
    private final HashMap<Stm, Integer> defs = new HashMap<>();
    private final HashMap<Temp, Set<Exp>> defTemps = new HashMap<>();
    private final HashMap<BasicBlock, Set<Exp>> genMap = new HashMap<>();
    private final HashMap<BasicBlock, Set<Exp>> killMap = new HashMap<>();
    private final CFG cfg;

    private GenKillSets(CFG cfg) {
        this.cfg = cfg;
    }
 
    private void initialize(Stm stm) {
        Set<Temp> uses = ExtractUses.getUses(stm);
        Set<Temp> defs = ExtractDefs.getDefs(stm);
        // initialize hashmap with empty sets for all temps we will encounter.
        Set<Temp> all = new HashSet<>();
        all.addAll(uses);
        all.addAll(defs);
        for(Temp temp : all) {
            if(!this.defTemps.containsKey(temp)) {
                this.defTemps.put(temp, new HashSet<>());
            }
        }
        // if stm contains an expression, map use temp to expression.
        Exp exp = ExtractExp.getExp(stm);
        if(exp != null) {
            for(Temp temp : uses) {
                this.defTemps.get(temp).add(exp);
            }
        }
        // if statment contains a memory expression, save reference to it.
        boolean isMemExp = ExtractMemExp.isMemExp(stm);
        if(isMemExp) {
            this.mems.add(exp);
        }
    }

    private Set<Exp> getExpressionsUsingTemp(Temp exp) {
        Set<Exp> exps = this.defTemps.get(exp);
        return exps;
    }

    private Set<Exp> getExpressionsUsingMem() {
        return this.mems;
    }

    public Set<Exp> getKill(BasicBlock basicBlock) {
        return this.killMap.get(basicBlock);
    }

	public Set<Exp> getKill( Stm stm) {
        Set<Exp> killBlock = new HashSet<>();
        return this.getKill(killBlock, stm);
    }

    public Set<Exp> getKill(Set<Exp> kill, Stm s) {
        if(s instanceof MOVE) {
            MOVE move = (MOVE)s;
            if(move.dst instanceof TEMP && move.src instanceof BINOP) {
                Temp temp = ((TEMP)move.dst).temp;
                kill.addAll(this.getExpressionsUsingTemp(temp));
            }
            if(move.dst instanceof TEMP && move.src instanceof MEM) {
                Temp temp = ((TEMP)move.dst).temp;
                kill.addAll(this.getExpressionsUsingTemp(temp));
            }
            if(move.dst instanceof MEM && move.src instanceof TEMP ){   
                kill.addAll(this.getExpressionsUsingMem());
            }
            if(move.dst instanceof TEMP && move.src instanceof CALL) {
                Temp temp = ((TEMP)move.dst).temp;
                kill.addAll(this.getExpressionsUsingMem());
                kill.addAll(this.getExpressionsUsingTemp(temp));
            }
        }
        if(s instanceof EXP) {
            EXP exp = (EXP)s;
            if(exp.exp instanceof CALL) {
                kill.addAll(this.getExpressionsUsingMem());
            }
        }
        return kill; 
	}

    public Set<Exp> getGen(BasicBlock basicBlock) {
        return this.genMap.get(basicBlock);
    }

	public Set<Exp> getGen(Stm s) {
        Set<Exp> gen = new HashSet<>();
        return this.getGen(gen, s);
    }

	public Set<Exp> getGen(Set<Exp> gen, Stm s) {
        if(s instanceof MOVE) {
            MOVE move = (MOVE)s;
            if(move.dst instanceof TEMP && move.src instanceof BINOP) {
                gen.add(move.src);
                gen.removeAll(this.getKill(s));
            }
            if(move.dst instanceof TEMP && move.src instanceof MEM) {
                gen.add(move.src);
                gen.removeAll(this.getKill(s));
            }
        }
        return gen;
	}

    public Integer getDefinitionId(Stm stm) {
        return this.defs.get(stm);
    }
    
    public Set<Exp> getDefinitions(Temp temp) {
        return this.defTemps.get(temp);
    }

    private void calculateGenSet(BasicBlock basicBlock) {
        StmList stmList = basicBlock.first;
        Set<Exp> genBlock = new HashSet<>();
        for (; stmList != null; stmList = stmList.tail) {
            Stm s = stmList.head;
            this.getGen(genBlock, s);
        }
        genMap.put(basicBlock, genBlock);
    }

    private void calculateKillSet(BasicBlock basicBlock) {
        StmList stmList = basicBlock.first;
        Set<Exp> killBlock = new HashSet<>();
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
                    this.defs.put(s, id++);
                }
            }
        }
        // find all expressions
        for (NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
            BasicBlock basicBlock = this.cfg.get(nodes.head);
            for (StmList stmList = basicBlock.first; stmList != null; stmList = stmList.tail) {
                this.initialize(stmList.head);
            }
        }
        // calculate kill set for each basic block.
        for(NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
            BasicBlock b = this.cfg.get(nodes.head);
            this.calculateKillSet(b);
        }
        // calculate gen set for each basic block.
        for(NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
            BasicBlock b = this.cfg.get(nodes.head);
            this.calculateGenSet(b);
        }
    }

    public void serialize(PrintStream printStream) {
        for(NodeList nodeList = this.cfg.nodes(); nodeList != null; nodeList = nodeList.tail) {
            printStream.println("## Block ##");
            BasicBlock basicBlock = this.cfg.get(nodeList.head);
            Set<Exp> genBlock = this.getGen(basicBlock);
            printStream.print("gen: { ");
            for(Exp genB : genBlock) {
                genB.accept(new QuadruplePrettyPrinter(printStream));
                printStream.print(", ");
            }
            printStream.print("}");
            Set<Exp> killBlock = this.getKill(basicBlock);
            printStream.print(" kill: { ");
            for(Exp killB : killBlock) {
                killB.accept(new QuadruplePrettyPrinter(printStream));
                printStream.print(", ");
            }
            printStream.print("}");
            printStream.println();
            printStream.println("### Statements ###");
            for(StmList stmList = basicBlock.first; stmList != null; stmList = stmList.tail) {
                Integer defId = this.getDefinitionId(stmList.head);
                printStream.print(defId + ":");
                stmList.head.accept(new QuadruplePrettyPrinter(printStream));
                Set<Exp> genStatement = this.getGen(stmList.head);
                printStream.print(" gen: { ");
                for(Exp genS : genStatement) {
                    genS.accept(new QuadruplePrettyPrinter(printStream));
                    printStream.print(", ");
                }
                printStream.print("}");
                Set<Exp> killStatement = this.getKill(stmList.head);
                printStream.print(" kill: { ");
                for(Exp killS : killStatement) {
                    killS.accept(new QuadruplePrettyPrinter(printStream));
                    printStream.print(", ");
                }
                printStream.print("}");
                printStream.println();
            }
            printStream.println("--------");
        }
    }
}
