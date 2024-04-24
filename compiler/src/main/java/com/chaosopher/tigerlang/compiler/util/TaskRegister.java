package com.chaosopher.tigerlang.compiler.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.chaosopher.tigerlang.compiler.core.LL;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;

public class TaskRegister {
    private LL<TaskWrapper> tasks = null;
    private List<Task> activeTasks = new ArrayList<Task>();
    InputStream in = null;
    OutputStream out = null;
    ErrorMsg errorMsg = null;

    class TaskWrapper implements Comparable<TaskWrapper> {
        final Task task;
        final String longName;
        final String shortName;
        String args = null;

        public TaskWrapper(Task task, String longName, String shortName) {
            this.task = task;
            this.longName = longName;
            this.shortName = shortName;
        }

        @Override
        public int compareTo(TaskWrapper o) {
            // TODO Auto-generated method stub
            return 0;
        }
    }

    public TaskRegister parseArgs(String[] args) {
        List<Task> taskList = new ArrayList<>();
        //only interested in n - 1 args, as nth arg is filename.
        for(int i = 0; i < args.length - 1; i++) {
            if(args[i].startsWith("--")) {
                Task task = this.findTaskByLongName(args[i].substring(2));
                taskList.add(task);
                //this.resolveDeps(task);
                if(i + 1 < args.length - 1  && args[i + 1].length() > 2 && !args[i + 1].substring(0, 2).equals("--")) {
                    //System.out.println("lvalue =" + args[i + 1]);
                    i++;
                    // ignored for now
                    // set argument in TaskWrapper.
                    // use argument in execute method below.
                }
            } else if(args[i].startsWith("-")) {
                for(int j = 1; j < args[i].length(); j++) {
                    Task task = this.findTaskByShortName(args[i].substring(j, j + 1));
                    taskList.add(task);
                 //   this.resolveDeps(task);
                }
            } else {
                throw new Error("Invalid argument syntax, show help.. " + args[i]);
            }
        }
        // process selected tasks
        this.process(taskList);
        //
        return this;
    }

    private void process(List<Task> taskList) {
        // iterate through all selected tasks
        for(Task task : taskList) {
            this.enableTask(task.getLongName());
        }
    }
 
    private void resolveDependancies(Task task) {
        List<Task> subList = new ArrayList<>();
        for(String dep : task.deps()) {
            if(dep.equals("")) continue;
            subList.add(this.findTaskByLongName(dep));
        }
        String[] depTask = task.resolveDeps(subList);
        for(String s: depTask) {
            if(s.equals("")) continue;
            if(null != this.findTaskByLongName(s)) {
                this.enableTask(s);
            }
        }
    }

    private void enableTask(String s) {
        Task task = this.findTaskByLongName(s);
        task.active = true;
        this.resolveDependancies(task);
        if(!activeTasks.contains(task)) {
            if(task instanceof BooleanTask) {
                activeTasks.add(0, task);
            } else {
                activeTasks.add(task);
            }
        }
    }

    private Task findTaskByLongName(String name) {
        LL<TaskWrapper> f = this.tasks;
        for(;f != null && !f.head.longName.equals(name); f = f.tail);
        if(f == null) throw new Error("No task for long name:  '" +  name + "'");
        return f.head.task;
    }

    private Task findTaskByShortName(String name) {
        LL<TaskWrapper> f = this.tasks;
        for(;f != null; f = f.tail) {
            if(f.head.shortName.equals("")) continue;
            if(f.head.shortName.equals(name)) break;;
        }
        if(f == null) throw new Error("No task for short name:  '" +  name + "'");
        return f.head.task;
    }

    /**
     * Executes all the active tasks. A @see Util.TaskContext instance is created using the streams and error instance.
     * @param in stream to read the tiger input from.
     * @param out stream to write the compiled assembly to.
     * @param log stream to write debug information to.
     * @param errorMsg object to capture error messages.
     * @return task register instance.
     */
    public TaskRegister execute(InputStream in, OutputStream out, OutputStream log, ErrorMsg errorMsg) {
        TaskContext taskContext = new TaskContext(in, out, log, errorMsg);
        for(Task t : this.activeTasks) {
            if(t.active) {
                if(taskContext.errorMsg.anyErrors) {
                    return this;
                }
                Timer.instance.push(t.name);
                t.execute(taskContext);
                Timer.instance.pop();
            }
        }
        return this;
    }

    public TaskRegister register(TaskProvider taskProvider) {
        taskProvider.build(this);
        return this;
    }

    public void register(SimpleTask simpleTask) {
        this.tasks = LL.<TaskWrapper>insertRear(this.tasks, new TaskWrapper(simpleTask, simpleTask.getLongName(), simpleTask.getShortName()));
    }

    public void register(BooleanTask booleanTask) {
        this.tasks = LL.<TaskWrapper>insertRear(this.tasks, new TaskWrapper(booleanTask, booleanTask.getLongName(), booleanTask.getShortName()));
    }

    public void register(DisjunctiveTask disjunctiveTask) {
        this.tasks = LL.<TaskWrapper>insertRear(this.tasks, new TaskWrapper(disjunctiveTask, disjunctiveTask.getLongName(), disjunctiveTask.getShortName()));
    }
}