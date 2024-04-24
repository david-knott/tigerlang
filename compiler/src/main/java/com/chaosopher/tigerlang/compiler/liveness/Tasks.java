package com.chaosopher.tigerlang.compiler.liveness;

import java.io.PrintStream;

import com.chaosopher.tigerlang.compiler.assem.DataFrag;
import com.chaosopher.tigerlang.compiler.assem.FragmentVisitor;
import com.chaosopher.tigerlang.compiler.assem.ProcFrag;
import com.chaosopher.tigerlang.compiler.flowgraph.AssemFlowGraph;
import com.chaosopher.tigerlang.compiler.graph.GraphvisRenderer;
import com.chaosopher.tigerlang.compiler.temp.DefaultMap;
import com.chaosopher.tigerlang.compiler.util.SimpleTask;
import com.chaosopher.tigerlang.compiler.util.SimpleTaskProvider;
import com.chaosopher.tigerlang.compiler.util.TaskContext;
import com.chaosopher.tigerlang.compiler.util.TaskProvider;
import com.chaosopher.tigerlang.compiler.util.TaskRegister;

/**
 * F|flowgraph-dump - dump the flow graphs V|liveness-dump - dump the liveness
 * graphs N|interference-dump - dump the interference graphs. all depend on
 * inst-compute ( generate the assembly for target machine )
 */
public class Tasks implements TaskProvider {

    @Override
    public void build(TaskRegister taskRegister) {
        taskRegister.register(new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                GraphvisRenderer graphvisRenderer = new GraphvisRenderer();
                taskContext.assemFragList.accept(new FragmentVisitor() {
                    @Override
                    public void visit(ProcFrag procFrag) {
                        AssemFlowGraph assemFlowGraph = new AssemFlowGraph(procFrag.body);
                        graphvisRenderer.render(new PrintStream(taskContext.out), assemFlowGraph, new DefaultMap());
                    }
                    @Override
                    public void visit(DataFrag dataFrag) {
                        // TODO Auto-generated method stub

                    }
                    }
                );
                }
            }, "F|flowgraph-dump", "dump the flowgraphs", "instr-compute")
        );
    }
    
}
