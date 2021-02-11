package com.chaosopher.tigerlang.compiler.callgraph;

import java.io.PrintStream;

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
                    CallGraphVisitor  callGraphVisitor = new CallGraphVisitor();
                    taskContext.decList.accept(callGraphVisitor);
                    CallGraphizRender cfgRenderer = new CallGraphizRender(callGraphVisitor.functionCallGraph);
                    cfgRenderer.write(new PrintStream(taskContext.out));

                    
                }
            }, "callgraph-display", "dup the call graph", "bindings-compute")
        );
    }
}