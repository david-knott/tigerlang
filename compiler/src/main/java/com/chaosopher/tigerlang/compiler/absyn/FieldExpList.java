package com.chaosopher.tigerlang.compiler.absyn;

import com.chaosopher.tigerlang.compiler.symbol.Symbol;

public class FieldExpList extends Absyn {
   public Symbol name;
   public Exp init;
   public FieldExpList tail;

   public FieldExpList(int p, Symbol n, Exp i, FieldExpList t) {
      pos = p;
      name = n;
      init = i;
      tail = t;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }
}
