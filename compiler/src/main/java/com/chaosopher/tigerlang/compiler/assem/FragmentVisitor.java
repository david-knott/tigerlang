package com.chaosopher.tigerlang.compiler.assem;

public interface FragmentVisitor {

    void visit(ProcFrag procFrag);

    void visit(DataFrag dataFrag);

}