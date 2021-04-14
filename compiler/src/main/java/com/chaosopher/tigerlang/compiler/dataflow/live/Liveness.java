package com.chaosopher.tigerlang.compiler.dataflow.live;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.chaosopher.tigerlang.compiler.dataflow.DataflowMeet;
import com.chaosopher.tigerlang.compiler.dataflow.GenKillSets;
import com.chaosopher.tigerlang.compiler.dataflow.ReverseDataFlow;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.graph.Node;
import com.chaosopher.tigerlang.compiler.temp.Temp;

public class Liveness extends ReverseDataFlow<Temp> {

    public static Liveness analyze(CFG cfg, GenKillSets<Temp> genKillSets) {
        Liveness liveness = new Liveness(cfg, genKillSets);
        liveness.generate();
        return liveness;
    }

    protected Liveness(CFG cfg, GenKillSets<Temp> genKillSets) {
        super(cfg, genKillSets, DataflowMeet.UNION);
    }

    @Override
    protected void initialise(CFG cfg, Map<BasicBlock, Set<Temp>> inMap, Map<BasicBlock, Set<Temp>> outMap) {
        for(Node node : cfg.nodes()) {
            BasicBlock b = this.cfg.get(node);
            inMap.put(b, new HashSet<>());
            outMap.put(b, new HashSet<>());
        }
    }
}
