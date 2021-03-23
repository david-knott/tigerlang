package com.chaosopher.tigerlang.compiler.dataflow.exp;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.chaosopher.tigerlang.compiler.graph.NodeList;
import com.chaosopher.tigerlang.compiler.tree.Exp;
import com.chaosopher.tigerlang.compiler.tree.QuadruplePrettyPrinter;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.util.Assert;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.dataflow.utils.ExtractExp;
import com.chaosopher.tigerlang.compiler.graph.Node;

class AvailableExpressions {

    public static AvailableExpressions analyze(CFG cfg, GenKillSets genKillSets) {
        AvailableExpressions availableExpressions = new AvailableExpressions(cfg, genKillSets);
        availableExpressions.generate();
        return availableExpressions;
    }

    private final CFG cfg;
    private final GenKillSets genKillSets;
    private final HashMap<BasicBlock, Set<Exp>> inMap = new HashMap<>();
    private final HashMap<BasicBlock, Set<Exp>> outMap = new HashMap<>();
    private final int maxIterations = 10;
    private int totalIterations;

    private AvailableExpressions(CFG cfg, GenKillSets genKillSets) {
        this.cfg = cfg;
        this.genKillSets = genKillSets;
    }

    public Set<Exp> getIn(BasicBlock basicBlock) {
        return this.inMap.get(basicBlock);
    }

    public Set<Exp> getOut(BasicBlock basicBlock) {
        return this.outMap.get(basicBlock);
    }

    public Set<Exp> getIn(BasicBlock basicBlock, Stm stm) {
       // BasicBlock basicBlock = this.cfg.getBlockReference(stm);
        StmList stmList = basicBlock.first;
        // set of all definitions that reach the start of this block
        Set<Exp> blockIn = new HashSet<>();
        blockIn.addAll(this.inMap.get(basicBlock));
        for (; stmList != null; stmList = stmList.tail) {
            // for each statement, we get the gen and kill
            Stm s = stmList.head;
            if (s == stm) {
                return blockIn;
            }
            // reconstitute kill and gen from in.
            Set<Exp> gen = this.genKillSets.getGen(s);
            // current block generates these
            Set<Exp> kill = this.genKillSets.getKill(s);
            blockIn.removeAll(kill);
            blockIn.addAll(gen);
        }
        Assert.unreachable("Statment was not contained in any block");
        return null;
    }

    public Set<Exp> getOut(BasicBlock basicBlock, Stm stm) {
        // could probably get in set for stm and apply gen and kill
        Set<Exp> blockOut = new HashSet<>();
        blockOut.addAll(this.getIn(basicBlock, stm));
        Set<Exp> gen = this.genKillSets.getGen(stm);
        // current block generates these
        Set<Exp> kill = this.genKillSets.getKill(stm);
        // apply transformation to in data to get out.
        blockOut.removeAll(kill);
        blockOut.addAll(gen);
        return blockOut;
    }

    private Set<Exp> getAllAvailableExpressions() {
        // combine all the gens into a new set
        Set<Exp> dataFlowSet = new HashSet<>();
        for (NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
            BasicBlock b = this.cfg.get(nodes.head);
            for (StmList stmList = b.first; stmList != null; stmList = stmList.tail) {
                Exp exp = ExtractExp.getExp(stmList.head);
                if (exp != null) {
                    dataFlowSet.add(exp);
                }
            }
        }
        return dataFlowSet;
    }

    private void generate() {
        // start node of IN set is set to empty.
        NodeList nodeList = this.cfg.nodes();
        Node start = nodeList.head;
        BasicBlock startBlock = this.cfg.get(start);
        this.inMap.put(startBlock, new HashSet<>());
        //this.outMap.put(startBlock, this.genKillSets.getGen(startBlock));
        this.outMap.put(startBlock, this.getAllAvailableExpressions());
        // all other sets ( IN & OUT ) have all available expressions.
        nodeList = nodeList.tail;
        for (; nodeList != null; nodeList = nodeList.tail) {
            BasicBlock b = this.cfg.get(nodeList.head);
            this.inMap.put(b, this.getAllAvailableExpressions());
            Set<Exp> initOut = new HashSet<>();
            initOut.addAll(this.getAllAvailableExpressions());
            initOut.removeAll(this.genKillSets.getKill(b));
            initOut.addAll(this.genKillSets.getGen(b));
            this.outMap.put(b, initOut);
        }
        // compute in[n] and out[n] for each block.
        boolean changed = true;
        do {
            changed = false;
            for (NodeList nodes = this.cfg.nodes(); nodes != null; nodes = nodes.tail) {
                BasicBlock b = this.cfg.get(nodes.head);
                // will contain all expression on first iteration
                Set<Exp> outPrev = (Set<Exp>) outMap.get(b);
                Set<Exp> inPrev = (Set<Exp>) inMap.get(b);
                // new set for available in
                // contains intersection of all expressions
                // in predecessors
                Set<Exp> in = new HashSet<>();
                NodeList preds;
                if( (preds = nodes.head.pred()) != null) {
                    BasicBlock predBlock = this.cfg.get(preds.head);
                    Set<Exp> predOut = outMap.get(predBlock);
                    in.addAll(predOut);
                    preds = preds.tail;
                    for (; preds != null; preds = preds.tail) {
                        predBlock = this.cfg.get(preds.head);
                        predOut = outMap.get(predBlock);
                        in.retainAll(predOut);
                    }
                }
                inMap.put(b, in);

                // Out set 
                Set<Exp> out = new HashSet<>();
                out.addAll(in);
                out.removeAll(this.genKillSets.getKill(b));
                out.addAll(this.genKillSets.getGen(b));
                outMap.put(b, out);

                var c1 = in.equals(inPrev);
                var c2 = out.equals(outPrev);
                changed = changed || !c1 || !c2;
            }
            this.totalIterations++;
            if (!changed || this.maxIterations == this.totalIterations)
                break;
        } while (true);
    }

    public void serialize(PrintStream printStream) {
        for (NodeList nodeList = this.cfg.nodes(); nodeList != null; nodeList = nodeList.tail) {
            printStream.println("## Block ##");
            BasicBlock basicBlock = this.cfg.get(nodeList.head);
            printStream.print(basicBlock.hashCode() + "");
            Set<Exp> genBlock = this.getIn(basicBlock);
            printStream.print(" in: { ");
            for(Exp genB : genBlock) {
                genB.accept(new QuadruplePrettyPrinter(printStream));
                printStream.print(", ");
            }
            printStream.print("}");
            Set<Exp> killBlock = this.getOut(basicBlock);
            printStream.print(" out: { ");
            for(Exp killB : killBlock) {
                killB.accept(new QuadruplePrettyPrinter(printStream));
                printStream.print(", ");
            }
            printStream.print("}");
            printStream.println();
            printStream.println("### Statements ###");
            for (StmList stmList = basicBlock.first; stmList != null; stmList = stmList.tail) {
                // Integer defId = this.getDefinitionId(stmList.head);
                // printStream.print(defId + ":");
                stmList.head.accept(new QuadruplePrettyPrinter(printStream));
                Set<Exp> genStatement = this.getIn(basicBlock, stmList.head);
                printStream.print(" in: { ");
                for(Exp genS : genStatement) {
                    genS.accept(new QuadruplePrettyPrinter(printStream));
                    printStream.print(", ");
                }
                printStream.print("}");
                Set<Exp> killStatement = this.getOut(basicBlock, stmList.head);
                printStream.print(" out: { ");
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