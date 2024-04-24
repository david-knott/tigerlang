package com.chaosopher.tigerlang.compiler.translate;

import com.chaosopher.tigerlang.compiler.util.Assert;
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
                    TranslatorVisitor translatorVisitor = new TranslatorVisitor();
                    taskContext.decList.accept(translatorVisitor);
                    FragList fragList = translatorVisitor.getFragList();
                    Assert.assertNotNull(fragList);
                    taskContext.setFragList(fragList);
                }
            }, "hir-compute", "Translate to HIR", "typed")
        );
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    taskContext.hirFragList.accept(new FragmentPrinter(taskContext.out));
                }
            }, "hir-display", "Display the HIR", "hir-compute")
        );
    }
}