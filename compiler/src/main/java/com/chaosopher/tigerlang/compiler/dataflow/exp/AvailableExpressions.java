package com.chaosopher.tigerlang.compiler.dataflow.exp;

import java.io.PrintStream;
import java.util.HashMap;

import com.chaosopher.tigerlang.compiler.graph.NodeList;
import com.chaosopher.tigerlang.compiler.tree.QuadruplePrettyPrinter;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.util.Assert;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.dataflow.DataFlowSet;
import com.chaosopher.tigerlang.compiler.graph.Node;

class AvailableExpressions {

    public static AvailableExpressions analyze(CFG cfg, GenKillSets genKillSets) {
        AvailableExpressions availableExpressions = new AvailableExpressions(cfg, genKillSets);
        availableExpressions.generate();
        return availableExpressions;
    }

    private final CFG cfg;
    private final GenKillSets genKillSets;
    private final HashMap<BasicBlock, DataFlowSet<DataFlowExpression>> inMap = new HashMap<>();
    private final HashMap<BasicBlock, DataFlowSet<DataFlowExpression>> outMap = new HashMap<>();
    private final int maxIterations = 10;
    private int totalIterations;

    private AvailableExpressions(CFG cfg, GenKillSets genKillSets) {
        this.cfg = cfg;
        this.genKillSets = genKillSets;
    }

    public DataFlowSet<DataFlowExpression> getIn(BasicBlock basicBlock) {
        return this.inMap.get(basicBlock);
    }

    public DataFlowSet<DataFlowExpression> getOut(BasicBlock basicBlock) {
        return this.outMap.get(basicBlock);
    }

    public DataFlowSet<DataFlowExpression> getIn(Stm stm) {
        BasicBlock basicBlock = this.cfg.getBlockReference(stm);
        StmList stmList = basicBlock.first;
        // set of all definitions that reach the start of this block
        DataFlowSet<DataFlowExpression> blockIn =  new DataFlowSet<>();
        blockIn.or(this.inMap.get(basicBlock));
        for (; stmList != null; stmList = stmList.tail) {
            // for each statement, we get the gen and kill
            Stm s = stmList.head;
            if(s == stm) {
                return blockIn;
            }
            // reconstitute kill and gen from in.
            DataFlowSet<DataFlowExpression> gen = this.genKillSets.getGen(s);
            // current block generates these
            DataFlowSet<DataFlowExpression> kill = this.genKillSets.getKill(s);
            blockIn.andNot(kill);
            blockIn.or(gen);
        }
        Assert.unreachable("Statment was not contained in any block");
        return null;
	}

    public DataFlowSet<DataFlowExpression> getOut(Stm stm) {
        // could probably get in set for stm and apply gen and kill
        DataFlowSet<DataFlowExpression> blockOut = new DataFlowSet<>();
        blockOut.or(this.getIn(stm));
        DataFlowSet<DataFlowExpression> gen = this.genKillSets.getGen(stm);
        // current block generates these
        DataFlowSet<DataFlowExpression> kill = this.genKillSets.getKill(stm);
        // apply transformation to in data to get out.
        blockOut.andNot(kill);
        blockOut.or(gen);
        return blockOut;
	}

    private DataFlowSet<DataFlowExpression> getAllAvailableExpressions() {
        //combine all the gens into a new set
        DataFlowSet<DataFlowExpression> dataFlowSet = new DataFlowSet<>();
        for(NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
            BasicBlock b = this.cfg.get(nodes.head);
            dataFlowSet.or(this.genKillSets.getGen(b));
        }
        return dataFlowSet;
    }

    private void generate() {
        // start node of IN set is set to empty.
        NodeList nodeList = this.cfg.nodes();
        Node start = nodeList.head;
        BasicBlock startBlock = this.cfg.get(start);
        inMap.put(startBlock, new DataFlowSet<>());
        outMap.put(startBlock, this.getAllAvailableExpressions());
        // all other sets ( IN & OUT )  have all available expressions.
        for(; nodeList != null; nodeList = nodeList.tail) {
            BasicBlock b = this.cfg.get(nodeList.head);
            inMap.put(startBlock, this.getAllAvailableExpressions());
            outMap.put(b, this.getAllAvailableExpressions());
        }
        // compute in[n] and out[n] for each block.
        boolean changed = true;
        do
        {
            changed = false;
            for(NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
                BasicBlock b = this.cfg.get(nodes.head);
                DataFlowSet<DataFlowExpression> outPrev = ( DataFlowSet<DataFlowExpression>)outMap.get(b); 
                DataFlowSet<DataFlowExpression> inPrev = ( DataFlowSet<DataFlowExpression>)inMap.get(b);

                DataFlowSet<DataFlowExpression> pout = new  DataFlowSet<>();
                for(NodeList preds = nodes.head.pred(); preds != null; preds = preds.tail) {
                    BasicBlock pb = this.cfg.get(preds.head);
                    // intersection join operator
                    pout.and(outMap.get(pb));
                }
                inMap.put(b, pout);

                DataFlowSet<DataFlowExpression> gen = ( DataFlowSet<DataFlowExpression>)this.genKillSets.getGen(b);
                DataFlowSet<DataFlowExpression> kill = ( DataFlowSet<DataFlowExpression>)this.genKillSets.getKill(b);
                DataFlowSet<DataFlowExpression> inDiff = ( DataFlowSet<DataFlowExpression>)inMap.get(b);
                // create new set for in.
                DataFlowSet<DataFlowExpression> in = new DataFlowSet<>();
                in.or(inDiff);
                in.andNot(kill);
                in.or(gen);
                outMap.put(b, in);
                var c1 = inMap.get(b).equals(inPrev);
                var c2 = outMap.get(b).equals(outPrev);
                changed = changed || !c1 || !c2;
            }
            this.totalIterations++;
            if(!changed /*|| this.maxIterations == this.totalIterations*/) break;
        } while(true);
    }

    public void serialize(PrintStream printStream) {
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
                //Integer defId = this.getDefinitionId(stmList.head);
                //printStream.print(defId + ":");
                stmList.head.accept(new QuadruplePrettyPrinter(printStream));
                DataFlowSet<DataFlowExpression> inSet = this.getIn(stmList.head);
                printStream.print(" In:");
                printStream.print(inSet);
                DataFlowSet<DataFlowExpression> outSet = this.getOut(stmList.head);
                printStream.print(" out:");
                printStream.print(outSet);
                printStream.println();
            }
            printStream.println("--------");
        }
    }
}