package com.chaosopher.tigerlang.compiler.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.chaosopher.tigerlang.compiler.core.LL;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;

public class TaskRegister {
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

    private LL<TaskWrapper> tasks = null;
    private List<Task> activeTasks = new ArrayList<Task>();

    public TaskRegister parseArgs(String[] args) {
        //only interested in n - 1 args, as nth arg is filename.
        for(int i = 0; i < args.length - 1; i++) {
            if(args[i].startsWith("--")) {
                Task task = this.findTaskByLongName(args[i].substring(2));
                this.resolveDeps(task);
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
                    this.resolveDeps(task);
                }
            } else {
                throw new Error("Invalid argument syntax, show help.. " + args[i]);
            }
        }
        return this;
    }
 
    private void resolveDeps(Task task) {
        task.active = true;
        if(!activeTasks.contains(task)) {
            activeTasks.add(task);
        }
        if(task.deps != null) {
            for(String dep : task.deps.split("\\s+")) {
                if(!dep.equals("")) {
                    Task depTask = this.findTaskByLongName(dep);
                    this.resolveDeps(depTask);
                }
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
        for (LL<TaskWrapper> t = this.tasks; t != null; t = t.tail) {
            if(t.head.task.active) {
                Timer.instance.push(t.head.task.name);
                t.head.task.execute(taskContext);
                Timer.instance.pop();
            }
        }
        return this;
    }

    public TaskRegister register(TaskProvider taskProvider) {
        taskProvider.build(this);
        return this;
    }

    /**
     * Returns short and long name in result[0] and
     * result[1] respectively. It is assumed at least the
     * long form is present.
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

    public void register(SimpleTask simpleTask) {
        String[] names = this.extractNames(simpleTask);
        String longName = !names[1].equals("") ? names[1] : names[0];
        String shortName = !names[1].equals("") ? names[0] : names[1];
        this.tasks = LL.<TaskWrapper>insertRear(this.tasks, new TaskWrapper(simpleTask, longName, shortName));
    }

    public void register(BooleanTask booleanTask) {
        String[] names = this.extractNames(booleanTask);
        String longName = !names[1].equals("") ? names[1] : names[0];
        String shortName = !names[1].equals("") ? names[0] : names[1];
        this.tasks = LL.<TaskWrapper>insertRear(this.tasks, new TaskWrapper(booleanTask, longName, shortName));
    }
}