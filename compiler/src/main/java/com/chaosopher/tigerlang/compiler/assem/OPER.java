package com.chaosopher.tigerlang.compiler.assem;

public class OPER extends Instr {
   public com.chaosopher.tigerlang.compiler.temp.TempList dst;
   public com.chaosopher.tigerlang.compiler.temp.TempList src;
   public Targets jump;
   public String comments;

   public OPER(String a, com.chaosopher.tigerlang.compiler.temp.TempList d, com.chaosopher.tigerlang.compiler.temp.TempList s, com.chaosopher.tigerlang.compiler.temp.LabelList j, String comments) {
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

   public OPER(String a, com.chaosopher.tigerlang.compiler.temp.TempList d, com.chaosopher.tigerlang.compiler.temp.TempList s, com.chaosopher.tigerlang.compiler.temp.LabelList j) {
      if (a == null)
         throw new Error("a");
      if (j == null)
         throw new Error("j");
      assem = a;
      dst = d;
      src = s;
      jump = new Targets(j);
   }

   public OPER(String a, com.chaosopher.tigerlang.compiler.temp.TempList d, com.chaosopher.tigerlang.compiler.temp.TempList s) {
      if (a == null)
         throw new Error("a");
      assem = a;
      dst = d;
      src = s;
      jump = null;
   }

   public com.chaosopher.tigerlang.compiler.temp.TempList use() {
      return src;
   }

   public com.chaosopher.tigerlang.compiler.temp.TempList def() {
      return dst;
   }

   public Targets jumps() {
      return jump;
   }

}
