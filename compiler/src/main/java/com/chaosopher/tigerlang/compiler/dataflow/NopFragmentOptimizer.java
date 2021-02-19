package com.chaosopher.tigerlang.compiler.dataflow;

import com.chaosopher.tigerlang.compiler.translate.DataFrag;
import com.chaosopher.tigerlang.compiler.translate.FragmentVisitor;
import com.chaosopher.tigerlang.compiler.translate.ProcFrag;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.translate.FragList;


class NopFragmentOptimizer implements FragmentVisitor {

    private final CloningTreeVisitor cloningTreeVisitor;
    public FragList fragList = null;

    public NopFragmentOptimizer(CloningTreeVisitor cloningTreeVisitor) {
        this.cloningTreeVisitor = cloningTreeVisitor;
    }

    @Override
    public void visit(ProcFrag procFrag) {
        procFrag.body.accept(cloningTreeVisitor);
        Stm deatomized = cloningTreeVisitor.stm;
        ProcFrag lirProcFrag = new ProcFrag(deatomized, procFrag.frame);
        this.fragList = new FragList(lirProcFrag, this.fragList);
    }

    @Override
    public void visit(DataFrag dataFrag) {
        this.fragList = new FragList(dataFrag, this.fragList);
    }
}