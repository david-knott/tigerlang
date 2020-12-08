package com.chaosopher.tiger.interpreter;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Hashtable;

import Translate.DataFrag;
import Translate.FragmentVisitor;
import Translate.ProcFrag;

/**
 * This class traverses the fragment list and builds a list of IR instructions
 * for execution.
 */
public class ExecutionOrderVisitor implements FragmentVisitor {

	/**
	 * HashMap to store all the strings with their associated labels.
	 */
	private final HashMap<String, String> stringMap = new HashMap<>();
	private final ExecutionOrderProcFragVisitor visitor = new ExecutionOrderProcFragVisitor();


	public ExecutionNode getExecutionNode() {
		return this.visitor.getExecutionNode();
	}

	public ExecutionOrderVisitor(PrintStream out, PrintStream err) {
	}

	@Override
	public void visit(ProcFrag procFrag) {
		procFrag.body.accept(this.visitor);
	}

	@Override
	public void visit(DataFrag arg0) {
		// TODO: extract the label and the string and install
		// into hash table.
		this.stringMap.put(arg0.toString(), arg0.toString());
	}
}