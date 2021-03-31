package com.chaosopher.tigerlang.compiler.dataflow.exp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.chaosopher.tigerlang.compiler.dataflow.GenKillSets;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.dataflow.utils.ExtractDefs;
import com.chaosopher.tigerlang.compiler.dataflow.utils.ExtractExp;
import com.chaosopher.tigerlang.compiler.dataflow.utils.ExtractMemExp;
import com.chaosopher.tigerlang.compiler.dataflow.utils.ExtractUses;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.BINOP;
import com.chaosopher.tigerlang.compiler.tree.CALL;
import com.chaosopher.tigerlang.compiler.tree.EXP;
import com.chaosopher.tigerlang.compiler.tree.Exp;
import com.chaosopher.tigerlang.compiler.tree.MEM;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.TEMP;

class AEGenKillSets extends GenKillSets<Exp> {

    public static GenKillSets<Exp> analyse(CFG cfg) {
        AEGenKillSets genKillSets = new AEGenKillSets(cfg);
        genKillSets.generate();
        return genKillSets;
    }

    private final HashMap<Temp, Set<Exp>> defTemps = new HashMap<>();
    private final Set<Exp> mems = new HashSet<>();
    
    private AEGenKillSets(CFG cfg) {
        super(cfg);
    }

    private Set<Exp> getExpressionsUsingTemp(Temp exp) {
        Set<Exp> exps = this.defTemps.get(exp);
        return exps;
    }

    private Set<Exp> getExpressionsUsingMem() {
        return this.mems;
    }

    @Override
    protected void initGenSet(Set<Exp> gen, Stm s) {
        if(s instanceof MOVE) {
            MOVE move = (MOVE)s;
            if(move.dst instanceof TEMP && move.src instanceof BINOP) {
                gen.add(move.src);
                gen.removeAll(this.getKill(s));
            }
            if(move.dst instanceof TEMP && move.src instanceof MEM) {
                gen.add(move.src);
                gen.removeAll(this.getKill(s));
            }
        }
    }

    @Override
    protected void initKillSet(Set<Exp> kill, Stm s) {
        if(s instanceof MOVE) {
            MOVE move = (MOVE)s;
            if(move.dst instanceof TEMP && move.src instanceof BINOP) {
                Temp temp = ((TEMP)move.dst).temp;
                kill.addAll(this.getExpressionsUsingTemp(temp));
            }
            if(move.dst instanceof TEMP && move.src instanceof MEM) {
                Temp temp = ((TEMP)move.dst).temp;
                kill.addAll(this.getExpressionsUsingTemp(temp));
            }
            if(move.dst instanceof MEM && move.src instanceof TEMP ){   
                kill.addAll(this.getExpressionsUsingMem());
            }
            if(move.dst instanceof TEMP && move.src instanceof CALL) {
                Temp temp = ((TEMP)move.dst).temp;
                kill.addAll(this.getExpressionsUsingMem());
                kill.addAll(this.getExpressionsUsingTemp(temp));
            }
        }
        if(s instanceof EXP) {
            EXP exp = (EXP)s;
            if(exp.exp instanceof CALL) {
                kill.addAll(this.getExpressionsUsingMem());
            }
        }
    }

    @Override
    protected void initialize(BasicBlock b, Stm stm) {
        Set<Temp> uses = ExtractUses.getUses(stm);
        Set<Temp> defs = ExtractDefs.getDefs(stm);
        // initialize hashmap with empty sets for all temps we will encounter.
        Set<Temp> all = new HashSet<>();
        all.addAll(uses);
        all.addAll(defs);
        for(Temp temp : all) {
            if(!this.defTemps.containsKey(temp)) {
                this.defTemps.put(temp, new HashSet<>());
            }
        }
        // if stm contains an expression, map use temp to expression.
        Exp exp = ExtractExp.getExp(stm);
        if(exp != null) {
            for(Temp temp : uses) {
                this.defTemps.get(temp).add(exp);
            }
        }
        // if statment contains a memory expression, save reference to it.
        boolean isMemExp = ExtractMemExp.isMemExp(stm);
        if(isMemExp) {
            this.mems.add(exp);
        }
    }
}