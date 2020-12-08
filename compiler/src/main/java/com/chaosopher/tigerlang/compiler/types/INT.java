package com.chaosopher.tigerlang.compiler.types;

public class INT extends Type {
	private static String NAME = "int";

	public INT() {
	}

	public boolean coerceTo(Type t) {
		return (t.actual() instanceof INT);
	}

	public String toString() {
		return NAME;
	}

	public void accept(GenVisitor genVisitor) {
		genVisitor.visit(this);
	}
}
