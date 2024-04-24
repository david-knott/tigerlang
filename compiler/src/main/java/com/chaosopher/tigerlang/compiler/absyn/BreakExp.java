package com.chaosopher.tigerlang.compiler.absyn;

public class BreakExp extends Exp {
   public Absyn loop;

   public BreakExp(int p) {
      pos = p;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }
}
