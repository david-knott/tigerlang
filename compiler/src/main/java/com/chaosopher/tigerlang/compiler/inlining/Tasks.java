package com.chaosopher.tigerlang.compiler.inlining;

import com.chaosopher.tigerlang.compiler.bind.Binder;
import com.chaosopher.tigerlang.compiler.findescape.EscapeVisitor;
import com.chaosopher.tigerlang.compiler.util.SimpleTask;
import com.chaosopher.tigerlang.compiler.util.SimpleTaskProvider;
import com.chaosopher.tigerlang.compiler.util.TaskContext;
import com.chaosopher.tigerlang.compiler.util.TaskProvider;
import com.chaosopher.tigerlang.compiler.util.TaskRegister;

public class Tasks implements TaskProvider {

    @Override
    public void build(TaskRegister taskRegister) {
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    // Visit cloned tree
                    Inliner inliner = new Inliner(taskContext.decList);
                    taskContext.decList.accept(inliner);
                    taskContext.setDecList(inliner.visitedDecList);
                    taskContext.decList.accept(new EscapeVisitor(taskContext.errorMsg));
                    taskContext.decList.accept(new Binder(taskContext.errorMsg));
                }
            }, "inline", "inline functions", "types-compute rename")
        );
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    Pruner pruner = new Pruner(taskContext.decList);
                    taskContext.decList.accept(pruner);
                    taskContext.setDecList(pruner.visitedDecList);
                    taskContext.decList.accept(new EscapeVisitor(taskContext.errorMsg));
                    taskContext.decList.accept(new Binder(taskContext.errorMsg));
                }
            }, "prune", "prune unused functions", "types-compute rename")
        );
        

    }
}