package com.chaosopher.tigerlang.compiler.dataflow;

import java.util.BitSet;
import java.util.HashMap;

import com.chaosopher.tigerlang.compiler.tree.BINOP;
import com.chaosopher.tigerlang.compiler.tree.CJUMP;
import com.chaosopher.tigerlang.compiler.tree.CONST;
import com.chaosopher.tigerlang.compiler.tree.CloningTreeVisitor;
import com.chaosopher.tigerlang.compiler.tree.EXP;
import com.chaosopher.tigerlang.compiler.tree.Exp;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.TEMP;
import com.chaosopher.tigerlang.compiler.util.Assert;

class ConstPropagation extends CloningTreeVisitor {
    
    private final GenKillSets genKillSets;
    private final BitSet constantDefintiions = new BitSet();
    private final HashMap<Integer, Integer> constants = new HashMap<>();
    private Stm currentStatement;
    
    public ConstPropagation(GenKillSets genKillSets) {
        this.genKillSets = genKillSets;
    }

    @Override
    public void visit(MOVE op) {
        this.currentStatement = op;
        // look for definitions of form a <- const
        if(op.src instanceof CONST) {
            Integer val = ((CONST)op.src).value;
            Integer defId = this.genKillSets.getDefinitionId(op);
            Assert.assertNotNull(defId, "No definition found for move: " + op.dst + " <- " + val);
            constantDefintiions.set(defId);
            constants.put(defId, val);
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

    public void visit(EXP op) {
        this.currentStatement = op;
        Exp expClone = this.rewrite(op.exp);
        this.stm = new EXP(expClone);
    }

    private Exp rewrite(Exp exp) {
        if (exp instanceof TEMP) {
            BitSet reachableIn = (BitSet) (this.genKillSets.getIn(this.currentStatement).clone()); // all definitions that reach this statement,
            TEMP leftTemp = (TEMP) exp; 
            BitSet tempDefs = this.genKillSets.getDefinitions(leftTemp.temp); // all definitions for temp that may be replaced.
            if(tempDefs != null) {
                reachableIn.and(tempDefs); // all definitions for temp that reach this statement.
                if(reachableIn.cardinality() == 1) { //only one def for this temp that reaches this statement
                    reachableIn.and(this.constantDefintiions);
                    if(reachableIn.cardinality() == 1) { //only one def for temp that reaches this statement and is a constant.
                        Integer t = this.constants.get(reachableIn.nextSetBit(0));
                        return new CONST(t);
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