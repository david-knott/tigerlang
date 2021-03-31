package com.chaosopher.tigerlang.compiler.graph;

import java.util.Iterator;

public class NodeList implements Iterable<Node> {
    public Node head;
    public NodeList tail;

    public NodeList(Node h) {
        head = h;
        tail = null;
    }

    public NodeList(Node h, NodeList t) {
        head = h;
        tail = t;
    }

    public boolean contains(Node n) {
        for (NodeList s = this; s != null; s = s.tail) {
            if (s.head == n) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        int size = 0;
        for (NodeList s = this; s != null; s = s.tail) {
            size++;
        }
        return size;
    }

    public NodeList append(Node t) {
        if (this.tail == null) {
            return new NodeList(this.head, new NodeList(t));
        }
        return new NodeList(this.head, this.tail.append(t));
    }

    /**
     * Return this set in reverse.
     * 
     * @return a new linked list with this lists elements in reverse.
     */
    public NodeList reverse() {
        if (this.tail == null) {
            return new NodeList(this.head);
        }
        return this.tail.reverse().append(this.head);
    }

    /**
     * Returns a new list without node. The original
     * list is unaffected
     * @param node the node to remove.
     * @return a new list with node node.
     */
    public NodeList remove(Node node) {
        var start = this;
        NodeList nodeList = null;
        if (this.head == node) {
            start = this.tail;
        }
        if (start == null) {
            return null;
        }
        for (; start != null; start = start.tail) {
            if (start.head == node)
                continue;
            if (nodeList == null) {
                nodeList = new NodeList(start.head);
            } else {
                nodeList = nodeList.append(start.head);
            }
        }
        return nodeList;
    }

    @Override
    public Iterator<Node> iterator() {
        return new NodeListIterator(this);
    }

    class NodeListIterator implements Iterator<Node> {
        
        private NodeList nodeList;

        NodeListIterator(NodeList nodeList) {
            this.nodeList = nodeList;
        }

        @Override
        public boolean hasNext() {
            return this.nodeList != null;
        }

        @Override
        public Node next() {
            Node current = nodeList.head;
            nodeList = nodeList.tail;
            return current;
        }
    }
}
