package com.chaosopher.tigerlang.compiler.main;

import com.chaosopher.tigerlang.compiler.util.TaskProvider;
import com.chaosopher.tigerlang.compiler.util.TaskRegister;

public class Tasks implements TaskProvider {

    @Override
    public void build(TaskRegister taskRegister) {
        /*
        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
            //    taskContext.out = taskContext.log;
            }
        }, "output", "Output assembly to file", "");

        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
            //    taskContext.out = taskContext.log;
            }
        }, "input", "Input tiger from file", "");
        */
    }
}