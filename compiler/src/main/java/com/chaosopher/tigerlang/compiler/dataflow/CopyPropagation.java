package com.chaosopher.tigerlang.compiler.dataflow;

import java.util.BitSet;
import java.util.HashMap;

import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.BINOP;
import com.chaosopher.tigerlang.compiler.tree.CJUMP;
import com.chaosopher.tigerlang.compiler.tree.CloningTreeVisitor;
import com.chaosopher.tigerlang.compiler.tree.EXP;
import com.chaosopher.tigerlang.compiler.tree.Exp;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.TEMP;

public class CopyPropagation extends CloningTreeVisitor {
    
    private final GenKillSets genKillSets;
    private final BitSet variableDefinitionns = new BitSet();
    private final HashMap<Integer, Temp> variables = new HashMap<>();
    private final HashMap<Temp, Stm> variableDefIds = new HashMap<>();
    private Stm currentStatement;
    
    public CopyPropagation(GenKillSets genKillSets) {
        this.genKillSets = genKillSets;
    }

    @Override
    public void visit(MOVE op) {
        this.currentStatement = op;
        // look for definitions of form a <- b, where b is a temp.
        if(op.src instanceof TEMP) {
            // get definition id for const definition
            int defId = this.genKillSets.getDefinitionId(op);
            // add defId to constant defs set
            variableDefinitionns.set(defId);
            // get the constant value
            Temp val = ((TEMP)op.src).temp;
            variables.put(defId, val);
            variableDefIds.put(val, op);

        } else {
            Exp clonedSrc = this.rewrite(op.src);
            op.dst.accept(this);
            Exp clonedDst = this.exp;
            this.stm = new MOVE(clonedDst, clonedSrc);
            return;
        }
        super.visit(op);
    }

    
    @Override
    public void visit(CJUMP cjump) {
        this.currentStatement = cjump;
        Exp leftClone = this.rewrite(cjump.left);
        Exp rightClone = this.rewrite(cjump.right);
        this.stm = new CJUMP(cjump.relop, leftClone, rightClone, cjump.iftrue, cjump.iffalse);
    }

    @Override
    public void visit(EXP op) {
        this.currentStatement = op;
        Exp expClone = this.rewrite(op.exp);
        this.stm = new EXP(expClone);
    }

    private Exp rewrite(Exp exp) {
        // t16 <- 3
        // t17 <- t16
        // t16 + t17 
        // c <- a + d => c <- b + d
        if (exp instanceof TEMP) {
            // get all definitions that reach this expressions statement.
            BitSet reachableIn = (BitSet) (this.genKillSets.getIn(this.currentStatement).clone()); 
            // all definitions that reach this statement,
            TEMP target = (TEMP) exp; 
            // we want to replace exp with the 
            BitSet targetDefs = this.genKillSets.getDefinitions(target.temp); 
            // all definitions for temp that may be replaced.
            if(targetDefs != null) {
                // find all definitions for temp that reach here.
                reachableIn.and(targetDefs); 
                // all definitions for temp that reach this statement.
                if(reachableIn.cardinality() == 1) { 
                    //if only one def for this temp that reaches this statement
                    reachableIn.and(this.variableDefinitionns); 
                    // and the one temp is also in in variables definitions.
                    if(reachableIn.cardinality() == 1) { 
                        //only one def for temp that reaches this statement and is a constant.
                        Temp replacement = this.variables.get(reachableIn.nextSetBit(0));
                        // get all the definitions of the potential replacement.
                        BitSet replacementDefs = this.genKillSets.getDefinitions(replacement);
                        // if there are any definitions of potential replace between
                        // the current statement and site where target is defined.
                        
                        // get in MINUS out, this should not contain replacement temp.
                        /*
                        BitSet defOut = this.genKillSets.getOut(this.variableDefIds.get(replacement));
                        System.out.println("defOut:" + defOut);
                        BitSet targetOut = (BitSet)this.genKillSets.getOut(this.currentStatement).clone();
                        System.out.println("targetOut:" + targetOut);
                        targetOut.andNot(defOut);
                        System.out.println("minus:" + targetOut);
                        System.out.println("defs of replace " + replacementDefs);

                        */
                        System.out.println("rewriting " + this.currentStatement);
                        System.out.println("=>" + target + " with " + replacement);
                   //     return new TEMP(replacement);
                    }
                }
            }
        }
        exp.accept(this);
        return this.exp;
    }

    @Override
    public void visit(BINOP op) {
        Exp leftClone, rightClone;
        leftClone = this.rewrite(op.left);
        rightClone = this.rewrite(op.right);
        this.exp = new BINOP(op.binop, leftClone, rightClone);
    }
}
