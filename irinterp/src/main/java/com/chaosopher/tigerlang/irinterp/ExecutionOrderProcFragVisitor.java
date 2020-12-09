package com.chaosopher.tigerlang.irinterp;

import com.chaosopher.tigerlang.compiler.tree.BINOP;
import com.chaosopher.tigerlang.compiler.tree.CALL;
import com.chaosopher.tigerlang.compiler.tree.CJUMP;
import com.chaosopher.tigerlang.compiler.tree.CONST;
import com.chaosopher.tigerlang.compiler.tree.ESEQ;
import com.chaosopher.tigerlang.compiler.tree.EXP;
import com.chaosopher.tigerlang.compiler.tree.ExpList;
import com.chaosopher.tigerlang.compiler.tree.JUMP;
import com.chaosopher.tigerlang.compiler.tree.LABEL;
import com.chaosopher.tigerlang.compiler.tree.MEM;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.NAME;
import com.chaosopher.tigerlang.compiler.tree.SEQ;
import com.chaosopher.tigerlang.compiler.tree.TEMP;
import com.chaosopher.tigerlang.compiler.tree.TreeVisitor;

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