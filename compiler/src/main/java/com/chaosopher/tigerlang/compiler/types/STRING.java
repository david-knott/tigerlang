package com.chaosopher.tigerlang.compiler.types;

public class STRING extends Type {
	private static String NAME = "string";

	public STRING() {
	}

	public boolean coerceTo(Type t) {
		return (t.actual() instanceof STRING);
	}

	public String toString() {
		return NAME;
	}

	public void accept(GenVisitor genVisitor) {
		genVisitor.visit(this);
	}
}
