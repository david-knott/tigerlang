package com.chaosopher.tigerlang.compiler.tree;

public class JUMP extends Stm {
  public Exp exp;
  public Temp.LabelList targets;

  public JUMP(Exp e, Temp.LabelList t) {
    exp = e;
    targets = t;
  }

  public JUMP(Temp.Label target) {
    this(new NAME(target), new Temp.LabelList(target, null));
  }

  public ExpList kids() {
    return new ExpList(exp, null);
  }

  public Stm build(ExpList kids) {
    return new JUMP(kids.head, targets);
  }

  @Override
  public void accept(TreeVisitor treeVisitor) {
    treeVisitor.visit(this);
  }

  @Override
  public int getOperator() {
    return TreeKind.JUMP;
  }

  @Override
  public int getArity() {
    return 1;
  }

  @Override
  public IR getNthChild(int index) {
    return this.exp;
  }
}
