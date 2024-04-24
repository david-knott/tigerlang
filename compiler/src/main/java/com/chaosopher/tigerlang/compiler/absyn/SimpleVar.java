package com.chaosopher.tigerlang.compiler.absyn;

import com.chaosopher.tigerlang.compiler.symbol.Symbol;
import com.chaosopher.tigerlang.compiler.util.Assert;

/**
 * Represents a variable in a program, which is either a pointer
 * to memory or an integer literal.
 */
public class SimpleVar extends Var  {
   public Symbol name;

   public SimpleVar(int p, Symbol n) {
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
