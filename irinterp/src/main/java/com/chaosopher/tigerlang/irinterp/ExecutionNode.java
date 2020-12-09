package com.chaosopher.tigerlang.irinterp;

import com.chaosopher.tigerlang.compiler.tree.IR;

class ExecutionNode {
	private final IR ir;
	private ExecutionNode next;

	public ExecutionNode(IR ir) {
		this.ir = ir;
		this.next = null;
	}

	public ExecutionNode(IR ir, ExecutionNode next) {
		this.ir = ir;
		this.next = next;
	}

	public ExecutionNode getNext() {
		return this.next;
	}

	public void accept(ExecutionNodeVisitor visitor) {
		ExecutionNode executionNode = this;
		while(executionNode != null) {
			executionNode.ir.accept(visitor);
			executionNode = executionNode.next;
		}
	}
}