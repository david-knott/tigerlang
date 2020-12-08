package com.chaosopher.tigerlang.compiler.inlining;

import com.chaosopher.tigerlang.compiler.util.TaskProvider;
import com.chaosopher.tigerlang.compiler.util.TaskRegister;

public class Tasks implements TaskProvider {

    @Override
    public void build(TaskRegister taskRegister) {
        /*
        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                throw new Error("Not implemented");
            }
        }, "inline", "inline functions", "types-compute rename");
        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                throw new Error("Not implemented");
            }
        }, "prune", "prune unused functions", "types-compute rename");
        */

    }
}