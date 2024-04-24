package com.chaosopher.tigerlang.compiler.callgraph;

import java.util.Hashtable;

import com.chaosopher.tigerlang.compiler.absyn.CallExp;
import com.chaosopher.tigerlang.compiler.absyn.DefaultVisitor;
import com.chaosopher.tigerlang.compiler.absyn.FunctionDec;
import com.chaosopher.tigerlang.compiler.graph.Node;
import com.chaosopher.tigerlang.compiler.util.Assert;

/**
 * Creates a call graph of functions, 
 * eg function a calls b call c.
 */
public class CallGraphVisitor extends DefaultVisitor {

    FunctionDec visitedFunction;
    public FunctionCallGraph functionCallGraph;
    Hashtable<FunctionDec, Node> functionDecs = new Hashtable<FunctionDec, Node>();
    
    public CallGraphVisitor() {
        this.functionCallGraph = new FunctionCallGraph();
    }
    
    @Override
    public void visit(FunctionDec exp) {
        FunctionDec prev = this.visitedFunction;
        this.visitedFunction = exp;
        super.visit(exp);
        this.visitedFunction = prev;
    }

    @Override
    public void visit(CallExp exp) {
        FunctionDec src = this.visitedFunction;
        FunctionDec def = (FunctionDec)exp.def;
        Assert.assertNotNull(def, "No definition found for " + exp.func);
        if(src != null) {
            this.functionCallGraph.addEdge(src, def);
        }
        super.visit(exp);
    }
}