package com.chaosopher.tigerlang.compiler.dataflow.vis;

import java.io.PrintStream;

import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.graph.Node;
import com.chaosopher.tigerlang.compiler.graph.NodeList;
import com.chaosopher.tigerlang.compiler.translate.DataFrag;
import com.chaosopher.tigerlang.compiler.translate.FragList;
import com.chaosopher.tigerlang.compiler.translate.FragmentVisitor;
import com.chaosopher.tigerlang.compiler.translate.ProcFrag;
import com.chaosopher.tigerlang.compiler.tree.QuadruplePrettyPrinter;
import com.chaosopher.tigerlang.compiler.tree.StmList;

class CFGGraphizRender2 implements FragmentVisitor {
    private final PrintStream out;

    public CFGGraphizRender2(PrintStream out) {
        this.out = out;
    }

    public void start(FragList fragList) {
        this.out.println("digraph D{");
        fragList.accept(this);
        this.out.println("}");
    }

    @Override
    public void visit(ProcFrag procFrag) {
        CFG cfg = CFG.build((StmList) procFrag.body);
        for (NodeList p = cfg.nodes(); p != null; p = p.tail) {
            Node n = p.head;
            BasicBlock basicBlock = cfg.get(n);
            out.print(n.hashCode());
            out.print(" [label=\"");
            for(StmList stmList = basicBlock.first; stmList != null; stmList = stmList.tail) {
                stmList.head.accept(new QuadruplePrettyPrinter(out));
                out.println();
            }
            out.println("\"]");
        }
        for (NodeList p = cfg.nodes(); p != null; p = p.tail) {
            Node n = p.head;
            out.print(n.hashCode());
            out.print(" -> {");
            for (NodeList q = n.succ(); q != null; q = q.tail) {
                out.print(q.head.hashCode());
                if (q.tail != null) {
                    out.print(",");
                }
            }
            out.println("}");
        }
    }

    @Override
    public void visit(DataFrag dataFrag) {
        // do nothing.
    }
}