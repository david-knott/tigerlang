package com.chaosopher.tigerlang.compiler.assem;

import com.chaosopher.tigerlang.compiler.temp.TempList;

public class TEST extends Instr {

   public Temp.TempList dst;
   public Temp.TempList src;
   public Targets jump;
   public String comments;
   private static int id = 0;

   public TEST() {
      dst = null;
      src = null;
      jump = null;
      super.assem = "test null, null";
   }

   public TEST(Temp.TempList d, Temp.TempList s) {
      dst = d;
      src = s;
      jump = null;
      if (dst == null && src == null) {
         super.assem = "test null, null";
      } else if (src == null) {
         super.assem = "test null,  %`d0";
      } else if (dst == null) {
         super.assem = "test %`s0,  null";
      } else {
         super.assem = "test %`s0,  %`d0";
      }
   }

   public TEST(Temp.TempList d, Temp.TempList s, Temp.LabelList j) {
      if (j == null)
         throw new Error("j");
      dst = d;
      src = s;
      jump = new Targets(j);
      if (dst == null && src == null) {
         super.assem = "test null, null";
      } else if (src == null) {
         super.assem = "test null,  %`d0";
      } else if (dst == null) {
         super.assem = "test %`s0,  null";
      } else {
         super.assem = "test %`s0,  %`d0";
      }
   }

   @Override
   public TempList use() {
      return this.src;
   }

   @Override
   public TempList def() {
      return this.dst;
   }

   @Override
   public Targets jumps() {
      return this.jump;
   }

   @Override
   public String toString() {
      return "test %`s,  %`d";
   }
}