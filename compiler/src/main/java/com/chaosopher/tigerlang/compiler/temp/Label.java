package com.chaosopher.tigerlang.compiler.temp;

import com.chaosopher.tigerlang.compiler.symbol.Symbol;


/**
 * A Label represents an address in assembly language.
 */

public class Label {

   private String name;

   /**
    * a printable representation of the label, for use in assembly language output.
    */
   public String toString() {
      return name;
   }

   /**
    * Makes a new label that prints as "name". Warning: avoid repeated calls to
    * <tt>new Label(s)</tt> with the same name <tt>s</tt>.
    */
   Label(String n) {
      name = n;
   }

   /**
    * Makes a new label with an arbitrary name.
    */
   Label(int count) {
      name = "L" + count;
   }

   /**
    * Makes a new label whose name is the same as a symbol.
    */
   Label(Symbol s) {
      this(s.toString());
   }
}
