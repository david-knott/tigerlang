package com.chaosopher.tigerlang.compiler.util;

public class BoolList {
  public boolean head;
  public BoolList tail;

  public BoolList(boolean h, BoolList t) {
    head = h;
    tail = t;
  }

  public BoolList append(boolean h) {
    var last = this;
    while (last.tail != null) {
      last = last.tail;
    }
    last.tail = new BoolList(h, null);
    return last.tail;
  }

  public BoolList last() {
    BoolList result = this;
    for (; result.tail != null; result = result.tail)
      ;
    return result;
  }
}