package com.chaosopher.tigerlang.compiler.tree;

public class JUMP extends Stm {
  public Exp exp;
  public com.chaosopher.tigerlang.compiler.temp.LabelList targets;

  public JUMP(Exp e, com.chaosopher.tigerlang.compiler.temp.LabelList t) {
    exp = e;
    targets = t;
  }

  public JUMP(com.chaosopher.tigerlang.compiler.temp.Label target) {
    this(new NAME(target), new com.chaosopher.tigerlang.compiler.temp.LabelList(target, null));
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

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof JUMP) {
            return
            ((JUMP)obj).exp.equals(this.exp)
            ;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + this.exp.hashCode();
        return result;
    }


}
