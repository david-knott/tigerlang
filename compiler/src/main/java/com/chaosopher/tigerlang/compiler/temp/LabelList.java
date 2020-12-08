package com.chaosopher.tigerlang.compiler.temp;

public class LabelList {
   public Label head;
   public LabelList tail;

   public LabelList(Label h, LabelList t) {
      if(h == null)
      throw new Error("h cannot be null");
      head = h;
      tail = t;
   }
}
