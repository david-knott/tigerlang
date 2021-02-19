package com.chaosopher.tigerlang.compiler.dataflow;

import com.chaosopher.tigerlang.compiler.translate.DataFrag;
import com.chaosopher.tigerlang.compiler.translate.FragList;
import com.chaosopher.tigerlang.compiler.translate.FragmentVisitor;
import com.chaosopher.tigerlang.compiler.translate.ProcFrag;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;

class ConstantPropagationFragmentOptimizer implements FragmentVisitor {

    public FragList fragList = null;

    public ConstantPropagationFragmentOptimizer() {
    }

    @Override
    public void visit(ProcFrag procFrag) {
        ConstPropagation constPropagation = new ConstPropagation(new GenKillSets(new CFG((StmList)procFrag.body)));
        procFrag.body.accept(constPropagation);
        Stm optimized = constPropagation.getStmList();
        ProcFrag lirProcFrag = new ProcFrag(optimized, procFrag.frame);
        this.fragList = new FragList(lirProcFrag, this.fragList);
    }

    @Override
    public void visit(DataFrag dataFrag) {
        this.fragList = new FragList(dataFrag, this.fragList);
    }
}