package com.chaosopher.tigerlang.compiler.types;

import com.chaosopher.tigerlang.compiler.util.SimpleTask;
import com.chaosopher.tigerlang.compiler.util.SimpleTaskProvider;
import com.chaosopher.tigerlang.compiler.util.TaskContext;
import com.chaosopher.tigerlang.compiler.util.TaskProvider;
import com.chaosopher.tigerlang.compiler.util.TaskRegister;

/**
 * Task provider for type checker.
 */
public class Tasks implements TaskProvider {

    public void build(TaskRegister taskRegister) {
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    TypeChecker.create(taskContext.decList, taskContext.errorMsg);
                }
            }, "types-compute", "Type checks the abstract syntax tree.", "bindings-compute")
        );
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    TypeChecker.create(taskContext.decList, taskContext.errorMsg);
                }
            }, "typed", "Default type checking.", "types-compute")
        );
    }
}