package com.chaosopher.tigerlang.compiler.canon;

import com.chaosopher.tigerlang.compiler.translate.DataFrag;
import com.chaosopher.tigerlang.compiler.translate.FragList;
import com.chaosopher.tigerlang.compiler.translate.FragmentVisitor;
import com.chaosopher.tigerlang.compiler.translate.ProcFrag;
import com.chaosopher.tigerlang.compiler.tree.TreeSimplifierVisitor;
import com.chaosopher.tigerlang.compiler.tree.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.util.Assert;
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
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    FragList frags = taskContext.hirFragList;
                    Assert.assertNotNull(frags);
                    CanonVisitor canonVisitor = new CanonVisitor(canonicalization);
                    frags.accept(canonVisitor);
                    taskContext.setLIR(canonVisitor.fragList);
                }
            }, "lir-compute", "Perform canonicalisation of HIR tree", "hir-compute")
        );
        taskRegister.register(
            new SimpleTask((taskContext) -> taskContext.lirFragList.accept(new FragPrettyPrinter(new PrettyPrinter(taskContext.log))),
                "lir-display", "Displays the lir", "lir-compute")
        );
        taskRegister.register(
            new SimpleTask((taskContext) -> taskContext.hirFragList.accept(new FragmentVisitor() {
                @Override
                public void visit(ProcFrag procFrag) {
                    TreeSimplifierVisitor expandTempVisitor = new TreeSimplifierVisitor();
                    procFrag.body.accept(expandTempVisitor);
                }
                @Override
                public void visit(DataFrag dataFrag) {
                }
                }), 
                "hir-simplify", 
                "Simplifies the hir in preparation for optimisation",
                "hir-compute"
            )
        );
        taskRegister.register(
            new SimpleTask((taskContext) -> taskContext.hirFragList.accept(new FragmentVisitor() {
                    public void visit(ProcFrag frags) {
                        CanonVisitor canonVisitor = new CanonVisitor(canonicalization);
                        frags.accept(canonVisitor);
                        taskContext.setLIR(canonVisitor.fragList);
                    }
                    @Override
                    public void visit(DataFrag dataFrag) {
                    }
                }), 
                "hir-simplify-canon", 
                "Simplifies the hir in preparation for optimisation",
                "hir-simplify"
            )
        );

        taskRegister.register(
            new SimpleTask((taskContext) -> taskContext.lirFragList.accept(new FragmentVisitor() {

                public void visit(ProcFrag procFrag) {
                    procFrag.body.accept(new PrettyPrinter(taskContext.log));
                }

                @Override
                public void visit(DataFrag dataFrag) {
                    // do nothing.
                }
                    
                }), 
                "lir-simplify-display", 
                "Simplifies the hir in preparation for optimisation",
                "hir-simplify-canon"
            )
        );


    }
}