package com.chaosopher.tigerlang.compiler.util;

public abstract class Task implements Comparable<Task> {
    public final String name;
    public final String description;
    public final String deps;

    public Task(String name, String description, String deps) {
        Assert.assertNotNull(name);
        Assert.assertNotNull(description);
        Assert.assertNotNull(deps);
        this.name = name;
        this.description = description;
        this.deps = deps;
    }
    public abstract void execute(TaskContext taskContext);

    public Task next;

    public boolean active = false;

    @Override
    public int compareTo(Task o) {
        return this.hashCode();
    }
}