package com.chaosopher.tigerlang.compiler.callgraph;

import java.util.Hashtable;

import com.chaosopher.tigerlang.compiler.absyn.FunctionDec;
import com.chaosopher.tigerlang.compiler.graph.Graph;
import com.chaosopher.tigerlang.compiler.graph.Node;

public class FunctionCallGraph extends Graph {

    Hashtable<FunctionDec, Node> functionDecs = new Hashtable<FunctionDec, Node>();
    Hashtable<Node, FunctionDec> functionDecsRev = new Hashtable<Node, FunctionDec>();

	public boolean inCycle(FunctionDec exp) {
		return super.inCycle(this.functionDecs.get(exp));
    }

    public Node getNode(FunctionDec functionDec) {
        return this.functionDecs.get(functionDec);
    }

    public FunctionDec getFunctionDec(Node node) {
        return this.functionDecsRev.get(node);
    }
    
    private Node getOrCreateNode(FunctionDec src) {
        Node srcNode = null;
        if(this.functionDecs.containsKey(src)) {
            srcNode = this.functionDecs.get(src);
        } else {
            srcNode = this.newNode();
            this.functionDecs.put(src, srcNode);
            this.functionDecsRev.put(srcNode, src);
        }
        return srcNode;
    }

    public void addEdge(FunctionDec src, FunctionDec args) {
        Node srcNode = this.getOrCreateNode(src);
        Node destNode = this.getOrCreateNode(args);
        this.addEdge(srcNode, destNode);
    }

}