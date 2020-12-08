package com.chaosopher.tigerlang.compiler.translate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.chaosopher.tigerlang.compiler.intel.IntelFrame;
import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.tree.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.tree.TreeVisitor;
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
                    taskContext.hirFragList.accept(new FragmentPrinter(taskContext.log));
                }
            }, "hir-display", "Display the HIR", "hir-compute")
        );
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream out = null;
                    try {
                        out = new ObjectOutputStream(bos);   
                        out.writeObject(taskContext.hirFragList);
                        out.flush();
                        byte[] yourBytes = bos.toByteArray();
                        //taskContext.out
                    } catch(Exception ex) {

                    } finally {
                    try {
                        bos.close();
                    } catch (IOException ex) {
                        // ignore close exception
                    }
                    }


                    taskContext.hirFragList.accept(new FragmentPrinter(taskContext.log));
                }
            }, "serialize", "Serialize the HIR", "hir-compute")
        );
        /*
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    Semant.Semant semant = new Semant.Semant(taskContext.errorMsg, new Translator());
                    taskContext.setFragList(semant.getTreeFragments(taskContext.decList));
                }
            }, "hir-compute", "Translate abstract syntax to HIR", "typed")
        );
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    TreeVisitor prettyPrinter = new PrettyPrinter(taskContext.log);
                    taskContext.hirFragList.accept(new FragmentVisitor(){

                        @Override
                        public void visit(ProcFrag procFrag) {
                            procFrag.body.accept(prettyPrinter);
                        }

                        @Override
                        public void visit(DataFrag dataFrag) {
                        }
                    });
                }
            }, "hir-display", "Display the HIR", "hir-compute")
        );
        */

    }
}