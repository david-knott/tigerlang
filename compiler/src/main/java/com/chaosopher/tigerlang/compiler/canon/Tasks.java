package com.chaosopher.tigerlang.compiler.canon;

import com.chaosopher.tigerlang.compiler.translate.FragList;
import com.chaosopher.tigerlang.compiler.translate.FragmentPrinter;
import com.chaosopher.tigerlang.compiler.util.DisjunctiveTask;
import com.chaosopher.tigerlang.compiler.util.SimpleTask;
import com.chaosopher.tigerlang.compiler.util.SimpleTaskProvider;
import com.chaosopher.tigerlang.compiler.util.TaskContext;
import com.chaosopher.tigerlang.compiler.util.TaskProvider;
import com.chaosopher.tigerlang.compiler.util.TaskRegister;

public class Tasks implements TaskProvider {

    final Canonicalization canonicalization;

    public Tasks(Canonicalization canonicalization) {
        this.canonicalization = canonicalization;
    }

    @Override
    public void build(TaskRegister taskRegister) {

        taskRegister.register(new DisjunctiveTask("optimize", "Default optimisation is canonicalisation", "lir-compute deatomize"));

        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    FragList frags = taskContext.hirFragList;
                    CanonVisitor canonVisitor = new CanonVisitor(canonicalization);
                    frags.accept(canonVisitor);
                    taskContext.setLIR(canonVisitor.fragList);
                }
            }, "lir-compute", "Perform canonicalisation of HIR tree", "hir-compute")
        );

        taskRegister.register(
            new SimpleTask((taskContext) -> taskContext.lirFragList.accept(new FragmentPrinter(taskContext.out)),
                "lir-display", "Displays the lir", "optimize")
        );
    }
}