package com.chaosopher.tigerlang.compiler.dataflow;

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