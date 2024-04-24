package com.chaosopher.tigerlang.compiler.dataflow.live;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.chaosopher.tigerlang.compiler.dataflow.DataflowMeet;
import com.chaosopher.tigerlang.compiler.dataflow.GenKillSets;
import com.chaosopher.tigerlang.compiler.dataflow.ReverseDataFlow;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.frame.Frame;
import com.chaosopher.tigerlang.compiler.graph.Node;
import com.chaosopher.tigerlang.compiler.temp.Temp;

public class Liveness extends ReverseDataFlow<Temp> {

    private Frame frame;

    public static Liveness analyze(CFG cfg, GenKillSets<Temp> genKillSets, Frame frame) {
        Liveness liveness = new Liveness(cfg, genKillSets, frame);
        liveness.generate();
        return liveness;
    }

    protected Liveness(CFG cfg, GenKillSets<Temp> genKillSets, final Frame frame) {
        super(cfg, genKillSets, DataflowMeet.UNION);
        this.frame = frame;
    }

    /**
     * Initializes in and out sets so that special registers rax & rfp are
     * live. 
     */
    @Override
    protected void initialise(CFG cfg, Map<BasicBlock, Set<Temp>> inMap, Map<BasicBlock, Set<Temp>> outMap) {
        for(Node node : cfg.nodes()) {
            BasicBlock b = this.cfg.get(node);
            inMap.put(b, Stream.of(
                    this.frame.RV(),
                    this.frame.FP()
                ).collect(Collectors.toCollection(HashSet::new)));
            outMap.put(b, Stream.of(
                    this.frame.RV(),
                    this.frame.FP()
                ).collect(Collectors.toCollection(HashSet::new)));

        }
    }
}
