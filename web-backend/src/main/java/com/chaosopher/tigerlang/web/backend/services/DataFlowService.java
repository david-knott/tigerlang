package com.chaosopher.tigerlang.web.backend.services;

import java.util.Set;

import com.chaosopher.tigerlang.compiler.canon.CanonicalizationImpl;
import com.chaosopher.tigerlang.compiler.dataflow.TreeAtomizer;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.graph.Node;
import com.chaosopher.tigerlang.compiler.translate.FragList;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;

import org.springframework.stereotype.Service;

@Service
class DataFlowServiceImpl implements DataFlowService {

    private CFG cfg;

    public CFG getCFG() {
        return this.cfg;
    }

    @Override
    public void init(FragList fragList) {
        TreeAtomizer treeAtomizer = new TreeAtomizer(new CanonicalizationImpl());
        fragList.accept(treeAtomizer);
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
    public Set<Integer> getIn(BasicBlock basicBlock, Stm stm) {
        return null;
    }

    @Override
    public Set<Integer> getOut(BasicBlock basicBlock, Stm stm) {
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

public interface DataFlowService {

    void init(FragList fragList);

    CFG getCFG();

    BasicBlock getBasicBlock(Node node);

    Set<Integer> getGen(Stm stm);

    Set<Integer> getKill(Stm stm);

    Set<Integer> getGen(BasicBlock basicBlock);

    Set<Integer> getKill(BasicBlock basicBlock);

    Set<Integer> getIn(BasicBlock basicBlock, Stm stm);

    Set<Integer> getOut(BasicBlock basicBlock, Stm stm);

    Set<Integer> getIn(BasicBlock basicBlock);

    Set<Integer> getOut(BasicBlock basicBlock);
}