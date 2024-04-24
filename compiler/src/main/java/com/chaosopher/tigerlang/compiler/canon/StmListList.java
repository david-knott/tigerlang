package com.chaosopher.tigerlang.compiler.canon;

import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.tree.TreeVisitor;

public class StmListList {
    public com.chaosopher.tigerlang.compiler.tree.StmList head;
    public StmListList tail;

    public StmListList(com.chaosopher.tigerlang.compiler.tree.StmList h, StmListList t) {
        head = h;
        tail = t;
    }

    public void accept(TreeVisitor treeVisitor) {
        for (StmListList stmListList = this; stmListList != null; stmListList = stmListList.tail) {
            for (StmList stmList = stmListList.head; stmList != null; stmList = stmList.tail) {
                stmList.head.accept(treeVisitor);
            }
        }
    }
}
