package com.chaosopher.tigerlang.compiler.absyn;

public class StringExp extends Exp {
   public String value;

   public StringExp(int p, String v) {
      pos = p;
      value = v;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }
}
