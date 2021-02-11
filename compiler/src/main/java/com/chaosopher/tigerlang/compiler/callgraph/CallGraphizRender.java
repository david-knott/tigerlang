package com.chaosopher.tigerlang.compiler.callgraph;

import java.io.PrintStream;

import com.chaosopher.tigerlang.compiler.absyn.FunctionDec;
import com.chaosopher.tigerlang.compiler.graph.Node;
import com.chaosopher.tigerlang.compiler.graph.NodeList;

public class CallGraphizRender {

    private final FunctionCallGraph cfg;

	public CallGraphizRender(FunctionCallGraph functionCallGraph) {
        this.cfg = functionCallGraph;
	}

	public void write(PrintStream out) {
        out.println("digraph D{");
        for (NodeList p = this.cfg.nodes(); p != null; p = p.tail) {
            Node n = p.head;
            FunctionDec functionDec = this.cfg.getFunctionDec(n);
            out.print(n.toString());
            out.print(" [label=\"");
            out.print(functionDec.name.toString());
            out.println("\"]");
        }
        for (NodeList p = this.cfg.nodes(); p != null; p = p.tail) {
            Node n = p.head;
            out.print(n.toString());
            out.print(" -> {");
            for (NodeList q = n.succ(); q != null; q = q.tail) {
                out.print(q.head.toString());
                if (q.tail != null) {

                    out.print(",");
                }
            }
            out.println("}");
        }
        out.println("}");
        
	}
}
