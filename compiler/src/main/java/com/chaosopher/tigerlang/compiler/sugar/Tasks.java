package com.chaosopher.tigerlang.compiler.sugar;

import com.chaosopher.tigerlang.compiler.util.TaskProvider;
import com.chaosopher.tigerlang.compiler.util.TaskRegister;

public class Tasks implements TaskProvider {

    @Override
    public void build(TaskRegister taskRegister) {
        /*
        new BooleanTask(new BooleanTaskFlag() {
            @Override
            public void set() {
                //set
                throw new Error(); 
            }
        }, "desugar-for", "Desugar 'for' loops", "");
        new BooleanTask(new BooleanTaskFlag() {
            @Override
            public void set() {
                //set
                throw new Error(); 
            }
        }, "desugar-string-cmp", "Desugar string comparisions", "");
        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                Desugar desugar = new Desugar();
                taskContext.decList.accept(desugar);
                throw new Error(); 
            }
        }, "desugar", "desugar the AST", "types-compute rename");*/
    }
}