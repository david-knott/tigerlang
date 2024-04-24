package com.chaosopher.tigerlang.compiler.frame;

import com.chaosopher.tigerlang.compiler.assem.InstrList;

public class Proc {
	
	public InstrList prolog;
	public InstrList epilog;
	public InstrList body;
	
	public Proc(InstrList prolog, InstrList body, InstrList epilog) {
		this.prolog = prolog;
		this.body = body;
		this.epilog = epilog;
	}
}