package com.chaosopher.tigerlang.compiler.callgraph;

import java.util.Hashtable;

import com.chaosopher.tigerlang.compiler.absyn.FunctionDec;
import com.chaosopher.tigerlang.compiler.graph.Graph;
import com.chaosopher.tigerlang.compiler.graph.Node;
import com.chaosopher.tigerlang.compiler.graph.NodeList;
import com.chaosopher.tigerlang.compiler.util.Assert;

public class FunctionCallGraph extends Graph {

    Hashtable<FunctionDec, Node> functionDecs = new Hashtable<FunctionDec, Node>();
    Hashtable<Node, FunctionDec> functionDecsRev = new Hashtable<Node, FunctionDec>();

	public boolean inCycle(FunctionDec exp) {
        Assert.assertNotNull(this.functionDecs.get(exp), "No declaration found for " + exp.name);
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

    /**
     * Print a human-readable dump for debugging.
     */
    public void show(java.io.PrintStream out) {
        for (NodeList p = nodes(); p != null; p = p.tail) {
            Node n = p.head;
            FunctionDec functionDec = this.getFunctionDec(n);
            out.print(functionDec.name);
            //out.print(" id " + n.inDegree());
            //out.print(" od " + n.outDegree());
            out.print(" calls ");
            for (NodeList q = n.succ(); q != null; q = q.tail) {
                FunctionDec functionDecS = this.getFunctionDec(q.head);
                out.print(functionDecS.name);
                out.print(" ");
            }
            out.println();
        }
    }
}