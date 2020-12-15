package com.chaosopher.tigerlang.compiler.staticlink;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.CallExp;
import com.chaosopher.tigerlang.compiler.absyn.DefaultVisitor;
import com.chaosopher.tigerlang.compiler.absyn.FunctionDec;
import com.chaosopher.tigerlang.compiler.callgraph.CallGraphVisitor;
import com.chaosopher.tigerlang.compiler.callgraph.FunctionCallGraph;
import com.chaosopher.tigerlang.compiler.graph.Node;
import com.chaosopher.tigerlang.compiler.graph.NodeList;

class StaticLinkEscapeVisitor extends DefaultVisitor {

    private FunctionCallGraph functionCallGraph;

    public StaticLinkEscapeVisitor(Absyn tree) {
        CallGraphVisitor callGraphVisitor = new CallGraphVisitor();
        tree.accept(callGraphVisitor);
        this.functionCallGraph = callGraphVisitor.functionCallGraph;
    }

    @Override
    public void visit(CallExp exp) {
        FunctionDec functionDec = (FunctionDec)exp.def;
        Node node = this.functionCallGraph.getNode(functionDec);
        NodeList nodeList = node.succ();
        if(nodeList == null) {
            functionDec.slEscapes = false;
            return;
        }
        // function only has one successor and it is recursive.
        if(nodeList.tail == null && nodeList.contains(node)) {
            functionDec.slEscapes = false;
            return;
        }
        // if this function only calls other functions at the same level
        // then static does not escape
        for(NodeList succs = node.succ(); succs != null; succs = succs.tail) {
            if(this.functionCallGraph.getFunctionDec(succs.head).level != functionDec.level) {
                functionDec.slEscapes = true;
                return;
                
            }
        }
        functionDec.slEscapes = false;
        super.visit(exp);
    }
}