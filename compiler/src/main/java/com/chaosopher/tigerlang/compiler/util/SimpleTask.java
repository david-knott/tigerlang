package com.chaosopher.tigerlang.compiler.util;

import java.util.List;

public class SimpleTask extends Task {
    private SimpleTaskProvider prov;

    public SimpleTask(SimpleTaskProvider prov, String name, String description, String deps) {
        super(name, description, deps);
        this.prov = prov;
    }

    @Override
    public void execute(TaskContext taskContext) {
        this.prov.only(taskContext);
    }

    @Override
    public String[] resolveDeps(List<Task> activeTasks) {
        return this.deps();
    }
}