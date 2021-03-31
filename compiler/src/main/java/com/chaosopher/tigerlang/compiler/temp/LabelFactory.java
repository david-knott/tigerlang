package com.chaosopher.tigerlang.compiler.temp;

import java.util.Hashtable;

import com.chaosopher.tigerlang.compiler.symbol.Symbol;


public class LabelFactory {

   private static int count;

    /**
    * Cache of previous created labels. This ensures
    * we dont have different labels with the same name.
    */
   private static Hashtable<String, Label> labels = new Hashtable<String, Label>();

   /**
    * Creates a new label and adds it
    * to an internal cache. The labels
    * name will be off the form 'L[number]'
    * @return a Label.
    */
   public Label create() {
      return create("L" + count++);
   }

   /**
    * Creates a new label or returns a
    * an existing one with the supplied symbol.
    * @param symbol the symbol to use with this label.
    * @return a Label.
    */
   public Label create(Symbol symbol) {
      return create(symbol.toString());
   }

   /**
    * Creates a new label or returns an already
    * create one with the supplied name string.
    * @param string the name of the label.
    * @return a Label.
    */
   public Label create(String string) {
      if(labels.containsKey(string)){
         return labels.get(string);
      }
      Label label = new Label(string);
      labels.put(string, label);
      return label;
   }
}