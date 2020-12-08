package com.chaosopher.tigerlang.compiler.absyn;

import com.chaosopher.tigerlang.compiler.symbol.Symbol;

public class TypeDec extends Dec {
   public Symbol name;
   public Ty ty;
   public TypeDec next;

   public TypeDec(int p, Symbol n, Ty t, TypeDec x) {
      pos = p;
      name = n;
      ty = t;
      next = x;
   }

   public String toString() {
      return "[TypeDec:{name=" + name + ",ty=" + ty + ",next=" + next + "}]";
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }
}
