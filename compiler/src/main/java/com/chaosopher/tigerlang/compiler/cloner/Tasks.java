package com.chaosopher.tigerlang.compiler.cloner;

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
                    AbsynCloner absynCloner = new AbsynCloner();
                    taskContext.decList.accept(absynCloner);
                    taskContext.setDecList(absynCloner.visitedDecList);
                }
            }, "clone", "Clone the ast", "parse")
        );
    }
}