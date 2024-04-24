package com.chaosopher.tigerlang.compiler.dataflow;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.graph.Node;
import com.chaosopher.tigerlang.compiler.graph.NodeList;
import com.chaosopher.tigerlang.compiler.tree.QuadruplePrettyPrinter;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;

/**
 * Abstract class that provides generic gen kill set functionality.
 */
public abstract class GenKillSets<T> {

    private final CFG cfg;
    private final HashMap<Integer, Stm> definitionIdStatements = new HashMap<>();
    private final HashMap<Integer, Integer> statementDefinitionIds = new HashMap<>();
    private final HashMap<Integer, BasicBlock> definitionIdBlocks = new HashMap<>();
    private final HashMap<BasicBlock, Set<T>> genMap = new HashMap<>();
    private final HashMap<BasicBlock, Set<T>> killMap = new HashMap<>();
    private final HashMap<Integer, BasicBlock> stmBasicBlockMap = new HashMap<>();

    public GenKillSets(final CFG cfg) {
        this.cfg = cfg;
    }

    public BasicBlock getBasicBlock(Stm stm) {
        return this.stmBasicBlockMap.get(System.identityHashCode(stm));
    }
    
    public Integer getDefinitionId(final Stm stm) {
        int key = System.identityHashCode(stm);
        return this.statementDefinitionIds.get(key);
    }

    public Stm getStatement(final Integer definitionId) {
        return this.definitionIdStatements.get(definitionId);
    }

    public BasicBlock getBasicBlock(final Integer definitionId) {
        return this.definitionIdBlocks.get(definitionId);
    }

    public boolean compareKill(final Integer defId, Set<T> other) {
        return this.getKill(this.definitionIdStatements.get(defId)).equals(other);
    }

    public boolean compareGen(final Integer defId, Set<T> other) {
        return this.getGen(this.definitionIdStatements.get(defId)).equals(other);
    }

    public Set<T> getKill(final BasicBlock basicBlock) {
        return this.killMap.get(basicBlock);
    }

	public Set<T> getKill(final Stm stm) {
        Set<T> killBlock = new HashSet<>();
        this.initKillSet(killBlock, stm);
        return killBlock;
    }

    public Set<T> getGen(final BasicBlock basicBlock) {
        return this.genMap.get(basicBlock);
    }

	public Set<T> getGen(final Stm s) {
        Set<T> gen = new HashSet<>();
        this.initGenSet(gen, s);
        return gen;
    }

    private void calculateGenSet(final BasicBlock basicBlock) {
        // gen (np) = gen (n) + ( gen(p) - kill(n))
        Set<T> genBlock = new HashSet<>();
        for (Stm s : basicBlock.first){
            genBlock.removeAll(this.getKill(s));
            this.initGenSet(genBlock, s);

        }
        genMap.put(basicBlock, genBlock);
    }

    protected abstract void initGenSet(Set<T> genBlock, final Stm s);

    private void calculateKillSet(final BasicBlock basicBlock) {
        Set<T> killBlock = new HashSet<>();
        for (Stm s : basicBlock.first){
            this.initKillSet(killBlock, s);
        }
        killMap.put(basicBlock, killBlock);
    }
    
    protected abstract void initKillSet(Set<T> killBlock, final Stm s);

    protected void generate() {
        // definition id map.
        int id = 1;
        for(Node node : this.cfg.nodes()) {
            BasicBlock b = this.cfg.get(node);
            for(Stm s : b.first) {
                this.definitionIdStatements.put(id, s);
                this.definitionIdBlocks.put(id, b);
                this.statementDefinitionIds.put(System.identityHashCode(s), id++);
                this.stmBasicBlockMap.put(System.identityHashCode(s), b);
            }
        }
        // initialize kill gen sets.
        for(Node node : this.cfg.nodes()) {
            BasicBlock b = this.cfg.get(node);
            for(Stm s : b.first) {
                this.initialize(b, s);
            }
        }
        // calculate kill set for each basic block.
        for(Node node : this.cfg.nodes()) {
            BasicBlock b = this.cfg.get(node);
            this.calculateKillSet(b);
        }
        // calculate gen set for each basic block.
        for(Node node : this.cfg.nodes()) {
            BasicBlock b = this.cfg.get(node);
            this.calculateGenSet(b);
        }
    }

    protected abstract void initialize(BasicBlock b, Stm s);

    public void serialize(PrintStream printStream) {
        for(NodeList nodeList = this.cfg.nodes(); nodeList != null; nodeList = nodeList.tail) {
            printStream.println("## Block ##");
            BasicBlock basicBlock = this.cfg.get(nodeList.head);
            printStream.print(basicBlock.hashCode() + "");
            Set<T> gen = this.getGen(basicBlock);
            printStream.print(" gen:");
            printStream.print(gen);
            Set<T> kill = this.getKill(basicBlock);
            printStream.print(" kill:");
            printStream.print(kill);
            printStream.println();
            printStream.println("### Statements ###");
            for(StmList stmList = basicBlock.first; stmList != null; stmList = stmList.tail) {
                Integer defId = this.getDefinitionId(stmList.head);
                printStream.print(defId + ":");
                stmList.head.accept(new QuadruplePrettyPrinter(printStream));
                Set<T> genStatement = this.getGen(stmList.head);
                printStream.print(" gen:");
                printStream.print(genStatement);
                Set<T> killStatement = this.getKill(stmList.head);
                printStream.print(" kill:");
                printStream.print(killStatement);
                printStream.println();
            }
            printStream.println("--------");
        }
    }
}
