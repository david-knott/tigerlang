package com.chaosopher.tigerlang.compiler.assem;

public class MOVE extends Instr {
   public com.chaosopher.tigerlang.compiler.temp.Temp dst;
   public com.chaosopher.tigerlang.compiler.temp.Temp src;

   public MOVE(String a, com.chaosopher.tigerlang.compiler.temp.Temp d, com.chaosopher.tigerlang.compiler.temp.Temp s) {
      if (a == null)
         throw new Error("a");
      if (d == null)
         throw new Error("d");
      if (s == null)
         throw new Error("s");
      assem = a;
      dst = d;
      src = s;
   }

   public com.chaosopher.tigerlang.compiler.temp.TempList use() {
      return new com.chaosopher.tigerlang.compiler.temp.TempList(src, null);
   }

   public com.chaosopher.tigerlang.compiler.temp.TempList def() {
      return new com.chaosopher.tigerlang.compiler.temp.TempList(dst, null);
   }

   public Targets jumps() {
      return null;
   }

}
