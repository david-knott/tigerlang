package com.chaosopher.tigerlang.compiler.types;

public class ARRAY extends Type {
   public final Type element;

   public ARRAY(Type e) {
      element = e;
   }

   public boolean coerceTo(Type t) {
      return this == t.actual();
   }

   public void accept(GenVisitor genVisitor) {
      genVisitor.visit(this);
   }
}
