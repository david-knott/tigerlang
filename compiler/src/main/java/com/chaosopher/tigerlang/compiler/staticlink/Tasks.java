package com.chaosopher.tigerlang.compiler.staticlink;

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
                    FunctionStaticLinkVisitor functionStaticLinkVisitor = new FunctionStaticLinkVisitor();
                    taskContext.decList.accept(functionStaticLinkVisitor);
                }
            }, "optimise-staticlinks", "", "bindings-compute")
        );
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    StaticLinkEscapeVisitor staticLinkEscapeVisitor = new StaticLinkEscapeVisitor(taskContext.decList);
                    taskContext.decList.accept(staticLinkEscapeVisitor);
                }
            }, "optimise-staticlinks-escapes", "", "optimise-staticlinks")
        );
    }
}