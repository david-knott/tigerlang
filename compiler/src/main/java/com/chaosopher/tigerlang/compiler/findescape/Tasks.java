package com.chaosopher.tigerlang.compiler.findescape;

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
                    EscapeVisitor escapeVisitor = new EscapeVisitor(taskContext.errorMsg);
                    taskContext.decList.accept(escapeVisitor);
                }
            }, "e|escapes-compute", "escape", "parse")
        );
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    taskContext.setEscapesDisplay(true);

                }
            }, "E|escapes-display", "escape", "escapes-compute")
        );
    }
}