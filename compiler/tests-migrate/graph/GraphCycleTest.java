package com.chaosopher.tigerlang.compiler.graph;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

public class GraphCycleTest {

    @Test
    public void graphContainsNoCycles() {
        Graph graph = new Graph();
        Node a = graph.newNode();
        Node b = graph.newNode();
        Node c = graph.newNode();
        Node d = graph.newNode();
        graph.addEdge(a, b);
        graph.addEdge(b, c);
        graph.addEdge(c, d);
        graph.addEdge(a, d);
        assertFalse(graph.isCyclic());
    }

    @Test
    public void graphContainsOneCycle() {
        Graph graph = new Graph();
        Node a = graph.newNode();
        Node b = graph.newNode();
        graph.addEdge(a, b);
        graph.addEdge(b, a);
        assertTrue(graph.isCyclic());
    }

    @Test
    public void graphContainsTwoCycle() {
        Graph graph = new Graph();
        Node a = graph.newNode();
        Node b = graph.newNode();
        Node c = graph.newNode();
        graph.addEdge(a, b);
        graph.addEdge(b, a);
        graph.addEdge(a, c);
        graph.addEdge(c, a);
        assertTrue(graph.isCyclic());
    }

    @Test
    public void graphContainsThreeCycle() {
        Graph graph = new Graph();
        Node a = graph.newNode();
        Node b = graph.newNode();
        Node c = graph.newNode();
        Node d = graph.newNode();
        graph.addEdge(a, b);
        graph.addEdge(b, a);
        graph.addEdge(a, c);
        graph.addEdge(c, a);
        graph.addEdge(c, d);
        graph.addEdge(d, a);
        assertTrue(graph.isCyclic());
    }





    @Test
    public void twoNodeNoCycle() {
        Graph graph = new Graph();
        Node a = graph.newNode();
        Node b = graph.newNode();
        graph.addEdge(a, b);
        assertFalse(graph.inCycle(a));
        assertFalse(graph.inCycle(b));
    }

    @Test
    public void twoNodeCycle() {
        Graph graph = new Graph();
        Node a = graph.newNode();
        Node b = graph.newNode();
        graph.addEdge(a, b);
        graph.addEdge(b, a);
        assertTrue(graph.inCycle(a));
        assertTrue(graph.inCycle(b));
    }


    @Test
    public void threeNodeNoCycle() {
        Graph graph = new Graph();
        Node a = graph.newNode();
        Node b = graph.newNode();
        Node c = graph.newNode();
        graph.addEdge(a, b);
        graph.addEdge(b, c);
        assertFalse(graph.inCycle(a));
        assertFalse(graph.inCycle(b));
        assertFalse(graph.inCycle(c));
    }

    @Test
    public void threeNodeStronglyConnected() {
        Graph graph = new Graph();
        Node a = graph.newNode();
        Node b = graph.newNode();
        Node c = graph.newNode();
        graph.addEdge(a, b);
        graph.addEdge(b, c);
        graph.addEdge(c, a);
        assertTrue(graph.inCycle(a));
        assertTrue(graph.inCycle(b));
        assertTrue(graph.inCycle(c));
    }

    @Test
    public void threeNodeCycle() {
        Graph graph = new Graph();
        Node a = graph.newNode();
        Node b = graph.newNode();
        Node c = graph.newNode();
        graph.addEdge(a, b);
        graph.addEdge(b, c);
        graph.addEdge(b, a);
        assertTrue(graph.inCycle(a));
        assertTrue(graph.inCycle(b));
        assertFalse(graph.inCycle(c));
    }
}