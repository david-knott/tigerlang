package com.chaosopher.tigerlang.compiler.absyn;

public class VarExp extends Exp {
   public Var var;

   public VarExp(int p, Var v) {
      pos = p;
      var = v;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }
}
