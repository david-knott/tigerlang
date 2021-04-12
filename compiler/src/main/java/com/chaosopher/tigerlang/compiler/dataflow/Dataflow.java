package com.chaosopher.tigerlang.compiler.dataflow;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.graph.Node;
import com.chaosopher.tigerlang.compiler.graph.NodeList;
import com.chaosopher.tigerlang.compiler.tree.QuadruplePrettyPrinter;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.util.Assert;

/**
 * Abstract base class that provides dataflow support for the classical data flow operations.
 * 
 */
public abstract class Dataflow<T> {

    protected final CFG cfg;
    private final DataflowMeet dataflowMeet;
    protected final GenKillSets<T> genKillSets;
    private HashMap<BasicBlock, Set<T>> minMap = new HashMap<>();
    private HashMap<BasicBlock, Set<T>> moutMap = new HashMap<>();

    protected Dataflow(final CFG cfg, GenKillSets<T> genKillSets, final DataflowMeet dataflowMeet) {
        this.cfg = cfg;
        this.dataflowMeet = dataflowMeet;
        this.genKillSets = genKillSets;
    }
    
    protected abstract void processNode(Node node, Set<T> in, Set<T> out, Map<BasicBlock, Set<T>> inMap, Map<BasicBlock, Set<T>> outMap);

    protected abstract void meetUnion(Node node, Set<T> in, Set<T> out, Map<BasicBlock, Set<T>> inMap, Map<BasicBlock, Set<T>> outMap);

    protected abstract void meetIntersection(Node node, Set<T> in, Set<T> out, Map<BasicBlock, Set<T>> inMap, Map<BasicBlock, Set<T>> outMap);

    protected abstract NodeList getDirectedNodeList();

    protected void generate() {
        boolean changed = true;
        this.initialise(this.cfg, this.minMap, this.moutMap);
        do {
            changed = false;
            for(Node node : this.getDirectedNodeList()) {
                BasicBlock b = this.cfg.get(node);
                Set<T> inPrev = this.minMap.get(b);
                Set<T> outPrev = this.moutMap.get(b);
                Set<T> in = new HashSet<>();
                Set<T> out = new HashSet<>();
                this.meet(node, in, out, this.minMap, this.moutMap);
                this.processNode(node, in, out, this.minMap, this.moutMap);
                this.loopComplete(node, in, out, this.minMap, this.moutMap);
                var c1 = in.equals(inPrev);
                var c2 = out.equals(outPrev);
                changed = changed || !c1 || !c2;
            }
            if (!changed)
                break;
        } while (true);
    }

    protected void loopComplete(Node node, Set<T> in, Set<T> out, HashMap<BasicBlock, Set<T>> minMap2,
            HashMap<BasicBlock, Set<T>> moutMap2) {
                // overiden in sub classes.
    }

    protected void meet(Node node, Set<T> in, Set<T> out, Map<BasicBlock, Set<T>> inMap, Map<BasicBlock, Set<T>> outMap) {
        switch (this.dataflowMeet) {
            case INTERSECTION:
                meetIntersection(node, in, out, inMap, outMap);
                break;
            case UNION:
                meetUnion(node, in, out, inMap, outMap);
                break;
            default:
                throw new Error(String.format("Unsupported meet operator " + this.dataflowMeet));
        }
    }

    protected abstract void initialise(CFG cfg, Map<BasicBlock, Set<T>> inMap, Map<BasicBlock, Set<T>> outMap);

    public void toStream(PrintStream printStream) {
        printStream.println("# DataFlow Info#");
        for(NodeList nodeList = this.cfg.nodes(); nodeList != null; nodeList = nodeList.tail) {
            printStream.println(String.format("## Block - preds: %d ##", nodeList.head.pred() != null ? nodeList.head.pred().size() : 0));

            BasicBlock basicBlock = this.cfg.get(nodeList.head);
            printStream.print(basicBlock.hashCode() + "");
            printStream.println();
            printStream.print("In:");
            printStream.println(this.getIn(basicBlock));
            printStream.print("Out:");
            printStream.println(this.getOut(basicBlock));
            printStream.println("### Statements ###");
            for(StmList stmList = basicBlock.first; stmList != null; stmList = stmList.tail) {
                Integer defId = this.getDefinitionId(stmList.head);
                printStream.print(defId + ":");
                stmList.head.accept(new QuadruplePrettyPrinter(printStream));
                Set<T> inSet = this.getIn(basicBlock, stmList.head);
                printStream.print(" In:");
                printStream.print(inSet);
                Set<T> outSet = this.getOut(basicBlock, stmList.head);
                printStream.print(" out:");
                printStream.print(outSet);
                printStream.println();
            }
        }
        printStream.println("--------");
    }

    public T getDataFlowItem(Stm stm) {

        return null;
    }

    public Set<T> getOut(Stm stm) {
        BasicBlock basicBlock = this.genKillSets.getBasicBlock(stm);
        return this.getOut(basicBlock, stm);
    }

    public Set<T> getOut(BasicBlock basicBlock, Stm stm) {
        // could probably get in set for stm and apply gen and kill
        Set<T> blockOut = new HashSet<>();
        blockOut.addAll(this.getIn(basicBlock, stm));
        Set<T> gen = this.genKillSets.getGen(stm);
        // current block generates these
        Set<T> kill = this.genKillSets.getKill(stm);
        blockOut.removeAll(kill);
        blockOut.addAll(gen);
        return blockOut;
    }

    public Set<T> getIn(Stm stm) {
        BasicBlock basicBlock = this.genKillSets.getBasicBlock(stm);
        return this.getIn(basicBlock, stm);
    }

    public Set<T> getIn(BasicBlock basicBlock, Stm stm) {
        StmList stmList = basicBlock.first;
        // set of all definitions that reach the start of this block
        Set<T> blockIn =  new HashSet<>();
        blockIn.addAll(this.minMap.get(basicBlock));
        for (; stmList != null; stmList = stmList.tail) {
            // for each statement, we get the gen and kill
            Stm s = stmList.head;
            if(s == stm) {
                return blockIn;
            }
            // reconstitute kill and gen from in.
            Set<T> gen = this.genKillSets.getGen(s);
            Set<T> kill = this.genKillSets.getKill(s);
            // remove items that were killed.
            blockIn.removeAll(kill);
            // add items that were generated.
            blockIn.addAll(gen);
        }
        Assert.unreachable("Statment was not contained in any block");
        return null;
    }

    public Integer getDefinitionId(Stm head) {
        return this.genKillSets.getDefinitionId(head);
    }

    public Set<T> getOut(BasicBlock basicBlock) {
        return this.moutMap.get(basicBlock);
    }

    public Set<T> getIn(BasicBlock basicBlock) {
        return this.minMap.get(basicBlock);
    }

    public boolean compareIn(Integer definitionId, Set<T> in) {
        Stm stm = this.genKillSets.getStatement(definitionId);
        BasicBlock block = this.genKillSets.getBasicBlock(definitionId);
        return this.getIn(block, stm).equals(in);
    }

    public boolean compareOut(Integer definitionId, Set<T> out) {
        Stm stm = this.genKillSets.getStatement(definitionId);
        BasicBlock block = this.genKillSets.getBasicBlock(definitionId);
        return this.getOut(block, stm).equals(out);
    }
}