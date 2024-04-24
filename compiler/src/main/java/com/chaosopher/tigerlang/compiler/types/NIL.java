package com.chaosopher.tigerlang.compiler.types;

public class NIL extends Type {

	private static String NAME = "nil";

	public NIL() {
	}

	public boolean coerceTo(Type t) {
		Type a = t.actual();
		return (a instanceof RECORD) || (a instanceof NIL);
	}

	public String toString() {
		return NAME;
	}

	public void accept(GenVisitor genVisitor) {
		genVisitor.visit(this);
	}
}
