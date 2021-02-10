package com.chaosopher.tigerlang.compiler.dataflow;

import java.io.PrintStream;

import com.chaosopher.tigerlang.compiler.canon.CanonicalizationImpl;
import com.chaosopher.tigerlang.compiler.graph.GraphvisRenderer;
import com.chaosopher.tigerlang.compiler.temp.DefaultMap;
import com.chaosopher.tigerlang.compiler.translate.DataFrag;
import com.chaosopher.tigerlang.compiler.translate.FragmentVisitor;
import com.chaosopher.tigerlang.compiler.translate.ProcFrag;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.util.Assert;
import com.chaosopher.tigerlang.compiler.util.SimpleTask;
import com.chaosopher.tigerlang.compiler.util.SimpleTaskProvider;
import com.chaosopher.tigerlang.compiler.util.TaskContext;
import com.chaosopher.tigerlang.compiler.util.TaskProvider;
import com.chaosopher.tigerlang.compiler.util.TaskRegister;

public class Tasks implements TaskProvider {

    private final TreeAtomizer treeAtomizer = new TreeAtomizer(new CanonicalizationImpl());
    private final TreeDeatomizer treeDeatomizer = new TreeDeatomizer(treeAtomizer.getTemps());

    @Override
    public void build(TaskRegister taskRegister) {
        taskRegister.register(new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                taskContext.hirFragList.accept(new FragmentVisitor() {
                    @Override
                    public void visit(ProcFrag procFrag) {
                        procFrag.body.accept(treeAtomizer);
                        Stm atomized = treeAtomizer.stm;
                        procFrag.body = atomized;
                    }
                    @Override
                    public void visit(DataFrag dataFrag) {
                        // do nothing.
                    }
                    });
                }
            }, "atomize", "Atomize hir tree", "hir-compute")
        );

        taskRegister.register(new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                taskContext.lirFragList.accept(new FragmentVisitor() {
                    @Override
                    public void visit(ProcFrag procFrag) {
                        procFrag.body.accept(treeDeatomizer);
                        Stm deatomized = treeDeatomizer.stm;
                        Assert.assertNotNull(deatomized, "Deatomized LIR cannot be null");
                        procFrag.body = deatomized;
                    }
                    @Override
                    public void visit(DataFrag dataFrag) {
                        // do nothing.
                    }
                    });
                }
            }, "deatomize", "Deatomize lir tree", "lir-compute")
        );

        taskRegister.register(new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                taskContext.lirFragList.accept(new FragmentVisitor() {

                    @Override
                    public void visit(ProcFrag procFrag) {
                        CFG cfg = new CFG((StmList)procFrag.body);
                        CFGRenderer cfgRenderer = new CFGGraphizRender(cfg);
                        cfgRenderer.write(new PrintStream(taskContext.out));
                    }
                    @Override
                    public void visit(DataFrag dataFrag) {
                        // do nothing.
                    }
                    });
                }
            }, "cfg", "Build cfg", "deatomize")
        );


        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {

                    throw new UnsupportedOperationException();
                }
            }, "common-subexpression-elimination", "Eliminate common subexpressions from quadruples", "hir-compute")
        );
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    throw new UnsupportedOperationException();

                }
            }, "constant-propagation", "Propogate constants expressions", "escapes-compute")
        );
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    throw new UnsupportedOperationException();

                }
            }, "copy-propagation", "Propogate variable expressions", "escapes-compute")
        );
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    throw new UnsupportedOperationException();

                }
            }, "deadcode-elimination", "Remove dead code from quadruples", "escapes-compute")
        );
    }
}