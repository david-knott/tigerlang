package com.chaosopher.tigerlang.web.backend.services;

import java.util.Set;

import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.graph.Node;
import com.chaosopher.tigerlang.compiler.translate.FragList;
import com.chaosopher.tigerlang.compiler.tree.Stm;

public interface ReDefDataFlowService {

    void init(FragList fragList);

    CFG getCFG();

    BasicBlock getBasicBlock(Node node);

    // reaching definitions
    Set<Integer> getGen(Stm stm);

    Set<Integer> getKill(Stm stm);

    Set<Integer> getGen(BasicBlock basicBlock);

    Set<Integer> getKill(BasicBlock basicBlock);

    Set<Integer> getIn(Stm stm);

    Set<Integer> getOut(Stm stm);

    Set<Integer> getIn(BasicBlock basicBlock);

    Set<Integer> getOut(BasicBlock basicBlock);
}