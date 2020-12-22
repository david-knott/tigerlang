package com.chaosopher.tigerlang.compiler.absyn;

import com.chaosopher.tigerlang.compiler.symbol.Symbol;
import com.chaosopher.tigerlang.compiler.types.Type;

public class TypeDec extends Dec implements TypeConstructor {
   public Symbol name;
   public Ty ty;
   public TypeDec next;
   private Type createdType;

   public TypeDec(int p, Symbol n, Ty t, TypeDec x) {
      pos = p;
      name = n;
      ty = t;
      next = x;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }

   @Override
   public Type getCreatedType() {
      return this.createdType;
   }

   @Override
   public void setCreatedType(Type type) {
      this.createdType = type;
   }
}
