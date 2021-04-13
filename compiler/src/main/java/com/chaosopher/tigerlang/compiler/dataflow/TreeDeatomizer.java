package com.chaosopher.tigerlang.compiler.dataflow;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import com.chaosopher.tigerlang.compiler.canon.StmListList;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.dataflow.def.DefGenKillSets;
import com.chaosopher.tigerlang.compiler.dataflow.def.RDDataFlow;
import com.chaosopher.tigerlang.compiler.dataflow.exp.AEDataFlow;
import com.chaosopher.tigerlang.compiler.dataflow.exp.AEGenKillSets;
import com.chaosopher.tigerlang.compiler.translate.DataFrag;
import com.chaosopher.tigerlang.compiler.translate.FragList;
import com.chaosopher.tigerlang.compiler.translate.FragmentVisitor;
import com.chaosopher.tigerlang.compiler.translate.ProcFrag;
import com.chaosopher.tigerlang.compiler.tree.BINOP;
import com.chaosopher.tigerlang.compiler.tree.CJUMP;
import com.chaosopher.tigerlang.compiler.tree.CloningTreeVisitor;
import com.chaosopher.tigerlang.compiler.tree.Exp;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.tree.TEMP;

/**
 * This class reassembles an canonicalized IR tree.
 */
public class TreeDeatomizer implements FragmentVisitor {

    public static final TreeDeatomizer apply(final FragList fragList) {
        TreeDeatomizer treeDeatomizer = new TreeDeatomizer();
        fragList.accept(treeDeatomizer);
        return treeDeatomizer;
    } 

    public FragList fragList = null;

    private TreeDeatomizer() {
        super();
    }

    @Override
    public void visit(ProcFrag procFrag) {
        CFG cfg = CFG.build((StmList)procFrag.body);
        GenKillSets<Integer> rdGenKillSets = DefGenKillSets.analyse(cfg);
        RDDataFlow rdDataFlow = RDDataFlow.analyze(cfg, rdGenKillSets);
        GenKillSets<Exp> aeGenKillSets = AEGenKillSets.analyse(cfg);
        AEDataFlow aeDataFlow = AEDataFlow.analyze(cfg, aeGenKillSets);
        TACloningTreeVisitor cloningTreeVisitor = new TACloningTreeVisitor(rdDataFlow, aeDataFlow);
        procFrag.body.accept(cloningTreeVisitor);
        Stm deatomized = cloningTreeVisitor.getStmList();
        ProcFrag lirProcFrag = new ProcFrag(deatomized, procFrag.frame);
        this.fragList = new FragList(lirProcFrag, this.fragList);
    }

    @Override
    public void visit(DataFrag dataFrag) {
        this.fragList = new FragList(dataFrag, this.fragList);
    }

    private class TACloningTreeVisitor extends CloningTreeVisitor {
       
        private class ReplacementItem {

            private final Exp exp;
            private final Integer defId;

            ReplacementItem(Exp exp, Integer defId) {
                this.exp = exp;
                this.defId = defId;
            }

            boolean defIdReachesStatement(Stm stm) {
                Set<Integer> reaching = rdDataFlow.getIn(stm);
                return reaching.contains(this.defId);
            }

            boolean expressionIsAvailable(Stm stm) {
                Set<Exp> available = aeDataFlow.getIn(stm);
                return available.contains(exp);
            }
        }

        private final Map<Exp, ReplacementItem> replacements = new Hashtable<>();
        private final RDDataFlow rdDataFlow;
        private final AEDataFlow aeDataFlow;
        private Stm currentStm;

        TACloningTreeVisitor(RDDataFlow dataFlow, AEDataFlow aeDataFlow) {
            this.rdDataFlow = dataFlow;
            this.aeDataFlow = aeDataFlow;
        }

        private Stm getCurrentStm() {
            return currentStm;
        }

        private void setCurrentStm(Stm stm) {
            this.currentStm = stm;
        }

        @Override
        public void visit(MOVE op) {
            this.setCurrentStm(op);
            if(op.dst instanceof TEMP) {
                // get the definition id of this statement.
                Integer defId = this.rdDataFlow.getDefinitionId(op);
                replacements.put(op.dst, new ReplacementItem(op.src, defId));
            }
            // call super class to clone the move.
            super.visit(op);
            this.setCurrentStm(null);
        }

        @Override
        public void visit(CJUMP op) {
            this.setCurrentStm(op);
            Exp left = op.left;
            left = this.replace(left);
            Exp right = op.right;
            right = this.replace(right);

            left.accept(this);
            Exp clonedLeft = this.exp;
            right.accept(this);
            Exp cloneRight = this.exp;
            this.stm = new CJUMP(op.relop, clonedLeft, cloneRight, op.iffalse, op.iftrue);
            this.setCurrentStm(null);
        }

        private Exp replace(Exp re) {
            // get statement this expression is part of
            Stm stm = this.getCurrentStm();
            // parameter exp which is a temp, 
            if(this.replacements.containsKey(re)) {
                ReplacementItem replacementItem = this.replacements.get(re);
                // check if that temps definition id has reached the current statement,
                // check if the expression is available at this point.
                // if both of these criteria are met, we can rewrite
                if(replacementItem.defIdReachesStatement(stm) 
                 && replacementItem.expressionIsAvailable(stm)) {
                   // replacementItem.exp.accept(this);
                    re = replacementItem.exp;
                 }
            }
            return re;
        }

        @Override
        public void visit(BINOP op) {
            Exp left = op.left;
            left = this.replace(left);
            Exp right = op.right;
            right = this.replace(right);

            left.accept(this);
            Exp clonedLeft = this.exp;
            right.accept(this);
            Exp cloneRight = this.exp;
            this.exp = new BINOP(op.binop, clonedLeft, cloneRight);
        }
    }
}