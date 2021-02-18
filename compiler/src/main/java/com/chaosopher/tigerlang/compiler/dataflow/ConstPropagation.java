package com.chaosopher.tigerlang.compiler.dataflow;

import java.util.BitSet;
import java.util.HashMap;

import com.chaosopher.tigerlang.compiler.tree.BINOP;
import com.chaosopher.tigerlang.compiler.tree.CONST;
import com.chaosopher.tigerlang.compiler.tree.CloningTreeVisitor;
import com.chaosopher.tigerlang.compiler.tree.Exp;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.TEMP;
import com.chaosopher.tigerlang.compiler.util.Assert;

class ConstPropagation extends CloningTreeVisitor {
    
    private final GenKillSets genKillSets;
    private final BitSet conDefs = new BitSet();
    private final HashMap<Integer, Integer> constants = new HashMap<>();
    private Stm currentMove;
    
    public ConstPropagation(GenKillSets genKillSets) {
        this.genKillSets = genKillSets;
    }

    @Override
    public void visit(MOVE op) {
        // look for definitions of form a <- const
        if(op.src instanceof CONST) {
            // get definition id for const definition
            int defId = this.genKillSets.getDefinitionId(op);
            // add defId to constant defs set
            conDefs.set(defId);
            // get the constant value
            Integer val = ((CONST)op.src).value;
            // sort a map of definition id -> value,
            // this is used for the actual rewrite
            constants.put(defId, val);
        }
        // store the current move, this is
        // used to get the in set if we
        // visit a binop.
        this.currentMove = op;
        super.visit(op);
    }

    private Exp rewrite(Exp exp) {
        if (exp instanceof TEMP) {
            BitSet reachableIn = (BitSet) (this.genKillSets.getIn(this.currentMove).clone());
            TEMP leftTemp = (TEMP) exp;
            BitSet tempDefs = this.genKillSets.getDefinitions(leftTemp.temp);
            Assert.assertNotNull(tempDefs, "Definitions for temp should not be null");
            reachableIn.and(tempDefs);
            reachableIn.and(this.conDefs);
            if (!reachableIn.isEmpty()) {
                System.out.println("Exp is constant:" + exp + " " + this.constants.get(reachableIn.nextSetBit(0)));
                return new CONST(this.constants.get(reachableIn.nextSetBit(0)));
            }
        }
        exp.accept(this);
        return this.exp;
    }

    @Override
    public void visit(BINOP op) {
        Exp leftClone, rightClone;
        // find what is reachable-in for the parent statement
        // if set of defs live at in
        // AND set of associated defs for temp left or right 
        // AND set of const defs is not empty
        // Get the first set bit in the resultant set
        // lookup its constant value and rewrite.

        /*
        if(op.left instanceof TEMP) {
            BitSet reachableIn = (BitSet)(this.genKillSets.getIn(this.currentMove).clone());
            TEMP leftTemp = (TEMP)op.left;
            BitSet tempDefs = this.genKillSets.getDefinitions(leftTemp.temp);
            reachableIn.and(tempDefs);
            reachableIn.and(this.conDefs);
            if(!reachableIn.isEmpty()) {
                System.out.println("Op Left is constant:" + op.left + " " + this.constants.get(reachableIn.nextSetBit(0)));
                leftClone = new CONST(this.constants.get(reachableIn.nextSetBit(0)));
            } else {
                op.left.accept(this);
                leftClone = this.exp;
            }
        } else {
            op.left.accept(this);
            leftClone = this.exp;
        }*/
        leftClone = this.rewrite(op.left);
        /*
        if(op.right instanceof TEMP) {
            BitSet reachableIn = (BitSet)(this.genKillSets.getIn(this.currentMove).clone());
            TEMP rightTemp = (TEMP)op.right;
            BitSet tempDefs = this.genKillSets.getDefinitions(rightTemp.temp);
            reachableIn.and(tempDefs);
            reachableIn.and(this.conDefs);
            if(!reachableIn.isEmpty()) {
                System.out.println("Op right is constant:" + op.right + " " + this.constants.get(reachableIn.nextSetBit(0)));
                rightClone = new CONST(this.constants.get(reachableIn.nextSetBit(0)));
            } else {
                op.right.accept(this);
                rightClone = this.exp;
            }
        } else {
            op.right.accept(this);
            rightClone = this.exp;
        }*/
        rightClone = this.rewrite(op.right);
        this.exp = new BINOP(op.binop, leftClone, rightClone);
    }
}