package com.chaosopher.tigerlang.compiler.absyn;

/**
 * DecList class is a singled linked list of @see Absyn.Dec items.
 */
public class DecList extends Absyn {
   public Dec head;
   public DecList tail;

   public DecList(Dec h, DecList t) {
      head = h;
      tail = t;
   }

   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }
}
