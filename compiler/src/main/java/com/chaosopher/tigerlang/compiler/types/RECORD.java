package com.chaosopher.tigerlang.compiler.types;

public class RECORD extends Type {
   public com.chaosopher.tigerlang.compiler.symbol.Symbol fieldName;
   public Type fieldType;
   public RECORD tail;

   public RECORD(com.chaosopher.tigerlang.compiler.symbol.Symbol n, Type t, RECORD x) {
      if (n == null)
         throw new IllegalArgumentException("Symbol n cannot be null");
      if (t == null)
         throw new IllegalArgumentException("Type t cannot be null");
      fieldName = n;
      fieldType = t;
      tail = x;
   }

   public RECORD append(com.chaosopher.tigerlang.compiler.symbol.Symbol n, Type t) {
      if (n == null)
         throw new IllegalArgumentException("Symbol n cannot be null");
      if (t == null)
         throw new IllegalArgumentException("Type t cannot be null");
      var last = this;
      while (last.tail != null) {
         last = last.tail;
      }
      last.tail = new RECORD(n, t, null);
      return last.tail;
   }

   public boolean coerceTo(Type t) {
      if (t == null)
         throw new IllegalArgumentException("Type t cannot be null");
      return this == t.actual();
   }

   public void accept(GenVisitor genVisitor) {
      genVisitor.visit(this);
   }
}
