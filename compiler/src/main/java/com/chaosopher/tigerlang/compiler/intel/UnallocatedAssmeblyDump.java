package com.chaosopher.tigerlang.compiler.intel;

import java.io.OutputStream;
import java.io.PrintStream;

import com.chaosopher.tigerlang.compiler.assem.DataFrag;
import com.chaosopher.tigerlang.compiler.assem.FragmentVisitor;
import com.chaosopher.tigerlang.compiler.assem.InstrList;
import com.chaosopher.tigerlang.compiler.assem.ProcFrag;
import com.chaosopher.tigerlang.compiler.temp.DefaultMap;

final class UnallocatedAssmeblyDump implements FragmentVisitor {
	private final PrintStream printStream;

	UnallocatedAssmeblyDump(OutputStream out) {
		this.printStream = new PrintStream(out);
		this.printStream.println("#### unallocated assembly dunp ###");
	}

	@Override
	public void visit(ProcFrag procFrag) {
	    this.printStream.println("proc:" + procFrag.frame.name);
	    for(InstrList instrList = procFrag.body; instrList != null; instrList = instrList.tail) {
	        this.printStream.println(instrList.head.format(new DefaultMap()));
	    }
	    this.printStream.println();
	}

	@Override
	public void visit(DataFrag dataFrag) {
	    this.printStream.println("data:" + dataFrag.toString());
	}
}