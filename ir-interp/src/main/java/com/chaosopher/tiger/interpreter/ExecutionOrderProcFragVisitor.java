package com.chaosopher.tiger.interpreter;

import Tree.BINOP;
import Tree.CALL;
import Tree.CJUMP;
import Tree.CONST;
import Tree.ESEQ;
import Tree.EXP;
import Tree.ExpList;
import Tree.JUMP;
import Tree.LABEL;
import Tree.MEM;
import Tree.MOVE;
import Tree.NAME;
import Tree.SEQ;
import Tree.TEMP;
import Tree.TreeVisitor;

class ExecutionOrderProcFragVisitor implements TreeVisitor {

    private ExecutionNode executionNode = null;

	@Override
	public void visit(BINOP arg0) {
        // visit this left node f
        arg0.left.accept(this);
        executionNode = new ExecutionNode(arg0.left, executionNode);
        // visit the right node
        arg0.right.accept(this);
        executionNode = new ExecutionNode(arg0.right, executionNode);
        // this node
        executionNode = new ExecutionNode(arg0, executionNode);
	}

	@Override
	public void visit(CALL arg0) {
        for(ExpList expList = arg0.args; expList != null; expList = expList.tail) {
            // visit each argument
            expList.head.accept(this);
            executionNode = new ExecutionNode(expList.head, executionNode);
        }
        // visit the call 
        executionNode = new ExecutionNode(arg0, executionNode);
	}

	@Override
	public void visit(CONST arg0) {
        // visit the int
        executionNode = new ExecutionNode(arg0, executionNode);
	}

	@Override
	public void visit(ESEQ arg0) {
        arg0.stm.accept(this);
        executionNode = new ExecutionNode(arg0.stm, executionNode);
        arg0.exp.accept(this);
        executionNode = new ExecutionNode(arg0.exp, executionNode);
	}

	@Override
	public void visit(EXP arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(JUMP arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(LABEL arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(MEM arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(MOVE arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(NAME arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(SEQ arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(TEMP arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(CJUMP arg0) {
		// TODO Auto-generated method stub

	}

	public ExecutionNode getExecutionNode() {
		return null;
	}
}