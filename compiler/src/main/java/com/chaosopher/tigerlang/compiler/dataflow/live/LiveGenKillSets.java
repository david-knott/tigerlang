package com.chaosopher.tigerlang.compiler.dataflow.live;

import java.util.Set;

import com.chaosopher.tigerlang.compiler.dataflow.GenKillSets;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.dataflow.utils.ExtractDefs;
import com.chaosopher.tigerlang.compiler.dataflow.utils.ExtractUses;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.Stm;

public class LiveGenKillSets extends GenKillSets<Temp> {

    public static GenKillSets<Temp> analyse(CFG cfg) {
        LiveGenKillSets genKillSets = new LiveGenKillSets(cfg);
        genKillSets.generate();
        return genKillSets;
    }

    private LiveGenKillSets(CFG cfg) {
        super(cfg);
    }

    @Override
    protected void initGenSet(Set<Temp> gen, Stm s) {
        // add new gens to to set.
        gen.addAll(ExtractUses.getUses(s));
        // kill any previous items
        gen.removeAll(this.getKill(s));
    }

    @Override
    protected void initKillSet(Set<Temp> kill, Stm s) {
        Set<Temp> defs = ExtractDefs.getDefs(s);
        kill.addAll(defs);
    }

    @Override
    protected void initialize(BasicBlock b, Stm s) {
        // do nothing.
    }
}