package com.chaosopher.tigerlang.compiler.staticlink;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.CallExp;
import com.chaosopher.tigerlang.compiler.absyn.DefaultVisitor;
import com.chaosopher.tigerlang.compiler.absyn.FunctionDec;
import com.chaosopher.tigerlang.compiler.callgraph.CallGraphVisitor;
import com.chaosopher.tigerlang.compiler.callgraph.FunctionCallGraph;
import com.chaosopher.tigerlang.compiler.graph.Node;
import com.chaosopher.tigerlang.compiler.graph.NodeList;
import com.chaosopher.tigerlang.compiler.translate.Level;

class StaticLinkEscapeVisitor extends DefaultVisitor {

    private FunctionCallGraph functionCallGraph;

    public StaticLinkEscapeVisitor(Absyn tree) {
        CallGraphVisitor callGraphVisitor = new CallGraphVisitor();
        tree.accept(callGraphVisitor);
        this.functionCallGraph = callGraphVisitor.functionCallGraph;
    }

    @Override
    public void visit(CallExp exp) {
        // visit the children
        super.visit(exp);

        FunctionDec functionDec = (FunctionDec)exp.def;
        Node node = this.functionCallGraph.getNode(functionDec);
        NodeList nodeList = node.succ();
        if(nodeList == null) {
            // function is a leaf function, static link can be passed in register.
            System.out.println(exp.func + " : has no successors - setting sl escapes to false");
            functionDec.staticLinkEscapes = false;
            return;
        }
        
        // function only has one successor and it is recursive, meaning it calls itself.
        if(nodeList.tail == null && nodeList.contains(node)) {
            System.out.println(exp.func + " : function is recursive - setting sl escapes to false");
            functionDec.staticLinkEscapes = false;
            return;
        }
        // if this function calls a function at a different level, then the static link escapes.
        boolean callDifferentLevel = false;
        for(NodeList succs = node.succ(); succs != null; succs = succs.tail) {
            FunctionDec succFunc = this.functionCallGraph.getFunctionDec(succs.head);
            System.out.println(functionDec.name + " calls " + succFunc.name);
            int level =  functionDec.level;
            int level2 =  this.functionCallGraph.getFunctionDec(succs.head).level;
            if(this.functionCallGraph.getFunctionDec(succs.head).level != functionDec.level) {
                System.out.println(exp.func + " : calls function at different level - setting sl escapes to true");
                callDifferentLevel = true;
                break;
            }
        }
        if(!callDifferentLevel) {
            functionDec.staticLinkEscapes = false;
            System.out.println(exp.func + " : only calls function at same level - setting sl escapes to false");
        }
    }
}