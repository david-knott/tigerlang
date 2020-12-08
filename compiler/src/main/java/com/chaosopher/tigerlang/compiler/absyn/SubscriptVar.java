package com.chaosopher.tigerlang.compiler.absyn;

public class SubscriptVar extends Var {
   public Var var;
   public Exp index;

   public SubscriptVar(int p, Var v, Exp i) {
      pos = p;
      var = v;
      index = i;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }
}
