package com.chaosopher.tigerlang.compiler.parse;

import com.chaosopher.tigerlang.compiler.util.BooleanTask;
import com.chaosopher.tigerlang.compiler.util.BooleanTaskFlag;
import com.chaosopher.tigerlang.compiler.util.SimpleTask;
import com.chaosopher.tigerlang.compiler.util.SimpleTaskProvider;
import com.chaosopher.tigerlang.compiler.util.TaskContext;
import com.chaosopher.tigerlang.compiler.util.TaskProvider;
import com.chaosopher.tigerlang.compiler.util.TaskRegister;

import com.chaosopher.tigerlang.compiler.absyn.DecList;
import com.chaosopher.tigerlang.compiler.util.Assert;

/**
 * Provides a collection of tasks related to the parse phase. Constructor
 * accepts the parser implementation to use.
 */
public class Tasks implements TaskProvider {

    final ParserService parserService;

    public Tasks(ParserService parserService) {
        Assert.assertNotNull(parserService);
        this.parserService = parserService;
    }

    @Override
    public void build(TaskRegister taskRegister) {
        taskRegister.register(
            new BooleanTask(new BooleanTaskFlag() {
                @Override
                public void set() {
                    parserService.configure(p -> p.setParserTrace(true));
                }
            }, "parse-trace", "Enable parsers traces.", "")
        );
        taskRegister.register(
            new BooleanTask(new BooleanTaskFlag() {
                @Override
                public void set() {
                    parserService.configure(p -> p.setNoPrelude(true));
                }
            }, "X|no-prelude", "Don’t include prelude.", "")
        );
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    DecList decList = parserService.parse(taskContext.in, taskContext.errorMsg);
                    if(taskContext.errorMsg.anyErrors) {
                        // there was a lexical or parse error, cannot continue
                    } else {
                        taskContext.setDecList(decList);
                    }
                }
            }, "parse", "parse", "")
        );
    }
}