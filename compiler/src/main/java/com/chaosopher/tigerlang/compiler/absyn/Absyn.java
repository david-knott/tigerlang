package com.chaosopher.tigerlang.compiler.absyn;


abstract public class Absyn {
  public int pos;

  public abstract void accept(AbsynVisitor visitor);

}
