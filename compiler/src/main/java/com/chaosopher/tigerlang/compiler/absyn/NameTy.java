package com.chaosopher.tigerlang.compiler.absyn;

import com.chaosopher.tigerlang.compiler.symbol.Symbol;
import com.chaosopher.tigerlang.compiler.util.Assert;

/**
 * A NameTy class represents a named type within the asbtract syntax tree. This
 * could be a primitive such as an int or string, or it could be a user defined
 * type that wascreated using the type construct.
 */
public class NameTy extends Ty {
   public Symbol name;
   public Absyn def;

   public NameTy(int p, Symbol n) {
      pos = p;
      name = n;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }

   public void setDef(Absyn exp) {
      Assert.assertNotNull(exp);
      this.def = exp;
   }
}