package com.chaosopher.tigerlang.compiler.canon;

import com.chaosopher.tigerlang.compiler.translate.DataFrag;
import com.chaosopher.tigerlang.compiler.translate.FragList;
import com.chaosopher.tigerlang.compiler.translate.FragmentVisitor;
import com.chaosopher.tigerlang.compiler.translate.ProcFrag;
import com.chaosopher.tigerlang.compiler.tree.StmList;


/**
 * Produces a new FragList that contains the HIR converted
 * to LIR trees. We reuse the Fragment data structure as it
 * ties a particular fragment to its activation record.
 */
public class CanonVisitor implements FragmentVisitor {

    final Canonicalization canonicalization;
    FragList fragList = null;

    public CanonVisitor(Canonicalization canonicalization) {
        this.canonicalization = canonicalization;
	}

	@Override
    public void visit(ProcFrag procFrag) {
        StmList stmList = canonicalization.canon(procFrag.body);
        ProcFrag lirProcFrag = new ProcFrag(stmList, procFrag.frame);
        this.fragList = new FragList(lirProcFrag, this.fragList);
    }

    @Override
    public void visit(DataFrag dataFrag) {
        this.fragList = new FragList(dataFrag, this.fragList);
    }
}