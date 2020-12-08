package com.chaosopher.tigerlang.compiler.absyn;

import com.chaosopher.tigerlang.compiler.symbol.Symbol;

/**
 * An ArrayExp class used to define a new tiger array.
 */
public class ArrayExp extends Exp {
   public Symbol typ;
   public Exp size, init;
   public Absyn def;

   /**
    * Default constructor
    * @param p the related character position in the source file
    * @param t the symbol that represents this class
    * @param s the expression that supplies the array size
    * @param i the expression that supplies the element initial values.
    */
   public ArrayExp(int p, Symbol t, Exp s, Exp i) {
      pos = p;
      typ = t;
      size = s;
      init = i;
   }

   public String toString() {
      return "[ArrayExp:{typ=" + typ + ",size=" + size + ",init=" + init + "}]";
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }

    public void setDef(Absyn exp) {
        this.def = exp;
    }


}
