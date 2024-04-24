package com.chaosopher.tigerlang.compiler.util;

import java.util.List;

public class DisjunctiveTask extends Task {

    public DisjunctiveTask(String name, String description, String deps) {
        super(name, description, deps);
    }

    @Override
    public void execute(TaskContext taskContext) {
        // do nothing.
    }

    @Override
    public String[] resolveDeps(List<Task> activeTasks) {
        if(!activeTasks.isEmpty() || deps().length == 0) {
           return new String[]{};
        } else {
            return new String[]{deps()[0]};
        }
    }
}