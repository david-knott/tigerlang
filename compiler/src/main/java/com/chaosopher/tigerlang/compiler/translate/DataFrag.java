package com.chaosopher.tigerlang.compiler.translate;

import com.chaosopher.tigerlang.compiler.frame.Frame;
import com.chaosopher.tigerlang.compiler.temp.Label;

/**
 * Stores string literal data for the HIR/LIR representation.
 **/
public class DataFrag extends Frag {

	private final String data;
	private final Frame frame;
	private final Label label;

	public DataFrag(Label label, String data, Frame frame) {
		this.frame = frame;
		this.label = label;
		this.data = data;
	}

	public String toString() {
		return this.frame.string(this.label, this.data);
	}

	public String getData() {
		return this.data;
	}

	@Override
	public Frame getFrame() {
		return this.frame;
	}

	public Label getLabel() {
		return this.label;
	}

	@Override
	public void accept(FragmentVisitor fragmentVisitor) {
        fragmentVisitor.visit(this);
	}
}