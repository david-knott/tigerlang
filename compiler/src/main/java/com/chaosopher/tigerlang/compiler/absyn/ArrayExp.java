package com.chaosopher.tigerlang.compiler.absyn;

/**
 * An ArrayExp class used to define a new tiger array. This
 * includes the array size expression and array initializer values. 
 */
public class ArrayExp extends Exp {
  // public Symbol typ;
   public NameTy typ;
   public Exp size, init;
   public Absyn def;

   /**
    * Default constructor
    * @param p the related character position in the source file
    * @param t the NameTy that represents the element type
    * @param s the expression that supplies the array size
    * @param i the expression that supplies the element initial values.
    */
   public ArrayExp(int p, NameTy t, Exp s, Exp i) {
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
