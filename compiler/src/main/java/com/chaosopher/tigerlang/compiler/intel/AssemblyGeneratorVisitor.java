package com.chaosopher.tigerlang.compiler.intel;

import com.chaosopher.tigerlang.compiler.assem.FragList;
import com.chaosopher.tigerlang.compiler.translate.DataFrag;
import com.chaosopher.tigerlang.compiler.translate.FragmentVisitor;
import com.chaosopher.tigerlang.compiler.translate.ProcFrag;
import com.chaosopher.tigerlang.compiler.tree.StmList;

/**
 * 
 */
class AssemblyGeneratorVisitor implements FragmentVisitor {

    final CodeGen codeGen;
    FragList fragList;
    Emitter emitter;

    public AssemblyGeneratorVisitor(CodeGen codeGen) {
        this.codeGen = codeGen;
    }

    @Override
    public void visit(ProcFrag procFrag) {
        var reducer = new Reducer(null); 
        codeGen.setReducer(reducer);
        // yuck, this smells.
        StmList stmList = (StmList)procFrag.body;
        for(; stmList != null; stmList = stmList.tail) {
            try {
                this.codeGen.burm(stmList.head);
            } catch (Exception e) {
                throw new Error(e);
            }
        }
        this.fragList = new FragList(new com.chaosopher.tigerlang.compiler.assem.ProcFrag(reducer.iList, procFrag.frame), this.fragList);
    }

    @Override
    public void visit(DataFrag dataFrag) {
        this.fragList = new FragList(new com.chaosopher.tigerlang.compiler.assem.DataFrag(dataFrag.toString()), this.fragList);
    }

    public FragList getAssemFragList() {
      //  return FragList.reverse(this.fragList);
      return this.fragList;
    }
}