package com.chaosopher.tigerlang.compiler.dataflow;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import com.chaosopher.tigerlang.compiler.canon.Canonicalization;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.translate.DataFrag;
import com.chaosopher.tigerlang.compiler.translate.FragList;
import com.chaosopher.tigerlang.compiler.translate.FragmentVisitor;
import com.chaosopher.tigerlang.compiler.translate.ProcFrag;
import com.chaosopher.tigerlang.compiler.tree.BINOP;
import com.chaosopher.tigerlang.compiler.tree.CALL;
import com.chaosopher.tigerlang.compiler.tree.CJUMP;
import com.chaosopher.tigerlang.compiler.tree.CloningTreeVisitor;
import com.chaosopher.tigerlang.compiler.tree.ESEQ;
import com.chaosopher.tigerlang.compiler.tree.EXPS;
import com.chaosopher.tigerlang.compiler.tree.Exp;
import com.chaosopher.tigerlang.compiler.tree.ExpList;
import com.chaosopher.tigerlang.compiler.tree.IR;
import com.chaosopher.tigerlang.compiler.tree.JUMP;
import com.chaosopher.tigerlang.compiler.tree.MEM;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.tree.TEMP;

/**
 * Creates an atomized tree. This transforms the HIR into
 * a form where there are no nested expressions. After canonicalisation
 * this is a quadruple set, which can be fed into data flow
 * analyses.
 */
public class TreeAtomizer extends CloningTreeVisitor implements FragmentVisitor {

    public static TreeAtomizer apply(Canonicalization canonicalization, FragList fragList) {
        TreeAtomizer atomizer = new TreeAtomizer(canonicalization);
        fragList.accept(atomizer);
        return atomizer;
    }

    public static TreeAtomizer apply(Canonicalization canonicalization, IR ir) {
        TreeAtomizer atomizer = new TreeAtomizer(canonicalization);
        ir.accept(atomizer);
        return atomizer;
    }

    private FragList fragList = null;
    private final Canonicalization canonicalization;
    private final Hashtable<Temp, Exp> temps = new Hashtable<>();

    public FragList getAtomizedFragList() {
        return this.fragList;
    }

    private TreeAtomizer(Canonicalization canonicalization) {
        this.canonicalization = canonicalization;
    }

    public StmList getCanonicalisedAtoms() {
        return this.canonicalization.canon(this.getStm() != null ? this.getStm() : new EXPS(this.getExp() ));
    }

    public Hashtable<Temp, Exp> getTemps() {
        return this.temps;
    }

    public Stm getAtoms() {
        return this.getStm() != null ? this.getStm() : new EXPS(this.getExp() );
    }

    private Temp createTemp(Exp exp) {
        Temp temp = Temp.create();
        this.temps.put(temp, exp);
        return temp;
    }

    public boolean contains(Temp temp) {
        return this.temps.containsKey(temp);
    }

    private final Set<Temp> newTemps = new HashSet<>();

    public Set<Temp> getNewTemps() {
        return this.newTemps;
    }

    /**
     * This method takes expression exp and converts it into a new expression
     * that evaluetes it and places its result into a new temp, which is returned
     * by an ESEQ.
     * @param exp the expression to be rewritten.
     * @return a new expression
     */
    private Exp rewrite(Exp exp) {
        if(exp instanceof MEM || exp instanceof BINOP) {
            Temp temp = this.createTemp(exp);
            newTemps.add(temp);
            return new ESEQ(new MOVE(new TEMP(temp), exp), new TEMP(temp));
        }
        return exp; 
     }

    @Override
    public void visit(BINOP op) {
        op.left.accept(this);
        Exp leftClone = rewrite(this.getExp() );
        op.right.accept(this);
        Exp rightClone = rewrite(this.getExp() );
        this.setExp(new BINOP(op.binop, leftClone, rightClone));
    }

    @Override
    public void visit(JUMP op) {
        op.exp.accept(this);
        Exp eClone = rewrite(this.getExp() );
        this.setStm(new JUMP(eClone, op.targets));
    }

    @Override
    public void visit(MEM op) {
        op.exp.accept(this);
        Exp expClone = rewrite(this.getExp() );
        this.setExp(new MEM(expClone));
    }

    @Override
    public void visit(CJUMP cjump) {
        cjump.left.accept(this);
        Exp leftClone = rewrite(this.getExp() );
        cjump.right.accept(this);
        Exp rightClone = rewrite(this.getExp() );
        this.setStm(new CJUMP(cjump.relop, leftClone, rightClone, cjump.iftrue, cjump.iffalse));
    }

    @Override
    public void visit(CALL op) {
        op.func.accept(this);
        Exp funcClone = this.getExp() ;
        ExpList cloneExpList = null;
        for (ExpList arg = op.args; arg != null; arg = arg.tail) {
            arg.head.accept(this);
            Exp cloneArg = rewrite(this.getExp() );
            cloneExpList = ExpList.append(cloneExpList, cloneArg);
        }
        this.setExp(new CALL(funcClone, cloneExpList));
    }

    @Override
    public void visit(ProcFrag procFrag) {
        procFrag.body.accept(this);
        Stm atomized = this.getCanonicalisedAtoms();
        ProcFrag lirProcFrag = new ProcFrag(atomized, procFrag.frame);
        this.fragList = new FragList(lirProcFrag, this.fragList);
    }

    @Override
    public void visit(DataFrag dataFrag) {
        this.fragList = new FragList(dataFrag, this.fragList);
    }
}