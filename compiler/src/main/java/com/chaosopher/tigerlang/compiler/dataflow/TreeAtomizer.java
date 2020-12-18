package com.chaosopher.tigerlang.compiler.dataflow;

import com.chaosopher.tigerlang.compiler.tree.MOVE;

import java.util.Hashtable;

import com.chaosopher.tigerlang.compiler.canon.Canonicalization;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.temp.TempList;
import com.chaosopher.tigerlang.compiler.tree.BINOP;
import com.chaosopher.tigerlang.compiler.tree.CALL;
import com.chaosopher.tigerlang.compiler.tree.CJUMP;
import com.chaosopher.tigerlang.compiler.tree.CONST;
import com.chaosopher.tigerlang.compiler.tree.ESEQ;
import com.chaosopher.tigerlang.compiler.tree.EXP;
import com.chaosopher.tigerlang.compiler.tree.Exp;
import com.chaosopher.tigerlang.compiler.tree.ExpList;
import com.chaosopher.tigerlang.compiler.tree.JUMP;
import com.chaosopher.tigerlang.compiler.tree.LABEL;
import com.chaosopher.tigerlang.compiler.tree.MEM;
import com.chaosopher.tigerlang.compiler.tree.NAME;
import com.chaosopher.tigerlang.compiler.tree.SEQ;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.tree.TEMP;
import com.chaosopher.tigerlang.compiler.tree.TreeVisitor;


/**
 * Creates an atomized tree. This transforms the HIR into
 * a form where there are no nested expressions. After canonicalisation
 * this is similar to a quadruple set, which can be fed into data flow
 * analyses.
 */
public class TreeAtomizer implements TreeVisitor {

    private Exp exp;
    private Stm stm;
    private final Canonicalization canonicalization;

    public TreeAtomizer(Canonicalization canonicalization) {
        this.canonicalization = canonicalization;
    }

    public StmList getCanonicalisedAtoms() {
        return this.canonicalization.canon(this.stm != null ? this.stm : new EXP(this.exp));
    }

    public Stm getAtoms() {
        return this.stm != null ? this.stm : new EXP(this.exp);
    }

    private Hashtable<Temp, Temp> temps = new Hashtable<>();
    private Temp createTemp() {
        Temp temp = Temp.create();
        this.temps.put(temp, temp);
        return temp;
    }

    public boolean contains(Temp temp) {
        return this.temps.containsKey(temp);
    }

    private Exp rewrite(Exp exp) {
        if(exp instanceof MEM || exp instanceof BINOP) {
            Temp temp = this.createTemp();
            return new ESEQ(new MOVE(new TEMP(temp), exp), new TEMP(temp));
        }
        return exp; 
     }

    @Override
    public void visit(BINOP op) {
        op.left.accept(this);
        Exp leftClone = rewrite(this.exp);
        op.right.accept(this);
        Exp rightClone = rewrite(this.exp);
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
        Exp eClone = rewrite(this.exp);
        this.stm = new JUMP(eClone, op.targets);
    }

    @Override
    public void visit(LABEL op) {
        this.stm = new LABEL(op.label);

    }

    @Override
    public void visit(MEM op) {
        op.exp.accept(this);
        Exp expClone = rewrite(this.exp);
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
        Exp leftClone = rewrite(this.exp);
        cjump.right.accept(this);
        Exp rightClone = rewrite(this.exp);
        this.stm = new CJUMP(cjump.relop, leftClone, rightClone, cjump.iftrue, cjump.iffalse);
    }

    @Override
    public void visit(StmList stmList) {
        for(;stmList != null; stmList = stmList.tail) {
            stmList.head.accept(this);
        }
    }
}