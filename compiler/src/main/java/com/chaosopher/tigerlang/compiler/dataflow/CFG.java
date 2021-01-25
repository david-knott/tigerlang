package com.chaosopher.tigerlang.compiler.dataflow;

import java.util.HashMap;

import com.chaosopher.tigerlang.compiler.graph.Graph;
import com.chaosopher.tigerlang.compiler.graph.Node;
import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.temp.LabelList;
import com.chaosopher.tigerlang.compiler.tree.CJUMP;
import com.chaosopher.tigerlang.compiler.tree.DefaultTreeVisitor;
import com.chaosopher.tigerlang.compiler.tree.ESEQ;
import com.chaosopher.tigerlang.compiler.tree.JUMP;
import com.chaosopher.tigerlang.compiler.tree.LABEL;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.util.Assert;


/**
 * A Control Flow Graph class. This takes the list of statements and
 * contructs a graph which represents the flow of control in the
 * program. This is useful for computing the kill and gen for the program.
 * 
 * https://en.wikipedia.org/wiki/Basic_block#Creation_algorithm
 * 1) Identify Leaders
 *      * First Instruction
 *      * Target(s) of a conditional or unconditional, ie a JUMP or CJUMP target
 *      * The instruction that immediately follows a conditional or unconditiona. ( not relevant to this class)
 * 2) Start from a leader, the set of all instructions until and not
 * including the next leader is a basic block corresponding to the starting
 * leader. Thus every basic block has a leader.
 */
public class CFG extends Graph {

    class BasicBlock {

        public final StmList first;
        public final Label label;
        public BasicBlock tail;
        public LabelList labelList;

        public BasicBlock(Stm first, Label label) {
            this.first = new StmList(first);
            this.label = label;
        }

        public BasicBlock(Stm first, Label label, BasicBlock tail) {
            this.first = new StmList(first);
            this.label = label;
            this.tail = tail;
        }

        public void addStatement(Stm stm) {
            this.first.append(stm);
        }

        @Override
        public String toString() {
            BasicBlock me = this;
            String result = "";
            while(me.tail != null) {
                 result+= me.label.toString() + "\n";
                 StmList sl = me.first;
                 while(sl.tail != null) {
                    result+= sl.head.toString() + "\n";
                    sl = sl.tail;
                 }
                 me = me.tail;
            }
            return result;
        }
    } 

    class BuildGraph {
        private  BasicBlock basicBlock;
        public BuildGraph(BasicBlock basicBlock) {
            this.basicBlock = basicBlock;
        }

        public void start() {
            this.start(this.basicBlock);
        }

        private void addEdge(BasicBlock basicBlock, LabelList labelList) {
            CFG.this.addEdge(basicBlock, labelBlockMap.get(labelList.head));
            if(labelList.tail != null) {
                this.addEdge(basicBlock, labelList.tail);
            }
        }

        private void start(BasicBlock basicBlock) {
            addNode(basicBlock);
            if(basicBlock.labelList != null) {
                addEdge(basicBlock, basicBlock.labelList);
            }
            if(basicBlock.tail != null) {
                this.start(basicBlock.tail);
            }
        }
    }

    /**
     * Builds basic blocks for the supplied fragment.
     */
    class BuildBasicBlocks extends DefaultTreeVisitor {

        BasicBlock basicBlock;

        @Override
        public void visit(ESEQ op) {
            Assert.unreachable("Should not be reachable for LIR");
        }

        @Override
        public void visit(LABEL op) {
            this.basicBlock = new BasicBlock(op, op.label, this.basicBlock);
            labelBlockMap.put(op.label, this.basicBlock);
        }

        @Override
        public void visit(CJUMP cjump) {
            this.basicBlock.labelList = new LabelList(cjump.iffalse, new LabelList(cjump.iftrue, null));
        }

        @Override
        public void visit(JUMP op) {
            this.basicBlock.labelList = op.targets;
        }

        @Override
        public void visit(StmList stmList) {
            super.visit(stmList);
            Assert.assertNotNull(this.basicBlock, "Basic block should not be null, perhaps there is no label ?");
            this.basicBlock.addStatement(stmList.head);
        }
    }
    
    private final StmList stmList;
    private final BuildBasicBlocks buildBasicBlocks;
    private final HashMap<Label, BasicBlock> labelBlockMap = new HashMap<>();
    private final HashMap<BasicBlock, Node> map = new HashMap<>();
    private final HashMap<Node, BasicBlock> revMap = new HashMap<>();

    public CFG(StmList stmList) {
        this.stmList = stmList;
        this.buildBasicBlocks = new BuildBasicBlocks();
        this.init();
    }

    public String get(Node node) {
        return this.revMap.get(node).toString();
    }

    private void init() {
        this.stmList.accept(this.buildBasicBlocks);
        new BuildGraph(this.buildBasicBlocks.basicBlock).start();
    }


    private void addNode(BasicBlock basicBlock) {
        Node node = super.newNode();
        this.map.put(basicBlock, node);
        this.revMap.put(node, basicBlock);
    }

    private void addEdge(BasicBlock start, BasicBlock end) {
        Node nodeStart = this.map.get(start);
        if(nodeStart == null) {
            nodeStart = super.newNode();
            this.map.put(start, nodeStart);
            this.revMap.put(nodeStart, start);
        }
        Node nodeEnd = this.map.get(end);
        if(nodeEnd == null) {
            nodeEnd = super.newNode();
            this.map.put(end, nodeEnd);
            this.revMap.put(nodeEnd, end);
        }
        super.addEdge(nodeStart, nodeEnd);
    }
}
