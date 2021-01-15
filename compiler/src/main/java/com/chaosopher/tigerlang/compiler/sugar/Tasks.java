package com.chaosopher.tigerlang.compiler.sugar;

import com.chaosopher.tigerlang.compiler.bind.Binder;
import com.chaosopher.tigerlang.compiler.findescape.EscapeVisitor;
import com.chaosopher.tigerlang.compiler.util.BooleanTask;
import com.chaosopher.tigerlang.compiler.util.BooleanTaskFlag;
import com.chaosopher.tigerlang.compiler.util.SimpleTask;
import com.chaosopher.tigerlang.compiler.util.SimpleTaskProvider;
import com.chaosopher.tigerlang.compiler.util.TaskContext;
import com.chaosopher.tigerlang.compiler.util.TaskProvider;
import com.chaosopher.tigerlang.compiler.util.TaskRegister;

public class Tasks implements TaskProvider {

    @Override
    public void build(TaskRegister taskRegister) {
        taskRegister.register(
            new BooleanTask(new BooleanTaskFlag() {
                @Override
                public void set() {
                    //set
                    throw new Error(); 
                }
            }, "desugar-for", "Desugar 'for' loops", "")
        );

        taskRegister.register(
            new BooleanTask(new BooleanTaskFlag() {
                @Override
                public void set() {
                    //set
                    throw new Error(); 
                }
            }, "desugar-string-cmp", "Desugar string comparisions", "")
        );
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    Desugar desugar = new Desugar(false, true);
                    taskContext.decList.accept(desugar);
                    taskContext.setDecList(desugar.visitedDecList);
                    taskContext.decList.accept(new EscapeVisitor(taskContext.errorMsg));
                    taskContext.decList.accept(new Binder(taskContext.errorMsg));
                }
            }, "desugar", "desugar the AST", "types-compute rename")
        );
    }
}