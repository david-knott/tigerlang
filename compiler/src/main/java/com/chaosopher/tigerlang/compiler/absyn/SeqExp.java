package com.chaosopher.tigerlang.compiler.absyn;

public class SeqExp extends Exp {
   public ExpList list;

   public SeqExp(int p, ExpList l) {
      pos = p;
      list = l;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }
}
