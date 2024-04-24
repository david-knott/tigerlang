package com.chaosopher.tigerlang.compiler.absyn;

public class ForExp extends Exp {
   public final VarDec var;
   public final Exp hi, body;

   public ForExp(int p, VarDec v, Exp h, Exp b) {
      pos = p;
      var = v;
      hi = h;
      body = b;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }
}
