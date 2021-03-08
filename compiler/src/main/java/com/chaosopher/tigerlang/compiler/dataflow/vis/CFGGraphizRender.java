package com.chaosopher.tigerlang.compiler.dataflow.vis;

import java.io.PrintStream;

import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.graph.Node;
import com.chaosopher.tigerlang.compiler.graph.NodeList;
import com.chaosopher.tigerlang.compiler.tree.QuadruplePrettyPrinter;
import com.chaosopher.tigerlang.compiler.tree.StmList;

public class CFGGraphizRender {

    private final CFG cfg;

    public CFGGraphizRender(CFG cfg) {
        this.cfg = cfg;
    }

    public void write(PrintStream out) {
        out.println("digraph D{");
        for (NodeList p = this.cfg.nodes(); p != null; p = p.tail) {
            Node n = p.head;
            BasicBlock basicBlock = this.cfg.get(n);
            out.print(n.toString());
            out.print(" [label=\"");
            for(StmList stmList = basicBlock.first; stmList != null; stmList = stmList.tail) {
                stmList.head.accept(new QuadruplePrettyPrinter(out));
                out.println();
            }
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