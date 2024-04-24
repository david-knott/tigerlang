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
import com.chaosopher.tigerlang.compiler.tree.CONST;
import com.chaosopher.tigerlang.compiler.tree.EXPS;
import com.chaosopher.tigerlang.compiler.tree.Exp;
import com.chaosopher.tigerlang.compiler.tree.MEM;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.TEMP;

public class AEGenKillSets extends GenKillSets<Exp> {

    public static GenKillSets<Exp> analyse(final CFG cfg) {
        AEGenKillSets genKillSets = new AEGenKillSets(cfg);
        genKillSets.generate();
        return genKillSets;
    }

    private final HashMap<Temp, Set<Exp>> expUseTemps = new HashMap<>();
    private final Set<Exp> mems = new HashSet<>();
    
    private AEGenKillSets(final CFG cfg) {
        super(cfg);
    }

    private Set<Exp> getExpressionsUsingTemp(final Temp exp) {
        Set<Exp> exps = this.expUseTemps.get(exp);
        return exps;
    }

    private Set<Exp> getExpressionsUsingMem() {
        return this.mems;
    }

    @Override
    protected void initGenSet(Set<Exp> gen, final Stm s) {
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
    protected void initKillSet(Set<Exp> kill, final Stm s) {
        if(s instanceof MOVE) {
            MOVE move = (MOVE)s;
            if(move.dst instanceof TEMP && move.src instanceof CONST) {
                Temp temp = ((TEMP)move.dst).temp;
                kill.addAll(this.getExpressionsUsingTemp(temp));
            }
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
        if(s instanceof EXPS) {
            EXPS exp = (EXPS)s;
            if(exp.exp instanceof CALL) {
                kill.addAll(this.getExpressionsUsingMem());
            }
        }
    }

    @Override
    protected void initialize(final BasicBlock b, final Stm stm) {
        // get all uses and definitions.
        Set<Temp> uses = ExtractUses.getUses(stm);
        Set<Temp> defs = ExtractDefs.getDefs(stm);
        // initialize expUseTemps hashmap with empty sets for all temps we will encounter.
        Set<Temp> all = new HashSet<>();
        all.addAll(uses);
        all.addAll(defs);
        for(Temp temp : all) {
            if(!this.expUseTemps.containsKey(temp)) {
                this.expUseTemps.put(temp, new HashSet<>());
            }
        }
        // if statement contains an expression, map use temp to expression.
        Exp exp = ExtractExp.getExp(stm);
        if(exp != null) {
            for(Temp temp : uses) {
                this.expUseTemps.get(temp).add(exp);
            }
        }
        // if statment contains a memory expression, save reference to it in mems collection.
        boolean isMemExp = ExtractMemExp.isMemExp(stm);
        if(isMemExp) {
            this.mems.add(exp);
        }
    }
}