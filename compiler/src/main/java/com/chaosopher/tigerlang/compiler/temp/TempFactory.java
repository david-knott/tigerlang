package com.chaosopher.tigerlang.compiler.temp;

import java.util.Hashtable;

import com.chaosopher.tigerlang.compiler.symbol.Symbol;

class TempFactory {

   private static int count;

    /**
    * Cache of previous created labels. This ensures
    * we dont have different labels with the same name.
    */
   private static Hashtable<String, Temp> labels = new Hashtable<>();

   /**
    * Creates a new Temp and adds it
    * to an internal cache. The labels
    * name will be off the form 'L[number]'
    * @return a Temp.
    */
   public Temp create() {
      return create("L" + count++);
   }

   /**
    * Creates a new Temp or returns a
    * an existing one with the supplied symbol.
    * @param symbol the symbol to use with this Temp.
    * @return a Temp.
    */
   public Temp create(Symbol symbol) {
      return create(symbol.toString());
   }

   /**
    * Creates a new Temp or returns an already
    * create one with the supplied name string.
    * @param string the name of the Temp.
    * @return a Temp.
    */
   public Temp create(String string) {
      if(labels.containsKey(string)){
         return labels.get(string);
      }
      Temp label = new Temp(string);
      labels.put(string, label);
      return label;
   }
}