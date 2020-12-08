package com.chaosopher.tigerlang.compiler.assem;

public class OPER extends Instr {
   public Temp.TempList dst;
   public Temp.TempList src;
   public Targets jump;
   public String comments;

   public OPER(String a, Temp.TempList d, Temp.TempList s, Temp.LabelList j, String comments) {
      if (a == null)
         throw new Error("a");
      if (d == null)
         throw new Error("d");
      if (s == null)
         throw new Error("s");
      if (j == null)
         throw new Error("j");
      assem = a;
      dst = d;
      src = s;
      jump = new Targets(j);
   }

   public OPER(String a, Temp.TempList d, Temp.TempList s, Temp.LabelList j) {
      if (a == null)
         throw new Error("a");
      if (j == null)
         throw new Error("j");
      assem = a;
      dst = d;
      src = s;
      jump = new Targets(j);
   }

   public OPER(String a, Temp.TempList d, Temp.TempList s) {
      if (a == null)
         throw new Error("a");
      assem = a;
      dst = d;
      src = s;
      jump = null;
   }

   public Temp.TempList use() {
      return src;
   }

   public Temp.TempList def() {
      return dst;
   }

   public Targets jumps() {
      return jump;
   }

}
