package com.chaosopher.tigerlang.compiler.absyn;

import java.io.PrintStream;

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
                    try(PrintStream printStream = new PrintStream(taskContext.out)) {
                        taskContext.decList.accept(new PrettyPrinter(printStream, taskContext.escapesDisplay, taskContext.bindingsDisplay));
                    }
                }
            }, "A|ast-display", "display abstract syntax tree", "parse")
        );
    }
}