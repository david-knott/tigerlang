package com.chaosopher.tigerlang.irinterp;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Hashtable;

import com.chaosopher.tigerlang.compiler.translate.DataFrag;
import com.chaosopher.tigerlang.compiler.translate.FragmentVisitor;
import com.chaosopher.tigerlang.compiler.translate.ProcFrag;

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
		this.stringMap.put(arg0.toString(), arg0.toString());
	}
}