package com.chaosopher.tigerlang.compiler.canon;

import com.chaosopher.tigerlang.compiler.translate.DataFrag;
import com.chaosopher.tigerlang.compiler.translate.FragmentVisitor;
import com.chaosopher.tigerlang.compiler.translate.ProcFrag;
import com.chaosopher.tigerlang.compiler.tree.TreeVisitor;

class FragPrettyPrinter implements FragmentVisitor {
    final TreeVisitor treeVisitor;

    public FragPrettyPrinter(TreeVisitor treeVisitor) {
        this.treeVisitor = treeVisitor;
    }

    @Override
    public void visit(ProcFrag procFrag) {
        procFrag.body.accept(this.treeVisitor);
    }

    @Override
    public void visit(DataFrag dataFrag) {
        //do nothing.
    }

}