package com.chaosopher.tigerlang.compiler.absyn;

import com.chaosopher.tigerlang.compiler.symbol.Symbol;

/**
 * A field var is used to refer to a field within a record instance.
 * There var is a reference to the base record and field is the record
 * property.
 * The abstract syntax type is the same as field variable type.
 */
public class FieldVar extends Var {
   public Var var;
   public Symbol field;

   public FieldVar(int p, Var v, Symbol f) {
      pos = p;
      var = v;
      field = f;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }
}
