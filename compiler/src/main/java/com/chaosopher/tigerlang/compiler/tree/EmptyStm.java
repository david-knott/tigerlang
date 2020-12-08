package com.chaosopher.tigerlang.compiler.tree;

/**
 * EmptyStm class, used for testing.
 */
public class EmptyStm extends Stm {

	private String title;

	public EmptyStm(String title) {
		this.title = title;
	}

	public EmptyStm() {
		this.title = "Default";
	}

	@Override
	public ExpList kids() {
		return null;
	}

	@Override
	public Stm build(ExpList kids) {
		return this;
	}

	@Override
	public void accept(TreeVisitor treeVisitor) {

	}

	public String toString() {
		return this.title;
	}

	@Override
	public int getOperator() {
		return 0;
	}

	@Override
	public int getArity() {
		return 0;
	}

	@Override
	public IR getNthChild(int index) {
		return null;
	}

}