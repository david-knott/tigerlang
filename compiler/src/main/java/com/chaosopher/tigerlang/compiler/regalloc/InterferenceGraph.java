package com.chaosopher.tigerlang.compiler.regalloc;

import com.chaosopher.tigerlang.compiler.graph.Node;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.graph.Graph;

/**
 * Graphiz renderer uses this, however we dont actually use
 * this class in the compiler.
 */
abstract public class InterferenceGraph extends Graph {

   abstract public Node tnode(Temp temp);

   abstract public Temp gtemp(Node node);

   abstract public void bind(Node node, Temp temp);

   abstract public MoveList moves();

   public void addEdge(Node from, Node to, boolean move) {
      if (from == to)
         return;
      this.addEdge(from, to);
   }

   public int spillCost(Node node) {
      return 1;
   }
}
