package com.chaosopher.tigerlang.compiler.dataflow;

import com.chaosopher.tigerlang.compiler.translate.DataFrag;
import com.chaosopher.tigerlang.compiler.translate.FragList;
import com.chaosopher.tigerlang.compiler.translate.FragmentVisitor;
import com.chaosopher.tigerlang.compiler.translate.ProcFrag;
import com.chaosopher.tigerlang.compiler.tree.Stm;


class FragmentTreeAtomizer implements FragmentVisitor {

    private final TreeAtomizer treeAtomizer;
    public FragList fragList = null;
    
    FragmentTreeAtomizer(TreeAtomizer deatomizer) {
        this.treeAtomizer = deatomizer;
    }

	@Override
    public void visit(ProcFrag procFrag) {
        procFrag.body.accept(this.treeAtomizer);
        Stm atomized = this.treeAtomizer.getCanonicalisedAtoms();
        ProcFrag lirProcFrag = new ProcFrag(atomized, procFrag.frame);
        this.fragList = new FragList(lirProcFrag, this.fragList);
    }

    @Override
    public void visit(DataFrag dataFrag) {
        this.fragList = new FragList(dataFrag, this.fragList);
    }
}