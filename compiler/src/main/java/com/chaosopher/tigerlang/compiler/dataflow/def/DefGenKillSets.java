package com.chaosopher.tigerlang.compiler.dataflow.def;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.chaosopher.tigerlang.compiler.dataflow.GenKillSets;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.TEMP;

public class DefGenKillSets extends GenKillSets<Integer> {

    public static GenKillSets<Integer> analyse(CFG cfg) {
        DefGenKillSets genKillSets = new DefGenKillSets(cfg);
        genKillSets.generate();
        return genKillSets;
    }

    /**
     * Stores a map of tempories a set of definitition id's where they are defined.
     */
    private final HashMap<Temp, Set<Integer>> defTemps = new HashMap<>();

    private DefGenKillSets(CFG cfg) {
        super(cfg);
    }

    private boolean isDefinition(Stm stm) {
        return (stm instanceof MOVE && ((MOVE)stm).dst instanceof TEMP);
    }

    @Override
    protected void initGenSet(Set<Integer> genBlock, Stm s) {
        if(this.isDefinition(s)) {
            MOVE move = (MOVE)s;
            // get destination temp.
            Temp temp = ((TEMP)move.dst).temp;
            // get the defintion id for the statement.
            Integer defId = this.getDefinitionId(s);
            // remove any previous definition of gen from gen set
            genBlock.removeAll(defTemps.get(temp));
            // add defId to gen set for this block.
            genBlock.add(defId);
            // add key temp -> [defId], used in kill phase.
            this.defTemps.get(temp).add(defId);
            // add refernce to gen map for statements
        }
    }

    @Override
    protected void initKillSet(Set<Integer> killBlock, Stm stm) {
        if(this.isDefinition(stm)) {
            MOVE move = (MOVE) stm;// get destination temp.
            Temp temp = ((TEMP) move.dst).temp;
            Integer defId = this.getDefinitionId(stm);
            // all other definitions except this are killed
            Set<Integer> redefinitions = (Set<Integer>)this.defTemps.get(temp);
            killBlock.addAll(redefinitions);
            killBlock.remove(defId);
        }
    }

    @Override
    protected void initialize(BasicBlock b, Stm s) {
        if(this.isDefinition(s)) {
            MOVE move = (MOVE)s;
            // get destination temp.
            Temp temp = ((TEMP)move.dst).temp;
            // add reference for temp and def, used in kill phase.
            this.defTemps.put(temp, new HashSet<Integer>());
        }
    }
}
