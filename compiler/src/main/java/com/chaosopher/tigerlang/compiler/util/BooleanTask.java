package com.chaosopher.tigerlang.compiler.util;

import java.util.List;

/**
 * A task that sets a boolean value.
 */
public class BooleanTask extends Task {
    private BooleanTaskFlag booleanTaskFlag;

    public BooleanTask(BooleanTaskFlag flag, String name, String description, String deps) {
        super(name, description, deps);
        this.booleanTaskFlag = flag;
    }

    @Override
    public void execute(TaskContext taskContext) {
        this.booleanTaskFlag.set();
    }

    @Override
    public String[] resolveDeps(List<Task> activeTasks) {
        return this.deps();
    }
}