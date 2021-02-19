package com.chaosopher.tigerlang.compiler.dataflow;

import com.chaosopher.tigerlang.compiler.translate.DataFrag;
import com.chaosopher.tigerlang.compiler.translate.FragList;
import com.chaosopher.tigerlang.compiler.translate.FragmentVisitor;
import com.chaosopher.tigerlang.compiler.translate.ProcFrag;
import com.chaosopher.tigerlang.compiler.tree.Stm;

class FragmentTreeDeatomizer implements FragmentVisitor {
    private final TreeDeatomizer treeDeatomizer;
    public FragList fragList = null;

    public FragmentTreeDeatomizer(TreeDeatomizer treeDeatomizer) {
        this.treeDeatomizer = treeDeatomizer;
    }

	@Override
    public void visit(ProcFrag procFrag) {
        procFrag.body.accept(treeDeatomizer);
        Stm deatomized = treeDeatomizer.stm;
        ProcFrag lirProcFrag = new ProcFrag(deatomized, procFrag.frame);
        this.fragList = new FragList(lirProcFrag, this.fragList);
    }

    @Override
    public void visit(DataFrag dataFrag) {
        this.fragList = new FragList(dataFrag, this.fragList);
    }
}