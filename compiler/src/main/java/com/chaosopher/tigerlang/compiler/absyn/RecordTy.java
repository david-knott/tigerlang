package com.chaosopher.tigerlang.compiler.absyn;

public class RecordTy extends Ty {
   public FieldList fields;

   public RecordTy(int p, FieldList f) {
      pos = p;
      fields = f;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }
}