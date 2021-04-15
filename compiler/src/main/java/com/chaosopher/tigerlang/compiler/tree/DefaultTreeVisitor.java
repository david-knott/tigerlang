package com.chaosopher.tigerlang.compiler.tree;

public class DefaultTreeVisitor implements TreeVisitor {

    @Override
    public void visit(BINOP op) {
        op.left.accept(this);
        op.right.accept(this);
    }

    @Override
    public void visit(CALL op) {
        op.func.accept(this);
     //   op.args.head.accept(this);
        for(ExpList expList = op.args; expList != null; expList = expList.tail) {
            expList.head.accept(this);
        }
       // op.args.head.accept(this);
    }

    @Override
    public void visit(CONST op) {
        // do nothing.
    }

    @Override
    public void visit(ESEQ op) {
        op.stm.accept(this);
        op.exp.accept(this);
    }

    @Override
    public void visit(EXP op) {
        op.exp.accept(this);
    }

    @Override
    public void visit(JUMP op) {
        op.exp.accept(this);
    }

    @Override
    public void visit(LABEL op) {
        // do nothing.
    }

    @Override
    public void visit(MEM op) {
        op.exp.accept(this);
    }

    @Override
    public void visit(MOVE op) {
        op.src.accept(this);
        op.dst.accept(this);
    }

    @Override
    public void visit(NAME op) {
        // do nothing.
    }

    @Override
    public void visit(SEQ op) {
        op.left.accept(this);
        op.right.accept(this);
    }

    @Override
    public void visit(TEMP op) {
        // do nothing.
    }

    @Override
    public void visit(CJUMP cjump) {
        cjump.left.accept(this);
        cjump.right.accept(this);
    }

    @Override
    public void visit(StmList stmList) {
        stmList.head.accept(this);
        if(stmList.tail != null) {
            stmList.tail.accept(this);
        }
    }
}