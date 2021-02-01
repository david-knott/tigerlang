package com.chaosopher.tigerlang.compiler.dataflow;

import java.util.Hashtable;

import com.chaosopher.tigerlang.compiler.canon.Canonicalization;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.BINOP;
import com.chaosopher.tigerlang.compiler.tree.CALL;
import com.chaosopher.tigerlang.compiler.tree.CJUMP;
import com.chaosopher.tigerlang.compiler.tree.ESEQ;
import com.chaosopher.tigerlang.compiler.tree.EXP;
import com.chaosopher.tigerlang.compiler.tree.Exp;
import com.chaosopher.tigerlang.compiler.tree.ExpList;
import com.chaosopher.tigerlang.compiler.tree.JUMP;
import com.chaosopher.tigerlang.compiler.tree.MEM;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.tree.TEMP;


/**
 * Creates an atomized tree. This transforms the HIR into
 * a form where there are no nested expressions. After canonicalisation
 * this is similar to a quadruple set, which can be fed into data flow
 * analyses.
 */
public class TreeAtomizer extends CloningTreeVisitor {

    private final Canonicalization canonicalization;
    private final Hashtable<Temp, Exp> temps = new Hashtable<>();

    public TreeAtomizer(Canonicalization canonicalization) {
        this.canonicalization = canonicalization;
    }

    public StmList getCanonicalisedAtoms() {
        return this.canonicalization.canon(this.stm != null ? this.stm : new EXP(this.exp));
    }

    public Hashtable<Temp, Exp> getTemps() {
        return this.temps;
    }

    public Stm getAtoms() {
        return this.stm != null ? this.stm : new EXP(this.exp);
    }

    private Temp createTemp(Exp exp) {
        Temp temp = Temp.create();
        this.temps.put(temp, exp);
        return temp;
    }

    public boolean contains(Temp temp) {
        return this.temps.containsKey(temp);
    }

    /**
     * This method takes expression exp and converts it into a new expression
     * that evaluetes it and places its result into a new temp, which is returned
     * by an ESEQ. This only operates on MEM and BINOP IR.
     * @param exp the expression to be rewritten.
     * @return a new expression
     */
    private Exp rewrite(Exp exp) {
        if(exp instanceof MEM || exp instanceof BINOP) {
            Temp temp = this.createTemp(exp);
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
    public void visit(JUMP op) {
        op.exp.accept(this);
        Exp eClone = rewrite(this.exp);
        this.stm = new JUMP(eClone, op.targets);
    }

    @Override
    public void visit(MEM op) {
        op.exp.accept(this);
        Exp expClone = rewrite(this.exp);
        this.exp = new MEM(expClone);
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
    public void visit(CALL op) {
        op.func.accept(this);
        Exp funcClone = this.exp;
        ExpList cloneExpList = null;
        for (ExpList arg = op.args; arg != null; arg = arg.tail) {
            arg.head.accept(this);
            Exp cloneArg = rewrite(this.exp);
            cloneExpList = ExpList.append(cloneExpList, cloneArg);
        }
        this.exp = new CALL(funcClone, cloneExpList);
    }
}