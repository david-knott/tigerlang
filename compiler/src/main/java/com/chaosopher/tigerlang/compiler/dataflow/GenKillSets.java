package com.chaosopher.tigerlang.compiler.dataflow;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.graph.Node;
import com.chaosopher.tigerlang.compiler.graph.NodeList;
import com.chaosopher.tigerlang.compiler.tree.LABEL;
import com.chaosopher.tigerlang.compiler.tree.QuadruplePrettyPrinter;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.util.Assert;


class DefinitionIds {

    public static DefinitionIds analyze(final CFG cfg) {
        DefinitionIds definitionIds = new DefinitionIds(cfg);
        definitionIds.generate();;
        return definitionIds;
    }

    private final CFG cfg;
    private final HashMap<Integer, Stm> definitionIdStatements = new HashMap<>();
    private final HashMap<Stm, Integer> statementDefinitionIds = new HashMap<>();

    public DefinitionIds(final CFG cfg) {
        this.cfg = cfg;
    }

    private void generate() {
        int id = 1;
        for(Node node : this.cfg.nodes()) {
            BasicBlock b = this.cfg.get(node);
            for(Stm s : b.first) {
                if(!(s instanceof LABEL)) {
                    this.definitionIdStatements.put(id, s);
                    this.statementDefinitionIds.put(s, id++);
                }
            }
        }
    }


}
public abstract class GenKillSets<T> {

    private final CFG cfg;
    private final HashMap<Integer, Stm> definitionIdStatements = new HashMap<>();
    private final HashMap<Stm, Integer> statementDefinitionIds = new HashMap<>();
    private final HashMap<BasicBlock, Set<T>> genMap = new HashMap<>();
    private final HashMap<BasicBlock, Set<T>> killMap = new HashMap<>();

    public GenKillSets(final CFG cfg) {
        this.cfg = cfg;
    }
    
    public Integer getDefinitionId(Stm stm) {
        //Assert.assertIsTrue(this.statementDefinitionIds.containsKey(stm), String.format("No definitionId found for %s", stm));
        return this.statementDefinitionIds.get(stm);
    }

    public boolean compareKill(Integer defId, Set<T> other) {
        return this.getKill(this.definitionIdStatements.get(defId)).equals(other);
    }

    public boolean compareGen(Integer defId, Set<T> other) {
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
        Set<T> genBlock = new HashSet<>();
        for (Stm s : basicBlock.first){
            this.initGenSet(genBlock, s);
        }
        genMap.put(basicBlock, genBlock);
    }

    protected abstract void initGenSet(Set<T> genBlock, final Stm s);

    private void calculateKillSet(BasicBlock basicBlock) {
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
                if(!(s instanceof LABEL)) {
                    this.definitionIdStatements.put(id, s);
                    this.statementDefinitionIds.put(s, id++);
                }
            }
        }
        //
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
