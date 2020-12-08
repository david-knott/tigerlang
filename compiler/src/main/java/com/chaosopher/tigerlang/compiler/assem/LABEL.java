package com.chaosopher.tigerlang.compiler.assem;

public class LABEL extends Instr {
   public com.chaosopher.tigerlang.compiler.temp.Label label;

   public LABEL(String a, com.chaosopher.tigerlang.compiler.temp.Label l) {
      assem = a;
      label = l;
   }

   public com.chaosopher.tigerlang.compiler.temp.TempList use() {
      return null;
   }

   @Override
   public String toString(){
      return assem;
   }

   public com.chaosopher.tigerlang.compiler.temp.TempList def() {
      return null;
   }

   public Targets jumps() {
      return null;
   }


}
