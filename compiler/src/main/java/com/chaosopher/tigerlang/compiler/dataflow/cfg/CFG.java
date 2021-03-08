package com.chaosopher.tigerlang.compiler.dataflow.cfg;

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

    class BuildGraph {
        private  BasicBlock basicBlock;
        public BuildGraph(BasicBlock basicBlock) {
            this.basicBlock = basicBlock;
        }

        public void start() {
            this.start(this.basicBlock);
        }

        private void addEdge(BasicBlock basicBlock, LabelList labelList) {
            BasicBlock target = labelBlockMap.get(labelList.head);
            Assert.assertNotNull(target, "Label not found");
            CFG.this.addEdge(basicBlock, target);
            if(labelList.tail != null) {
                this.addEdge(basicBlock, labelList.tail);
            }
        }

        private void start(BasicBlock basicBlock) {
            if(basicBlock.tail != null) {
                this.start(basicBlock.tail);
            }
            addNode(basicBlock);
            if(basicBlock.labelList != null) {
                addEdge(basicBlock, basicBlock.labelList);
            }
        }
    }

    /**
     * Builds basic blocks for the supplied fragment.
     */
    class BuildBasicBlocks extends DefaultTreeVisitor {

        BasicBlock basicBlock;
        BasicBlock prevBasicBlock;

        @Override
        public void visit(ESEQ op) {
            Assert.unreachable("Should not be reachable for LIR");
        }

        @Override
        public void visit(LABEL op) {
            this.prevBasicBlock = this.basicBlock;
            this.basicBlock = new BasicBlock(op, op.label, this.basicBlock);
            // this will be clobbered if there are jumps / cjumps in this block.
            if(prevBasicBlock != null && this.prevBasicBlock.labelList == null) {
             //   this.basicBlock.labelList = new LabelList(this.prevBasicBlock.label, null);
                this.prevBasicBlock.labelList = new LabelList(this.basicBlock.label, null);
            }
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
            stmList.head.accept(this);
            Assert.assertNotNull(this.basicBlock, "Basic block should not be null, perhaps there is no label ?");
            this.basicBlock.addStatement(stmList.head);
            // add reference to stmt -> basicBlock
            blockReference.put(stmList.head, this.basicBlock);
            if(stmList.tail != null) {
                stmList.tail.accept(this);
            }
        }
    }

    public static CFG build(StmList stmList) {
        CFG cfg = new CFG(stmList);
        cfg.init();
        return cfg;
    }

    private final StmList stmList;
    private final BuildBasicBlocks buildBasicBlocks;
    private final HashMap<Label, BasicBlock> labelBlockMap = new HashMap<>();
    private final HashMap<BasicBlock, Node> map = new HashMap<>();
    private final HashMap<Node, BasicBlock> revMap = new HashMap<>();
    private final HashMap<Stm, BasicBlock> blockReference = new HashMap<>();

    private CFG(StmList stmList) {
        this.stmList = stmList;
        this.buildBasicBlocks = new BuildBasicBlocks();
    }

    public BasicBlock get(Node node) {
        BasicBlock bb = this.revMap.get(node);
        Assert.assertNotNull(bb, "Basic block should not be null for node " + node);
        return bb;
    }

    private void init() {
        this.stmList.accept(this.buildBasicBlocks);
        new BuildGraph(this.buildBasicBlocks.basicBlock).start();
    }

    private Node addNode(BasicBlock basicBlock) {
        Node node = this.map.get(basicBlock);
        if(node == null) {
            node = super.newNode();
            this.map.put(basicBlock, node);
            this.revMap.put(node, basicBlock);
        }
        return node;
    }

    private void addEdge(BasicBlock start, BasicBlock end) {
        Node nodeStart = this.addNode(start);
        Node nodeEnd = this.addNode(end);
        super.addEdge(nodeStart, nodeEnd);
    }

	public BasicBlock getBlockReference(Stm stm) {
        return this.blockReference.get(stm);
	}
}
