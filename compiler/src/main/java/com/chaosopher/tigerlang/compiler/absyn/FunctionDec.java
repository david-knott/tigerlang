package com.chaosopher.tigerlang.compiler.absyn;

import com.chaosopher.tigerlang.compiler.symbol.Symbol;

public class FunctionDec extends Dec {
   public Symbol name;
   public DecList params;
   public NameTy result; /* optional */
   public Exp body;
   public FunctionDec next;
   public boolean sl = true;

   public boolean staticLink() {
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
}
