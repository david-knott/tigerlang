package com.chaosopher.tigerlang.compiler.types;

import com.chaosopher.tigerlang.compiler.util.Assert;

public class INT extends Type {
	private static String NAME = "int";

	public INT() {
	}

	public boolean coerceTo(Type t) {
		Assert.assertNotNull(t);
		return (t.actual() instanceof INT);
	}

	public String toString() {
		return NAME;
	}

	public void accept(GenVisitor genVisitor) {
		genVisitor.visit(this);
	}
}
