package com.chaosopher.tigerlang.compiler.absyn;

public class RecordExp extends Exp {
   //public Symbol typ;
   public NameTy typ;
   public Absyn def;
   public FieldExpList fields;

   public RecordExp(int p, NameTy t, FieldExpList f) {
      pos = p;
      typ = t;
      fields = f;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }

    public void setDef(Absyn exp) {
        this.def = exp;
    }
}
