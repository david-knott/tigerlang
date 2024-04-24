package com.chaosopher.tigerlang.compiler.util;

import java.util.List;

public abstract class Task implements Comparable<Task> {
    public final String name;
    public final String description;
    public final String deps;
    private final String shortName;
    private final String longName;

    public Task(String name, String description, String deps) {
        Assert.assertNotNull(name);
        Assert.assertNotNull(description);
        Assert.assertNotNull(deps);
        this.name = name;
        this.description = description;
        this.deps = deps;
        String[] names = this.extractNames(this);
        this.longName = !names[1].equals("") ? names[1] : names[0];
        this.shortName = !names[1].equals("") ? names[0] : names[1];
    }

    public String getLongName() {
        return longName;
    }

    public String getShortName() {
        return shortName;
    }

    /**
     * Returns short and long name in result[0] and result[1] respectively. It is
     * assumed at least the long form is present.
     * 
     * @param task
     * @return
     */
    public String[] extractNames(Task task) {
        String name = task.name;
        String[] results = new String[2];
        int i = 0;
        for(; i < name.length() && name.charAt(i) != '|'; i++);
        results[0] = name.substring(0, i);
        results[1] = i < name.length() - 1? name.substring(i + 1) : "";
        return results;
    }

    public abstract void execute(TaskContext taskContext);

    public Task next;

    public boolean active = false;

    @Override
    public int compareTo(Task o) {
        return this.hashCode();
    }

    public String[] deps() {
        return this.deps.split("\\s+");
    }


    public abstract String[] resolveDeps(List<Task> activeTasks);
}