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
        if(op.src instanceof CONST) {
            int defId = this.genKillSets.getDefinitionId(op);
            conDefs.set(defId);
            Integer val = ((CONST)op.src).value;
            constants.put(defId, val);
        }
        this.currentMove = op;
        super.visit(op);
    }

    @Override
    public void visit(BINOP op) {
        Exp leftClone, rightClone;
        // what is reachable in for this statement
        // if set of defs live at 
        
        // AND set of defs for temp 
        
        // AND set of const defs is not empty
        
        // then propgate.
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
        }
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
        }
        this.exp = new BINOP(op.binop, leftClone, rightClone);
    }
}