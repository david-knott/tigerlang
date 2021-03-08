package com.chaosopher.tigerlang.compiler.dataflow.exp;

import java.io.PrintStream;
import java.util.HashMap;
import com.chaosopher.tigerlang.compiler.dataflow.*;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.graph.NodeList;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.BINOP;
import com.chaosopher.tigerlang.compiler.tree.CALL;
import com.chaosopher.tigerlang.compiler.tree.EXP;
import com.chaosopher.tigerlang.compiler.tree.LABEL;
import com.chaosopher.tigerlang.compiler.tree.MEM;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.QuadruplePrettyPrinter;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.tree.TEMP;

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

    private final HashMap<Stm, Integer> defs = new HashMap<>();
    private final HashMap<Temp, DataFlowSet<DataFlowExpression>> defTemps = new HashMap<>();
    private final HashMap<Temp, DataFlowSet<DataFlowExpression>> useTemps = new HashMap<>();
    private final DataFlowSet<DataFlowExpression> mems = new DataFlowSet<>();
    private final HashMap<BasicBlock, DataFlowSet<DataFlowExpression>> genMap = new HashMap<>();
    private final HashMap<BasicBlock, DataFlowSet<DataFlowExpression>> killMap = new HashMap<>();
    private final CFG cfg;

    private GenKillSets(CFG cfg) {
        this.cfg = cfg;
    }
 
    public DataFlowSet<DataFlowExpression> getKill(BasicBlock basicBlock) {
        return this.killMap.get(basicBlock);
    }

	public DataFlowSet<DataFlowExpression> getKill( Stm stm) {
        DataFlowSet<DataFlowExpression> killBlock = new DataFlowSet<>();
        return this.getKill(killBlock, stm);
    }

    private HashMap<Stm, DataFlowExpression> dfa = new HashMap<>();

	private void initExpressions(Stm s) {
        if(this.isMove(s) && isBinopSrc(s) && isTempDst(s)) {
            MOVE move = (MOVE)s;
          //  Temp temp = ((TEMP)move.dst).temp;
            BINOP binop = (BINOP)move.src;
            TEMP b = (TEMP)binop.left;
            TEMP c = (TEMP)binop.right;
            DataFlowExpression expId = this.createDataFlowExpression(binop.binop, b, c);
            this.addUseTemp(b.temp, expId);
            this.addUseTemp(c.temp, expId);
            this.dfa.put(s, expId);
        }
        if(this.isMove(s) && isMemSrc(s) && isTempDst(s)) {
            MOVE move = (MOVE)s;
            Temp temp = ((TEMP)move.dst).temp;
            MEM mem = (MEM)move.src;
            TEMP b = (TEMP)mem.exp;
            DataFlowExpression expId = this.createDataFlowExpression(mem, b);
            this.addUseTemp(temp, expId);
            this.dfa.put(s, expId);
        }
    }

	public DataFlowSet<DataFlowExpression> getKill(DataFlowSet<DataFlowExpression> killBlock, Stm s) {
        if(this.isMove(s) /* && isBinopSrc(s) */ && isTempDst(s)) {
            MOVE move = (MOVE)s;// get destination temp.
            Temp temp = ((TEMP)move.dst).temp;
            // kill all expressions with t
            DataFlowSet<DataFlowExpression> redefinitions = this.findExpressions(temp);
            killBlock.or(redefinitions);
        }
        if(this.isMove(s) && isMemSrc(s) && isTempDst(s)) {
            // kill all expressions with t
            MOVE move = (MOVE)s;// get destination temp.
            Temp temp = ((TEMP)move.dst).temp;
            // kill all expressions with t
            DataFlowSet<DataFlowExpression> redefinitions = this.findExpressions(temp);
            killBlock.or(redefinitions);
        }
        if(this.isMove(s) && isMemDst(s) && isTempSrc(s)) {
            DataFlowSet<DataFlowExpression> redefinitions = this.findMemExpressions();
            killBlock.or(redefinitions);
        }
        if(this.isCall(s)) {
            // kill all mem expressions
            DataFlowSet<DataFlowExpression> redefinitions = this.findMemExpressions();
            killBlock.or(redefinitions);
        }
        if(this.isMove(s) && isCallSrc(s)) {
            MOVE move = (MOVE)s;// get destination temp.
            Temp temp = ((TEMP)move.dst).temp;
            // kill all expressions with t
            DataFlowSet<DataFlowExpression> redefinitions = this.findExpressions(temp);
            killBlock.or(redefinitions);
            // and kill all mem expressions
            DataFlowSet<DataFlowExpression> memExp = this.findMemExpressions();
            killBlock.or(memExp);
        }
        return killBlock; 
	}

    public DataFlowSet<DataFlowExpression> getGen(BasicBlock basicBlock) {
        return this.genMap.get(basicBlock);
    }

	public DataFlowSet<DataFlowExpression> getGen(Stm s) {
        DataFlowSet<DataFlowExpression> gen = new DataFlowSet<>();
        return this.getGen(gen, s);
    }

    private void addUseTemp(Temp b, DataFlowExpression expId) {
        if(!this.useTemps.containsKey(b)) {
            this.useTemps.put(b, new DataFlowSet<>());
        }
        this.useTemps.get(b).add(expId);;
    }

	public DataFlowSet<DataFlowExpression> getGen(DataFlowSet<DataFlowExpression> gen, Stm s) {
        // t <- b * c
        if(this.isMove(s) && isBinopSrc(s) && isTempDst(s)) {
            MOVE move = (MOVE)s;
            Temp temp = ((TEMP)move.dst).temp;
            // create an index for the expression b * c
            DataFlowExpression expId = this.dfa.get(s);
            // add expId to gen set for this block.
            gen.add(expId);
            // remove any other expressions that use temp as an operand
            gen.andNot(this.findExpressions(temp));
        }
        // t <- M[b]
        if(this.isMove(s) && isMemSrc(s) && isTempDst(s)) {
            MOVE move = (MOVE)s;
            Temp temp = ((TEMP)move.dst).temp;
            // create an index for the expression M[b]
            DataFlowExpression expId = this.dfa.get(s);
            // add expId to gen set for this block.
            gen.add(expId);
            // remove any other expressions that use temp as an operand
            gen.andNot(this.findExpressions(temp));
            // gen.andNot(defTemps.get(temp));
            mems.add(expId);
        }
        return gen;
	}

    public Integer getDefinitionId(Stm stm) {
        return this.defs.get(stm);
    }
    
    public DataFlowSet<DataFlowExpression> getDefinitions(Temp temp) {
        return this.defTemps.get(temp);
    }

    private void initSets(BasicBlock basicBlock) {
        StmList stmList = basicBlock.first;
        for (; stmList != null; stmList = stmList.tail) {
            Stm s = stmList.head;
            // we are only interested in MOVE with a destination thats a temp.
            if(s instanceof MOVE) {
                MOVE move = (MOVE)s;
                // get destination temp.
                Temp temp = ((TEMP)move.dst).temp;
                // add reference for temp and def
                defTemps.put(temp, new DataFlowSet<>());
            }
            this.initExpressions(s);
        }
    }

    private boolean isMove(Stm stm) {
        return stm instanceof MOVE;
    }

    private boolean isTempSrc(Stm stm) {
        return isMove(stm) && ((MOVE)stm).src instanceof TEMP;
    }

    private boolean isTempDst(Stm stm) {
        return isMove(stm) && ((MOVE)stm).dst instanceof TEMP;
    }

    private boolean isMemSrc(Stm stm) {
        return isMove(stm) 
            && ((MOVE)stm).src instanceof MEM
            && ((MEM)((MOVE)stm).src).exp instanceof TEMP;
    }

    private boolean isMemDst(Stm stm) {
        return isMove(stm) 
            && ((MOVE)stm).dst instanceof MEM
            && ((MEM)((MOVE)stm).dst).exp instanceof TEMP;
    }

    private boolean isBinopSrc(Stm stm) {
        return isMove(stm) 
            && ((MOVE)stm).src instanceof BINOP
            && ((BINOP)((MOVE)stm).src).left instanceof TEMP
            && ((BINOP)((MOVE)stm).src).right instanceof TEMP;
    }

    public boolean isCall(Stm stm) {
        return stm instanceof EXP
            && ((EXP)stm).exp instanceof CALL;
    }

    public boolean isCallSrc(Stm stm) {
        return isMove(stm) 
            && ((MOVE)stm).src instanceof CALL;
    }

    private void calculateGenSet(BasicBlock basicBlock) {
        StmList stmList = basicBlock.first;
        DataFlowSet<DataFlowExpression> genBlock = new DataFlowSet<>();
        for (; stmList != null; stmList = stmList.tail) {
            Stm s = stmList.head;
            this.getGen(genBlock, s);
        }
        genMap.put(basicBlock, genBlock);
    }

    private DataFlowExpression createDataFlowExpression(MEM mem, TEMP b) {
        return new MemDataFlowExpression(b.temp);
    }

    private DataFlowExpression createDataFlowExpression(int binop, TEMP b, TEMP c) {
        return new BinopDataFlowExpression(binop, b.temp, c.temp);
    }

    /**
     * Generate a kill set.
     * 
     * @param basicBlock
     */
    private void calculateKillSet(BasicBlock basicBlock) {
        StmList stmList = basicBlock.first;
        DataFlowSet<DataFlowExpression> killBlock = new DataFlowSet<>();
        for (; stmList != null; stmList = stmList.tail) {
            Stm s = stmList.head;
            this.getKill(killBlock, s);
        }
        killMap.put(basicBlock, killBlock);
    }

    private DataFlowSet<DataFlowExpression> findMemExpressions() {
        return this.mems;
    }

    private DataFlowSet<DataFlowExpression> findExpressions(Temp temp) {
        return this.useTemps.containsKey(temp) ? this.useTemps.get(temp) : new DataFlowSet<>();
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
        // initialise empty sets.
        for(NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
            BasicBlock b = this.cfg.get(nodes.head);
            this.initSets(b);
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
            printStream.print(basicBlock.hashCode() + "");
            DataFlowSet<DataFlowExpression> gen = this.getGen(basicBlock);
            printStream.print(" gen:");
            printStream.print(gen);
            DataFlowSet<DataFlowExpression> kill = this.getKill(basicBlock);
            printStream.print(" kill:");
            printStream.print(kill);
            printStream.println();
            printStream.println("### Statements ###");
            for(StmList stmList = basicBlock.first; stmList != null; stmList = stmList.tail) {
                Integer defId = this.getDefinitionId(stmList.head);
                printStream.print(defId + ":");
                stmList.head.accept(new QuadruplePrettyPrinter(printStream));
                DataFlowSet<DataFlowExpression> genStatement = this.getGen(stmList.head);
                printStream.print(" gen:");
                printStream.print(genStatement);
                DataFlowSet<DataFlowExpression> killStatement = this.getKill(stmList.head);
                printStream.print(" kill:");
                printStream.print(killStatement);
                printStream.println();
            }
            printStream.println("--------");
        }
    }
}
