package com.chaosopher.tigerlang.compiler.absyn;

public class ExpList extends Absyn {
   public Exp head;
   public ExpList tail;

   public ExpList(Exp h, ExpList t) {
      head = h;
      tail = t;
   }

   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }
}
