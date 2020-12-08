package com.chaosopher.tigerlang.compiler.types;

public class VOID extends Type {

	public boolean coerceTo(Type t) {
		return (t.actual() instanceof VOID);
	}

	public void accept(GenVisitor genVisitor) {
		genVisitor.visit(this);
	}
}