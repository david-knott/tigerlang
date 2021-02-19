package com.chaosopher.tigerlang.compiler.intel;

import com.chaosopher.tigerlang.compiler.canon.Canonicalization;
import com.chaosopher.tigerlang.compiler.regalloc.RegAllocFactory;
import com.chaosopher.tigerlang.compiler.translate.FragList;
import com.chaosopher.tigerlang.compiler.util.SimpleTask;
import com.chaosopher.tigerlang.compiler.util.SimpleTaskProvider;
import com.chaosopher.tigerlang.compiler.util.TaskContext;
import com.chaosopher.tigerlang.compiler.util.TaskProvider;
import com.chaosopher.tigerlang.compiler.util.TaskRegister;

/**
 * A collection of tasks related to code generation for the intel x64
 * instruction set. This class will be refactored so the AssemblyCompute visitor
 * will be split into separate modules and tasks ( Canon, Instruction Select,
 * Reg Alloc )
 */
public class Tasks implements TaskProvider {
    final RegAllocFactory regAllocFactory;
    final Canonicalization canonicalization;

    public Tasks(RegAllocFactory regAllocFactory, Canonicalization canonicalization) {
        this.regAllocFactory = regAllocFactory;
        this.canonicalization = canonicalization;
    }

    @Override
    public void build(TaskRegister taskRegister) {
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    FragList lirFragList = taskContext.lirFragList;
                    AssemblyGeneratorVisitor assemblyFragmentVisitor = new AssemblyGeneratorVisitor(new CodeGen());
                    lirFragList.accept(assemblyFragmentVisitor);
                    taskContext.setAssemFragList(assemblyFragmentVisitor.getAssemFragList());
                }
        }, "instr-compute", "Select x64 as target", "optimize" )
        );
        
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    com.chaosopher.tigerlang.compiler.assem.FragList assemblyFragList = taskContext.assemFragList;
                    assemblyFragList.accept(new UnallocatedAssmeblyDump(taskContext.out));

                }
            }, "instr-display", "Dump the unallocated assembly", "instr-compute")
        );

        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    com.chaosopher.tigerlang.compiler.assem.FragList assemblyFragList = taskContext.assemFragList;
                    UnAllocatedAssemblyStats assemblyStats = new UnAllocatedAssemblyStats();
                    assemblyFragList.accept(assemblyStats);
                    assemblyStats.dump(taskContext.out);

                }
            }, "instr-stats", "Display instruction type counts", "instr-compute")
        );
    }
}