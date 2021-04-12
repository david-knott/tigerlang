package com.chaosopher.tigerlang.compiler.tree;

public class CloningTreeVisitor extends DefaultTreeVisitor {

    protected Exp exp;
    public Stm stm;
    protected StmList stmList;

    public StmList getStmList() {
        return stmList;
    }

    @Override
    public void visit(BINOP op) {
        op.left.accept(this);
        Exp leftClone = this.exp;
        op.right.accept(this);
        Exp rightClone = this.exp;
        this.exp = new BINOP(op.binop, leftClone, rightClone);
    }

    @Override
    public void visit(CALL op) {
        op.func.accept(this);
        Exp funcClone = this.exp;
        ExpList cloneExpList = null;
        for(ExpList arg = op.args; arg != null; arg = arg.tail) {
            arg.head.accept(this);
            Exp cloneArg = this.exp;
            cloneExpList = ExpList.append(cloneExpList, cloneArg);
        }
        this.exp = new CALL(funcClone, cloneExpList);
    }

    @Override
    public void visit(CONST op) {
        this.exp = new CONST(op.value);
    }

    @Override
    public void visit(ESEQ op) {
        op.stm.accept(this);
        Stm clonedStm = this.stm;
        op.exp.accept(this);
        Exp clonedExp = this.exp;
        this.exp = new ESEQ(clonedStm, clonedExp);
    }

    @Override
    public void visit(EXP op) {
        op.exp.accept(this);
        Exp eClone = this.exp;
        this.stm = new EXP(eClone);
    }

    @Override
    public void visit(JUMP op) {
        op.exp.accept(this);
        Exp eClone = this.exp;
        this.stm = new JUMP(eClone, op.targets);
    }

    @Override
    public void visit(LABEL op) {
        this.stm = new LABEL(op.label);

    }

    @Override
    public void visit(MEM op) {
        op.exp.accept(this);
        Exp expClone = this.exp;
        this.exp = new MEM(expClone);
    }

    @Override
    public void visit(MOVE op) {
        op.dst.accept(this);
        Exp dClone = this.exp;
        op.src.accept(this);
        Exp sClone = this.exp;
        this.stm = new MOVE(dClone, sClone);
    }

    @Override
    public void visit(NAME op) {
        this.exp = new NAME(op.label);
    }

    @Override
    public void visit(SEQ op) {
        op.left.accept(this);
        Stm leftClone = this.stm;
        op.right.accept(this);
        Stm rightClone = this.stm;
        this.stm = new SEQ(leftClone, rightClone);
    }

    @Override
    public void visit(TEMP op) {
        this.exp = new TEMP(op.temp);
    }

    @Override
    public void visit(CJUMP cjump) {
        cjump.left.accept(this);
        Exp leftClone = this.exp;
        cjump.right.accept(this);
        Exp rightClone = this.exp;
        this.stm = new CJUMP(cjump.relop, leftClone, rightClone, cjump.iftrue, cjump.iffalse);
    }

    @Override
    public void visit(StmList stmList) {
        StmList cloned = null;

        for(;stmList != null; stmList = stmList.tail) {
            stmList.head.accept(this);
            Stm clonedHead = this.stm;
            cloned = StmList.append(cloned, clonedHead);
        }
        this.stmList = cloned;
        
        /*
        DK: Commented out as I dont think this should be present. Its basically
        doing the same thing as the above code block.
        stmList.head.accept(this);
        Stm clonedHead = this.stm;
        this.stmList = new StmList(clonedHead);

        if(stmList.tail != null) {
            stmList.tail.accept(this);
            this.stmList = new StmList(clonedHead, this.stmList);
        }*/
    }
}