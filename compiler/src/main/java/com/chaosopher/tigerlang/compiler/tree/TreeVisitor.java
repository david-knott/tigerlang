package com.chaosopher.tigerlang.compiler.tree;

public interface TreeVisitor {

    public void visit(BINOP op);

    public void visit(CALL op);

    public void visit(CONST op);

    public void visit(ESEQ op);

    public void visit(EXPS op);

    public void visit(JUMP op);

    public void visit(LABEL op);

    public void visit(MEM op);

    public void visit(MOVE op);

    public void visit(NAME op);

    public void visit(SEQ op);

    public void visit(TEMP op);

    public void visit(CJUMP cjump);
    
    public void visit(StmList stmList);
}
