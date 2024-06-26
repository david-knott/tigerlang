package com.chaosopher.tigerlang.compiler.dataflow.exp;

import java.util.Collection;
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

class REGenKillSets extends GenKillSets<Integer> {

    public static GenKillSets<Integer> analyse(final CFG cfg) {
        REGenKillSets genKillSets = new REGenKillSets(cfg);
        genKillSets.generate();
        return genKillSets;
    }

    private final HashMap<Exp, Set<Integer>> expDefMap = new HashMap<>();
    private final HashMap<Temp, Set<Integer>> defIdUseTemps = new HashMap<>();
    private final Set<Integer> mems = new HashSet<>();
    
    public REGenKillSets(CFG cfg) {
        super(cfg);
    }

    @Override
    protected void initGenSet(Set<Integer> gen, Stm s) {
        Integer defId = this.getDefinitionId(s);
        // s1: t1 = a + c | gen: {s1} - kill(s1)

        // s1: t1 = M(a)  | gen {s1} - kill(s1) 
        if(s instanceof MOVE) {
            MOVE move = (MOVE)s;
            if(move.dst instanceof TEMP && move.src instanceof BINOP) {
                gen.add(defId);
                gen.removeAll(this.getKill(s));
          //      this.defMap.put(defId, move.src);
            //    this.revDefMap.put(move.src, defId);
            }
            if(move.dst instanceof TEMP && move.src instanceof MEM) {
                gen.add(defId);
                gen.removeAll(this.getKill(s));
             ///   this.defMap.put(defId, move.src);
                //this.revDefMap.put(move.src, defId);
            }
        }
    }

    private void addRecomputationDefs(Exp src, Integer defId, Set<Integer> kill) {
        Set<Integer> recompute = new HashSet<>();
        recompute.addAll(this.expDefMap.get(src));
        recompute.remove(defId);
        kill.addAll(recompute);
    }

    @Override
    protected void initKillSet(Set<Integer> kill, Stm s) {
        // s1: t1 = a + c | kill all defs that use t1 or recompute a + c

        // s1: t1 = M(a)  | kill all defs that

        Integer defId = this.getDefinitionId(s);

        if(s instanceof MOVE) {
            MOVE move = (MOVE)s;
            if(move.dst instanceof TEMP && move.src instanceof CONST) {
                Temp temp = ((TEMP)move.dst).temp;
                kill.addAll(this.getExpressionsUsingTemp(temp));
                this.addRecomputationDefs(move.src, defId, kill);
            }
            if(move.dst instanceof TEMP && move.src instanceof BINOP) {
                Temp temp = ((TEMP)move.dst).temp;
                kill.addAll(this.getExpressionsUsingTemp(temp));
                this.addRecomputationDefs(move.src, defId, kill);
            }
            if(move.dst instanceof TEMP && move.src instanceof MEM) {
                Temp temp = ((TEMP)move.dst).temp;
                kill.addAll(this.getExpressionsUsingTemp(temp));
                this.addRecomputationDefs(move.src, defId, kill);
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

    private Collection<? extends Integer> getExpressionsUsingMem() {
        return this.mems;
    }

    private Collection<? extends Integer> getExpressionsUsingTemp(Temp temp) {
        return this.defIdUseTemps.get(temp);
    }

    @Override
    protected void initialize(BasicBlock b, Stm stm) {
        // get all uses and definitions.
        Set<Temp> uses = ExtractUses.getUses(stm);
        Set<Temp> defs = ExtractDefs.getDefs(stm);
        Integer defId = this.getDefinitionId(stm);
        // initialize expUseTemps hashmap with empty sets for all temps we will encounter.
        Set<Temp> all = new HashSet<>();
        all.addAll(uses);
        all.addAll(defs);
        for(Temp temp : all) {
            if(!this.defIdUseTemps.containsKey(temp)) {
                this.defIdUseTemps.put(temp, new HashSet<>());
            }
        }
        // if statement contains an expression, map temp to definition id.
        Exp exp = ExtractExp.getExp(stm);
        if(exp != null) {

           if(!this.expDefMap.containsKey(exp)) {
               this.expDefMap.put(exp, new HashSet<>());
           }
           this.expDefMap.get(exp).add(defId);

            for(Temp temp : uses) {
                this.defIdUseTemps.get(temp).add(defId);
            }
        }
        // if statment contains a memory expression, save reference to defintion id.
        boolean isMemExp = ExtractMemExp.isMemExp(stm);
        if(isMemExp) {
            this.mems.add(defId);
        }
        
    }
}