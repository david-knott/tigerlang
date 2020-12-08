package com.chaosopher.tigerlang.compiler.absyn;

import com.chaosopher.tigerlang.compiler.symbol.Symbol;

/**
 * Represents a list of fields in a record. This is used by record types.
 * By default this ast escapes, unless the FindEscape phase marks this item as
 * not escaping.
 */
public class FieldList extends Absyn {
   public Symbol name;
 //  public Symbol typ;
   public NameTy typ;
   public FieldList tail;
   public boolean escape = true;
   /**
    * Pointer to Type definition of this FieldList.
    */
   public Absyn def;

   public FieldList(int p, Symbol n, NameTy t, FieldList x) {
      pos = p;
      name = n;
      typ = t;
      tail = x;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }

   public void setDef(Absyn exp) {
      this.def = exp;
   }
}
