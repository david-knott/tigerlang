package com.chaosopher.tigerlang.compiler.absyn;

import com.chaosopher.tigerlang.compiler.symbol.Symbol;
import com.chaosopher.tigerlang.compiler.types.Type;

/**
 * This class defines the type of an array. The typ field represents the 
 * right value, eg the int in 'type intArray = array of int'. The intArray
 * is captured in the TypeDec. 
 */
public class ArrayTy extends Ty {
   public NameTy typ;

   public ArrayTy(int p, NameTy t) {
      pos = p;
      typ = t;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }
}