package com.chaosopher.tigerlang.compiler.flowgraph;

import java.util.Hashtable;

import com.chaosopher.tigerlang.compiler.assem.Instr;
import com.chaosopher.tigerlang.compiler.assem.LABEL;
import com.chaosopher.tigerlang.compiler.graph.Node;
import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.temp.TempList;

public class AssemFlowGraph extends FlowGraph {

    private Hashtable<Node, com.chaosopher.tigerlang.compiler.assem.Instr> nodeMap = new Hashtable<Node, com.chaosopher.tigerlang.compiler.assem.Instr>();
    private Hashtable<com.chaosopher.tigerlang.compiler.assem.Instr, Node> invNodeMap = new Hashtable<com.chaosopher.tigerlang.compiler.assem.Instr, Node>();
    private Hashtable<Label, Instr> labelInstr = new Hashtable<Label, Instr>();

    private Node getOrCreate(Instr instr) {
        if(invNodeMap.containsKey(instr)) {
            return invNodeMap.get(instr);
        }
        Node node = this.newNode();
        nodeMap.put(node, instr);
        invNodeMap.put(instr, node);
        return node;
    }

    public AssemFlowGraph(com.chaosopher.tigerlang.compiler.assem.InstrList instrs) {
        //add all the labels to a hashtable first
        //add the labels to the node map
        for (com.chaosopher.tigerlang.compiler.assem.InstrList p = instrs; p != null; p = p.tail) {
            if(p.head instanceof LABEL){
                var l = ((LABEL)p.head).label;
                labelInstr.put(l, p.head);
                //this.getOrCreate(p.head);
            }
        }
        com.chaosopher.tigerlang.compiler.assem.InstrList p = instrs;
        Node prevNode = this.getOrCreate(p.head);
        p = p.tail;
        for (; p != null; p = p.tail) {
            Node node = this.getOrCreate(p.head);
            var prevInstr = instr(prevNode);
            var targets = prevInstr.jumps();
            if(targets != null) {
                for (var t = targets.labels; t != null; t = t.tail) {
                    var jinstr = labelInstr.get(t.head);
                    if(jinstr == null) {
                        throw new Error("No instruction for " + t.head);
                    }
                    Node jNode = this.getOrCreate(jinstr);
                    this.addEdge(prevNode, jNode);
                }
            } else {
                this.addEdge(prevNode, node);                
            }
            prevNode = node;
        }
    }

    @Override
    public com.chaosopher.tigerlang.compiler.assem.Instr instr(Node node) {
        return nodeMap.get(node);
    }

    @Override
    public Node node(Instr instr) {
        return invNodeMap.get(instr);
    }

    @Override
    public TempList def(Node node) {
        return instr(node).def();
    }

    @Override
    public TempList use(Node node) {
        return instr(node).use();
    }

    @Override
    public boolean isMove(Node node) {
        return instr(node) instanceof com.chaosopher.tigerlang.compiler.assem.MOVE;
    }

}