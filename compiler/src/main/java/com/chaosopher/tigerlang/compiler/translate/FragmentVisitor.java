package com.chaosopher.tigerlang.compiler.translate;

public interface FragmentVisitor {

    public void visit(ProcFrag procFrag);

    public void visit(DataFrag dataFrag);
}