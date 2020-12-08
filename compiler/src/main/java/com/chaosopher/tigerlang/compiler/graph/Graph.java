package com.chaosopher.tigerlang.compiler.graoh;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Stack;

import com.chaosopher.tigerlang.compiler.parse.sym;

public class Graph {

    int nodecount = 0;
    NodeList mynodes, mylast;

    /**
     * Returns true if this graph contains any cycles.
     * @return true if this graph contains any cycles.
     */
    public boolean isCyclic() {
        boolean[] visisted = new boolean[this.nodecount];
        boolean[] stack = new boolean[this.nodecount];
        // executes a DFS search on seach node in the graph.
        for(NodeList nodeList = this.mynodes; nodeList != null; nodeList = nodeList.tail) {
            if(this.isCyclic(nodeList.head, visisted, stack)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Recursive helper function for cyclic test.
     * @return true of this graph contains any cycles.
     */
    private boolean isCyclic(Node node, boolean[] visited, boolean[] stack) {
        // have we already seen this node in the call stack
        if(stack[node.mykey]) {
            return true;
        }
        // if node is not in the call stack and has been already
        // visited then its not in a cycle. This is because it
        // has already been visited in another DFS tree search.
        if(visited[node.mykey]) {
            return false;
        }
        visited[node.mykey] = true;
        stack[node.mykey] = true;
        for(NodeList succs = node.succs; succs != null; succs = succs.tail) {
            if(this.isCyclic(succs.head, visited, stack)) {
                return true;
            }
        }
        stack[node.mykey] = false;
        return false;
    }

    /**
     * Returns true if node is in a cycle.
     * 
     * @param n node to check if part of cycle.
     * @return true if node is contained in a cycle.
     */
    public boolean inCycle(Node n) {
        return this.inCycle(n, new boolean[this.nodecount]);
    }

    private boolean inCycle(Node n, boolean[] visited) {
        if(visited[n.mykey]) {
            return true;
        }
        visited[n.mykey] = true;
        for(NodeList succs = n.succs; succs != null; succs = succs.tail) {
            if(this.inCycle(succs.head, visited)) {
                return true;
            }
        }
        return false;
    }

    /**
     * A matrix of all nodes that are connected. If two nodes are part of a cycle
     * both nodes will have a connection to each other.
     * @return
     */
    public int[][] getTransitiveClosureMatrix() {
        int[][] tc = new int[nodecount][nodecount];
        for (NodeList nl = this.mynodes; nl != null; nl = nl.tail) {
            Node node = nl.head;
            transitiveClosureHelper(tc, node, node);
        }
        return tc;
    }

    private void transitiveClosureHelper(int[][] tc, Node first, Node next) {
        tc[first.mykey][next.mykey] = 1;
        for (NodeList nl = next.succ(); nl != null; nl = nl.tail) {
            Node adjacentNode = nl.head;
            if (tc[first.mykey][adjacentNode.mykey] == 0)
                transitiveClosureHelper(tc, first, adjacentNode);
        }
    }

    public NodeList nodes() {
        return mynodes;
    }

    public Node newNode() {
        return new Node(this);
    }

    void check(Node n) {
        if (n.mygraph != this)
            throw new Error("Graph.addEdge using nodes from the wrong graph");
    }

    static boolean inList(Node a, NodeList l) {
        for (NodeList p = l; p != null; p = p.tail)
            if (p.head == a)
                return true;
        return false;
    }

    public void addEdge(Node from, Node to) {
        check(from);
        check(to);
        if (from.goesTo(to))
            return;
        to.preds = new NodeList(from, to.preds);
        from.succs = new NodeList(to, from.succs);
    }

    NodeList delete(Node a, NodeList l) {
        if (l == null)
            throw new Error("Graph.rmEdge: edge nonexistent");
        else if (a == l.head)
            return l.tail;
        else
            return new NodeList(l.head, delete(a, l.tail));
    }

    public void rmEdge(Node from, Node to) {
        to.preds = delete(from, to.preds);
        from.succs = delete(to, from.succs);
    }

    /**
     * Print a human-readable dump for debugging.
     */
    public void show(java.io.PrintStream out) {
        for (NodeList p = nodes(); p != null; p = p.tail) {
            Node n = p.head;
            out.print(n.toString());
            out.print(": ");
            for (NodeList q = n.succ(); q != null; q = q.tail) {
                out.print(q.head.toString());
                out.print(" ");
            }
            out.println();
        }
    }
}