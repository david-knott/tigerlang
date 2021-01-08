package com.chaosopher.tigerlang.compiler.absyn;

import com.chaosopher.tigerlang.compiler.symbol.Symbol;
import com.chaosopher.tigerlang.compiler.types.Type;

public class FunctionDec extends Dec implements TypeConstructor {
   public Symbol name;
   public DecList params;
   public NameTy result; /* optional */
   public Exp body;
   public FunctionDec next;
   public boolean sl = true;
   /* defaults to true, meaning static link is stored in frame */
   public boolean staticLinkEscapes = true;
   public int level; 
   private Type createdType;

   public boolean includeStaticLink() {
      return sl && body != null;
   }

   public FunctionDec(int p, Symbol n, DecList a, NameTy r, Exp b, FunctionDec x) {
      pos = p;
      name = n;
      params = a;
      result = r;
      body = b;
      next = x;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }

   /**
    * Returns the created type for this function, FUNCTION type.
    */
   @Override
   public Type getCreatedType() {
      return this.createdType;
   }

   /**
    * Sets the created type for this function. 
    */
   @Override
   public void setCreatedType(Type type) {
      this.createdType = type;
   }
}
