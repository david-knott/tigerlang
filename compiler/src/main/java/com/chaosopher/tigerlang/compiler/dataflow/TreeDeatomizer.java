package com.chaosopher.tigerlang.compiler.dataflow;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.dataflow.def.DefGenKillSets;
import com.chaosopher.tigerlang.compiler.dataflow.def.RDDataFlow;
import com.chaosopher.tigerlang.compiler.dataflow.exp.AEDataFlow;
import com.chaosopher.tigerlang.compiler.dataflow.exp.AEGenKillSets;
import com.chaosopher.tigerlang.compiler.dataflow.live.LiveGenKillSets;
import com.chaosopher.tigerlang.compiler.dataflow.live.Liveness;
import com.chaosopher.tigerlang.compiler.dataflow.utils.ExtractDefs;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.translate.DataFrag;
import com.chaosopher.tigerlang.compiler.translate.FragList;
import com.chaosopher.tigerlang.compiler.translate.FragmentVisitor;
import com.chaosopher.tigerlang.compiler.translate.ProcFrag;
import com.chaosopher.tigerlang.compiler.tree.BINOP;
import com.chaosopher.tigerlang.compiler.tree.CALL;
import com.chaosopher.tigerlang.compiler.tree.CJUMP;
import com.chaosopher.tigerlang.compiler.tree.CloningTreeVisitor;
import com.chaosopher.tigerlang.compiler.tree.EXP;
import com.chaosopher.tigerlang.compiler.tree.Exp;
import com.chaosopher.tigerlang.compiler.tree.ExpList;
import com.chaosopher.tigerlang.compiler.tree.MEM;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.tree.TEMP;
import com.chaosopher.tigerlang.compiler.util.Assert;

/**
 * This class reassembles an canonicalized IR tree.
 */
public class TreeDeatomizer implements FragmentVisitor {

    private class DeadCodeRemoval extends CloningTreeVisitor {

        private final Liveness livenessDataFlow;
        private final List<Stm> deadCode = new ArrayList<>();

        public DeadCodeRemoval(final Liveness livenessDataFlow) {
            this.livenessDataFlow = livenessDataFlow;
        }

        /**
         * Examines move statements to see if they are dead. If a
         * move temp is never used, it can be removed.
         */
        @Override
        public void visit(MOVE op) {
            Set<Temp> defs = ExtractDefs.getDefs(op);
            if(!this.livenessDataFlow.getOut(op).containsAll(defs)) {
                this.deadCode.add(op);
            }
            // clone it
            super.visit(op);
        }

        /**
         * Removes stm that are dead.
         */
        @Override
        public void visit(StmList stmList) {
            StmList clone = null, temp = null;
            for(StmList next = stmList; next != null; next = next.tail) {
                next.head.accept(this);
                if(this.deadCode.contains(next.head)) {
                   continue;
                }
                Stm cloned = this.getStm();
                if(clone == null) {
                    clone = temp = new StmList(cloned);
                } else {
                    temp.tail = (temp = new StmList(cloned));
                }
            }
            this.setStmList(clone);
        }
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
                Assert.assertNotNull(stm, "Statement cannot be null");
                Set<Integer> reaching = rdDataFlow.getIn(stm);
                return reaching.contains(this.defId);
            }

            boolean expressionIsAvailable(Stm stm) {
                Assert.assertNotNull(stm, "Statement cannot be null");
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
        public void visit(EXP op) {
            this.setCurrentStm(op);
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
            Exp clonedLeft = this.getExp();
            right.accept(this);
            Exp cloneRight = this.getExp();
            this.setStm(new CJUMP(op.relop, clonedLeft, cloneRight, op.iftrue, op.iffalse));
            this.setCurrentStm(null);
        }


        private Exp replace(Exp re) {
            // get statement this expression is part of
            Stm stm = this.getCurrentStm();
            Assert.assertNotNull(stm, "Statement cannot be null");
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
        public void visit(MEM op) {
            Exp exp = op.exp;
            exp = this.replace(exp);
            exp.accept(this);
            Exp clonedExp = this.getExp();
            this.setExp(new MEM(clonedExp));
        }

        @Override
        public void visit(BINOP op) {
            Exp left = op.left;
            left = this.replace(left);
            Exp right = op.right;
            right = this.replace(right);

            left.accept(this);
            Exp clonedLeft = this.getExp();
            right.accept(this);
            Exp cloneRight = this.getExp();
            this.setExp(new BINOP(op.binop, clonedLeft, cloneRight));
        }

        @Override
        public void visit(CALL op) {
            ExpList clonedExpList = null, temp = null;
            for(ExpList expList = op.args; expList != null; expList = expList.tail) {
                Exp arg = expList.head;
                arg = this.replace(arg);
                arg.accept(this);
                Exp clonedArg = this.getExp();
                if(clonedExpList == null) {
                    clonedExpList = temp = new ExpList(clonedArg);
                } else {
                    temp.tail = temp = new ExpList(clonedArg);
                }
            }
            op.func.accept(this);
            Exp clonedF = this.getExp();
            this.setExp(new CALL(clonedF, clonedExpList));
        }
    }

    public static final TreeDeatomizer apply(final FragList fragList) {
        TreeDeatomizer treeDeatomizer = new TreeDeatomizer();
        fragList.accept(treeDeatomizer);
        return treeDeatomizer;
    } 

    private FragList fragList = null;

    private TreeDeatomizer() {
        super();
    }

    public FragList getDeatomizedFragList() {
        return this.fragList;
    }

    @Override
    public void visit(ProcFrag procFrag) {
        // use data flow analysis to recombine tree.
        CFG cfg = CFG.build((StmList)procFrag.body);
        GenKillSets<Integer> rdGenKillSets = DefGenKillSets.analyse(cfg);
        RDDataFlow rdDataFlow = RDDataFlow.analyze(cfg, rdGenKillSets);
        GenKillSets<Exp> aeGenKillSets = AEGenKillSets.analyse(cfg);
        AEDataFlow aeDataFlow = AEDataFlow.analyze(cfg, aeGenKillSets);
        TACloningTreeVisitor cloningTreeVisitor = new TACloningTreeVisitor(rdDataFlow, aeDataFlow);
        procFrag.body.accept(cloningTreeVisitor);
        StmList deatomized = cloningTreeVisitor.getStmList();
        // remove old statements.
        cfg = CFG.build(deatomized);
        GenKillSets<Temp> liveGenKillSets = LiveGenKillSets.analyse(cfg);
        Liveness liveness = Liveness.analyze(cfg, liveGenKillSets);
        DeadCodeRemoval deadCodeRemoval = new DeadCodeRemoval(liveness);
        deatomized.accept(deadCodeRemoval);
        StmList cleaned = deadCodeRemoval.getStmList();
        ProcFrag lirProcFrag = new ProcFrag(cleaned, procFrag.frame);
        this.fragList = new FragList(lirProcFrag, this.fragList);
    }

    @Override
    public void visit(DataFrag dataFrag) {
        this.fragList = new FragList(dataFrag, this.fragList);
    }

}