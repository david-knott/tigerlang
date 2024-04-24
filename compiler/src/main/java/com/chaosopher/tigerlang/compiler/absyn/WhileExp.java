package com.chaosopher.tigerlang.compiler.absyn;

public class WhileExp extends Exp {
   public Exp test, body;

   public WhileExp(int p, Exp t, Exp b) {
      pos = p;
      test = t;
      body = b;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }
}
