package com.chaosopher.tigerlang.compiler.dataflow;

import java.io.PrintStream;

import com.chaosopher.tigerlang.compiler.canon.CanonicalizationImpl;
import com.chaosopher.tigerlang.compiler.util.SimpleTask;
import com.chaosopher.tigerlang.compiler.util.SimpleTaskProvider;
import com.chaosopher.tigerlang.compiler.util.TaskContext;
import com.chaosopher.tigerlang.compiler.util.TaskProvider;
import com.chaosopher.tigerlang.compiler.util.TaskRegister;


public class Tasks implements TaskProvider {

    @Override
    public void build(TaskRegister taskRegister) {
        taskRegister.register(new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                TreeAtomizer treeAtomizer = new TreeAtomizer(new CanonicalizationImpl());
                TreeDeatomizer treedeatomizer = new TreeDeatomizer(treeAtomizer.getTemps());

                FragmentTreeAtomizer atomizerVisitor = new FragmentTreeAtomizer(treeAtomizer);
                taskContext.hirFragList.accept(atomizerVisitor);
                taskContext.setLIR(atomizerVisitor.fragList);

                NopFragmentOptimizer fragmentOptimezer = new NopFragmentOptimizer(new CloningTreeVisitor());
                taskContext.lirFragList.accept(fragmentOptimezer);
                taskContext.setFragList(fragmentOptimezer.fragList);

                ConstantPropagationFragmentOptimizer constOptimezer = new ConstantPropagationFragmentOptimizer();
                taskContext.lirFragList.accept(constOptimezer);
                taskContext.setLIR(constOptimezer.fragList);
                
                FragmentTreeDeatomizer fragmentVisitor = new FragmentTreeDeatomizer(treedeatomizer);
                taskContext.lirFragList.accept(fragmentVisitor);
                taskContext.setLIR(fragmentVisitor.fragList);
            }
            }, "deatomize", "Atomize hir tree", "hir-compute")
        );
        
        taskRegister.register(new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                TreeAtomizer treeAtomizer = new TreeAtomizer(new CanonicalizationImpl());
                FragmentTreeAtomizer atomizerVisitor = new FragmentTreeAtomizer(treeAtomizer);
                taskContext.hirFragList.accept(atomizerVisitor);
                taskContext.setLIR(atomizerVisitor.fragList);
                }
            }, "atomize", "Atomize lir tree", "hir-compute")
        );

        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
            @Override
                public void only(TaskContext taskContext) {
                    (new CFGGraphizRender2(new PrintStream(taskContext.out))).start(taskContext.lirFragList);
                }
            }, "cfg", "Build cfg", "deatomize")
        );

        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {

                    throw new UnsupportedOperationException();
                }
            }, "common-subexpression-elimination", "Eliminate common subexpressions from quadruples", "hir-compute")
        );
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    throw new UnsupportedOperationException();

                }
            }, "constant-propagation", "Propogate constants expressions", "escapes-compute")
        );
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    throw new UnsupportedOperationException();

                }
            }, "copy-propagation", "Propogate variable expressions", "escapes-compute")
        );
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    throw new UnsupportedOperationException();

                }
            }, "deadcode-elimination", "Remove dead code from quadruples", "escapes-compute")
        );
    }
}