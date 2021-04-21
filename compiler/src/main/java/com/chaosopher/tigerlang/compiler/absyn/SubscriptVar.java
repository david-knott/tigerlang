package com.chaosopher.tigerlang.compiler.absyn;

/**
 * A subcript variable, of the form a[i], where a is the
 * base variable and i is the index. This has the type of
 * the array element.
 */
public class SubscriptVar extends Var {
   public Var var;
   public Exp index;

   public SubscriptVar(int p, Var v, Exp i) {
      pos = p;
      var = v;
      index = i;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }
}
