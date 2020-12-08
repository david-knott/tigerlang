package com.chaosopher.tigerlang.compiler.regalloc;

public class MoveList {
   public com.chaosopher.tigerlang.compiler.graph.Node src, dst;
   public MoveList tail;

   public MoveList(com.chaosopher.tigerlang.compiler.graph.Node s, com.chaosopher.tigerlang.compiler.graph.Node d, MoveList t) {
      if (s == null)
         throw new Error("s");
      if (d == null)
         throw new Error("d");
      src = s;
      dst = d;
      tail = t;
   }

   public boolean contains(com.chaosopher.tigerlang.compiler.graph.Node s, com.chaosopher.tigerlang.compiler.graph.Node d) {
      MoveList item = this;
      for (; item != null; item = item.tail) {
         if (item.src == s && item.dst == d) {
            return true;
         }
         if (item.src == d && item.dst == s) {
            return true;
         }
      }
      return false;
   }
}
