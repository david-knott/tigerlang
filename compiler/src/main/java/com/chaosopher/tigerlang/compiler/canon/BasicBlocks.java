package com.chaosopher.tigerlang.compiler.canon;

class BasicBlocks {
  public StmListList blocks;
  public com.chaosopher.tigerlang.compiler.temp.Label done;

  private StmListList lastBlock;
  private com.chaosopher.tigerlang.compiler.tree.StmList lastStm;

  private void addStm(com.chaosopher.tigerlang.compiler.tree.Stm s) {
    lastStm = lastStm.tail = new com.chaosopher.tigerlang.compiler.tree.StmList(s, null);
  }

  private void doStms(com.chaosopher.tigerlang.compiler.tree.StmList l) {
    if (l == null)
      doStms(new com.chaosopher.tigerlang.compiler.tree.StmList(new com.chaosopher.tigerlang.compiler.tree.JUMP(done), null));
    else if (l.head instanceof com.chaosopher.tigerlang.compiler.tree.JUMP || l.head instanceof com.chaosopher.tigerlang.compiler.tree.CJUMP) {
      addStm(l.head);
      mkBlocks(l.tail);
    } else if (l.head instanceof com.chaosopher.tigerlang.compiler.tree.LABEL)
      doStms(new com.chaosopher.tigerlang.compiler.tree.StmList(new com.chaosopher.tigerlang.compiler.tree.JUMP(((com.chaosopher.tigerlang.compiler.tree.LABEL) l.head).label), l));
    else {
      addStm(l.head);
      doStms(l.tail);
    }
  }

  void mkBlocks(com.chaosopher.tigerlang.compiler.tree.StmList l) {
    if (l == null)
      return;
    else if (l.head instanceof com.chaosopher.tigerlang.compiler.tree.LABEL) {
      lastStm = new com.chaosopher.tigerlang.compiler.tree.StmList(l.head, null);
      if (lastBlock == null)
        lastBlock = blocks = new StmListList(lastStm, null);
      else
        lastBlock = lastBlock.tail = new StmListList(lastStm, null);
      doStms(l.tail);
    } else
      mkBlocks(new com.chaosopher.tigerlang.compiler.tree.StmList(new com.chaosopher.tigerlang.compiler.tree.LABEL(new com.chaosopher.tigerlang.compiler.temp.Label()), l));
  }

  public BasicBlocks(com.chaosopher.tigerlang.compiler.tree.StmList stms) {
    done = new com.chaosopher.tigerlang.compiler.temp.Label();
    mkBlocks(stms);
  }
}
