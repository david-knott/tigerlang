package com.chaosopher.tigerlang.compiler.util;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Timer class used to timing how long each task takes.
 */
public class Timer {

    private TimeVar total;

    class TimeVar {

        long start;
        long stop;
        long first;
        long elapsed;
        boolean initial = true;

        public boolean isZero() {
            return (this.start - this.stop) == 0;
        }

        public void start() {
            this.start = System.currentTimeMillis();
            if(this.initial) {
                this.first = this.start;
                this.initial = false;
            }
        }

        public void stop() {
            this.stop = System.currentTimeMillis();
            this.elapsed+= this.stop - this.start;
        }

        public double elapsed() {
            return this.elapsed / 1000d;
        }
    }

    public static final Timer instance = new Timer();
    final Stack<TimeVar> tasks = new Stack<TimeVar>();
    final Map<String, TimeVar> table = new LinkedHashMap<String, TimeVar>();

    /**
     * Default constructor. 
     */
    public Timer() {
        this.total = new TimeVar();
    }

    /**
     * Start a subtimer for a named task.
     * @param name
     */
    public void push(String name) {
        // stop previous task if any.
        if(!this.tasks.empty()) {
            this.tasks.peek().stop();
        }
        // create and start next task
        TimeVar current = new TimeVar();
        this.table.put(name, current);
        this.tasks.push(current);
        current.start();
    }

    /**
     * Stops the current task and pops it off the stack.
     * If there is a previous task on the stack, start it.
     */
    public void pop() {
        Assert.assertIsTrue(!this.tasks.empty());
        this.tasks.peek().stop();
        this.tasks.pop();
        // set the start time of the previous task if present
        if(!this.tasks.empty()) {
            this.tasks.peek().start();
        }
    }

    private String displayTiming(String key, double timing, double total) {
        return String.format("%15s | %-5f | %5f %%", key, timing, timing * 100 / total);
    }
   
    /**
     * Dumps the task timings to the supplied output stream.
     */
    public void dump(OutputStream outputStream) {
        try(PrintStream ps = new PrintStream(outputStream)) {
            // execution time, time for each function to execute
            ps.println("Total execution times ( seconds )");
            for(String key : this.table.keySet()) {
                TimeVar timing = this.table.get(key);
                if(!timing.isZero()) {
                  //  ps.println(" " + key + " " + timing.elapsed());
                  ps.println(this.displayTiming(key, timing.elapsed(), this.total.elapsed()));
                }
            }
            ps.println();
            // cumulative time - commented out as it doesn't work correctly.
            /*
            ps.println("Cumulative times ( seconds )");
            for(String key : this.table.keySet()) {
                TimeVar timing = this.table.get(key);
                if(timing.stop != timing.first) {
                    ps.println(this.displayTiming(key, ((double)timing.stop - timing.first)/1000, this.total.elapsed()));
                }
            }
            ps.println();
            */
            // total time, time to complete compilation.
            ps.println("Total ( seconds ) : " + ((double)this.total.stop - this.total.start)/ 1000);
        }
    }

    /**
     * Starts the root timer.
     */
    public void start() {
        this.total.start();
    }


    /**
     * Signals to the timer that we are finished timing. This
     * ensures any remaning timings are stopped. It also records
     * the time when this function was called.
     */
	public void stop() {
        if(!this.tasks.empty()) {
            do {
                this.tasks.peek().stop();
                this.tasks.pop();
            }while(!this.tasks.empty());
        }
        this.total.stop();
	}
}