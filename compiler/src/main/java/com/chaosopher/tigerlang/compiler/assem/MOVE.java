package com.chaosopher.tigerlang.compiler.assem;

public class MOVE extends Instr {
   public Temp.Temp dst;
   public Temp.Temp src;

   public MOVE(String a, Temp.Temp d, Temp.Temp s) {
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

   public Temp.TempList use() {
      return new Temp.TempList(src, null);
   }

   public Temp.TempList def() {
      return new Temp.TempList(dst, null);
   }

   public Targets jumps() {
      return null;
   }

}
