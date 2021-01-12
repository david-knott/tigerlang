package com.chaosopher.tigerlang.compiler.absyn;

import com.chaosopher.tigerlang.compiler.util.Assert;

/**
 * DecList class is a singled linked list of @see com.chaosopher.tigerlang.compiler.absyn.Dec items.
 */
public class DecList extends Absyn {
   public Dec head;
   public DecList tail;

   public DecList(Dec h, DecList t) {
      Assert.assertNotNull(h, "Head cannot be null");
      head = h;
      tail = t;
   }

   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }
}
