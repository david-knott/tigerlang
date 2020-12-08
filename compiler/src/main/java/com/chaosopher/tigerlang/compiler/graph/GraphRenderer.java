package com.chaosopher.tigerlang.compiler.graoh;

import java.io.PrintStream;

import com.chaosopher.tigerlang.compiler.flowgraph.FlowGraph;
import com.chaosopher.tigerlang.compiler.regalloc.InterferenceGraph;
import com.chaosopher.tigerlang.compiler.temp.TempMap;

public interface GraphRenderer {

	void render(PrintStream out, InterferenceGraph graph, TempMap tempMap);

	void render(PrintStream out, FlowGraph graph, TempMap tempMap);
}