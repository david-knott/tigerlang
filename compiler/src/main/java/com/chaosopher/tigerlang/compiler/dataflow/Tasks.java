package com.chaosopher.tigerlang.compiler.dataflow;

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
                TreeAtomizer treeAtomizer = TreeAtomizer.apply(new CanonicalizationImpl(), taskContext.hirFragList);
                taskContext.setLIR(treeAtomizer.getAtomizedFragList());

                //TreeDeatomizer treeDeatomizer = TreeDeatomizer.apply(fragList);

                /*
                NopFragmentOptimizer fragmentOptimezer = new NopFragmentOptimizer(new CloningTreeVisitor());
                taskContext.lirFragList.accept(fragmentOptimezer);
                taskContext.setFragList(fragmentOptimezer.fragList);

                ConstantPropagationFragmentOptimizer constOptimezer = new ConstantPropagationFragmentOptimizer();
                taskContext.lirFragList.accept(constOptimezer);
                taskContext.setLIR(constOptimezer.fragList);

                CopyPropagationFragmentOptimizer copyOptimiser = new CopyPropagationFragmentOptimizer();
                taskContext.lirFragList.accept(copyOptimiser);
                taskContext.setLIR(copyOptimiser.fragList);

*/


            //    TreeDeatomizer2 treedeatomizer = new TreeDeatomizer2();
            //    taskContext.lirFragList.accept(treedeatomizer);
              //  taskContext.setLIR(treedeatomizer.fragList);
            }
            }, "deatomize", "Datomize hir tree", "hir-compute")
        );
        
        taskRegister.register(new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                TreeAtomizer treeAtomizer = TreeAtomizer.apply(new CanonicalizationImpl(), taskContext.hirFragList);
                taskContext.setLIR(treeAtomizer.getAtomizedFragList());
                }
            }, "atomize", "Atomize lir tree", "hir-compute")
        );

        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
            @Override
                public void only(TaskContext taskContext) {
              //      (new CFGGraphizRender2(new PrintStream(taskContext.out))).start(taskContext.lirFragList);
                }
            }, "cfg", "Build cfg", "deatomize")
        );

        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
            @Override
                public void only(TaskContext taskContext) {
                //    taskContext.lirFragList.accept(new KillGenDisplay(taskContext.out));
                }
            }, "killgen-display", "Display Killgen Information", "atomize")
        );

        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
            @Override
                public void only(TaskContext taskContext) {
             //       (new CFGGraphizRender2(new PrintStream(taskContext.out))).start(taskContext.lirFragList);
                }
            }, "reachdef-display", "Display Reachable Definitions Information", "deatomize")
        );


    }
}