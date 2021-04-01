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
    private final DataflowDir dataflowDir;
    protected final GenKillSets<T> genKillSets;
    private HashMap<BasicBlock, Set<T>> minMap = new HashMap<>();
    private HashMap<BasicBlock, Set<T>> moutMap = new HashMap<>();

    protected Dataflow(final CFG cfg, GenKillSets<T> genKillSets, final DataflowMeet dataflowMeet, final DataflowDir dataflowDir) {
        this.cfg = cfg;
        this.dataflowMeet = dataflowMeet;
        this.dataflowDir = dataflowDir;
        this.genKillSets = genKillSets;
    }
    
    private NodeList getDirectedNodeList() {
        return this.dataflowDir == DataflowDir.FORWARD ? 
            this.cfg.nodes() :
            this.cfg.nodes().reverse();
    }

    protected NodeList getDirectedAdjacentNodeList(Node node) {
        return this.dataflowDir == DataflowDir.REVERSE ?
            node.succ() :
            node.pred();
    }

    protected void processNode(Node node, Set<T> in, Set<T> out, Map<BasicBlock, Set<T>> inMap, Map<BasicBlock, Set<T>> outMap) {
        BasicBlock b = this.cfg.get(node);
        out.addAll(in);
        out.removeAll(this.genKillSets.getKill(b));
        out.addAll(this.genKillSets.getGen(b));
        outMap.put(b, out);
    }

    protected void meetUnion(Node node, Set<T> in, Set<T> out, Map<BasicBlock, Set<T>> inMap, Map<BasicBlock, Set<T>> outMap) {
        BasicBlock b = this.cfg.get(node);
        if(this.getDirectedAdjacentNodeList(node) != null) {
            for (Node adj : this.getDirectedAdjacentNodeList(node)) {
                BasicBlock ab = this.cfg.get(adj);
                in.addAll(outMap.get(ab));
            }
        }
        inMap.put(b, in);
    }

    protected void meetIntersection(Node node, Set<T> in, Set<T> out, Map<BasicBlock, Set<T>> inMap, Map<BasicBlock, Set<T>> outMap) {
        BasicBlock b = this.cfg.get(node);
        NodeList preds;
        if( (preds = this.getDirectedAdjacentNodeList(node)) != null) {
            Set<T> predOut = null;
            for (Node pred : preds) {
                BasicBlock predBlock = this.cfg.get(pred);
                if(predOut == null) {
                    predOut = outMap.get(predBlock);
                    in.addAll(predOut);
                }
                predBlock = this.cfg.get(pred);
                predOut = outMap.get(predBlock);
                in.retainAll(predOut);
            }
        }
        inMap.put(b, in);
    }

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
                this.doGenerate(node, in, out, this.minMap, this.moutMap);
                var c1 = in.equals(inPrev);
                var c2 = out.equals(outPrev);
                changed = changed || !c1 || !c2;
            }
            if (!changed)
                break;
        } while (true);
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

    protected abstract void doGenerate(Node node, Set<T> in, Set<T> out, Map<BasicBlock, Set<T>> inMap, Map<BasicBlock, Set<T>> outMap);

    protected abstract void initialise(CFG cfg, Map<BasicBlock, Set<T>> inMap, Map<BasicBlock, Set<T>> outMap);

    public void toStream(PrintStream printStream) {
        printStream.println("# Reaching Definitions #");
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

    private Set<T> getOut(BasicBlock basicBlock, Stm stm) {
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

    private Set<T> getIn(BasicBlock basicBlock, Stm stm) {
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

    private Integer getDefinitionId(Stm head) {
        return this.genKillSets.getDefinitionId(head);
    }

    private Set<T> getOut(BasicBlock basicBlock) {
        return this.moutMap.get(basicBlock);
    }

    private Set<T> getIn(BasicBlock basicBlock) {
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