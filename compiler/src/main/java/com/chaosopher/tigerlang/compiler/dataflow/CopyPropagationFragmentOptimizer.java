package com.chaosopher.tigerlang.compiler.dataflow;

import com.chaosopher.tigerlang.compiler.translate.DataFrag;
import com.chaosopher.tigerlang.compiler.translate.FragList;
import com.chaosopher.tigerlang.compiler.translate.FragmentVisitor;
import com.chaosopher.tigerlang.compiler.translate.ProcFrag;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;

class CopyPropagationFragmentOptimizer implements FragmentVisitor {
    public FragList fragList = null;

    @Override
    public void visit(ProcFrag procFrag) {
        CopyPropagation copyPropagation = new CopyPropagation(new GenKillSets(new CFG((StmList)procFrag.body)));
        procFrag.body.accept(copyPropagation);
        Stm optimized = copyPropagation.getStmList();
        ProcFrag lirProcFrag = new ProcFrag(optimized, procFrag.frame);
        this.fragList = new FragList(lirProcFrag, this.fragList);
    }

    @Override
    public void visit(DataFrag dataFrag) {
        this.fragList = new FragList(dataFrag, this.fragList);
    }
}