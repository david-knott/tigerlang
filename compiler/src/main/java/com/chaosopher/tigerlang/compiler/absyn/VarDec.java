package com.chaosopher.tigerlang.compiler.absyn;

import com.chaosopher.tigerlang.compiler.symbol.Symbol;

/**
 * This class represents the declaration of a variable.
 * The declarion can contain an optional type.
 */
public class VarDec extends Dec {
   public Symbol name;
   public boolean escape = true;
   public NameTy typ; /* optional */
   public Exp init;

   public VarDec(int p, Symbol n, NameTy t, Exp i) {
      pos = p;
      name = n;
      typ = t;
      init = i;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }
}
