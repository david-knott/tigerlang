package com.chaosopher.tigerlang.web.backend.services.impl;

import java.util.Set;

import com.chaosopher.tigerlang.compiler.canon.CanonicalizationImpl;
import com.chaosopher.tigerlang.compiler.dataflow.TreeAtomizer;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.graph.Node;
import com.chaosopher.tigerlang.compiler.translate.FragList;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.web.backend.services.ReDefDataFlowService;

import org.springframework.stereotype.Service;

@Service
class ReDefDataFlowServiceImpl implements ReDefDataFlowService {

    private CFG cfg;

    public CFG getCFG() {
        return this.cfg;
    }

    @Override
    public void init(FragList fragList) {
        TreeAtomizer treeAtomizer = TreeAtomizer.apply(new CanonicalizationImpl(), fragList);
        StmList stmList = treeAtomizer.getCanonicalisedAtoms();
        this.cfg = CFG.build(stmList);
    }

    @Override
    public BasicBlock getBasicBlock(Node node) {
        return this.cfg.get(node);
    }

    @Override
    public Set<Integer> getGen(Stm stm) {
        return null;
    }

    @Override
    public Set<Integer> getKill(Stm stm) {
        return null;
    }

    @Override
    public Set<Integer> getGen(BasicBlock basicBlock) {
        return null;
    }

    @Override
    public Set<Integer> getKill(BasicBlock basicBlock) {
        return null;
    }

    @Override
    public Set<Integer> getIn(Stm stm) {
        return null;
    }

    @Override
    public Set<Integer> getOut(Stm stm) {
        return null;
    }

    @Override
    public Set<Integer> getIn(BasicBlock basicBlock) {
        return null;
    }

    @Override
    public Set<Integer> getOut(BasicBlock basicBlock) {
        return null;
    }
}