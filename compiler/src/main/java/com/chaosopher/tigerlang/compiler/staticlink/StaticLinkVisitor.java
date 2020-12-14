package com.chaosopher.tigerlang.compiler.staticlink;

import java.util.Hashtable;
import java.util.LinkedList;

import com.chaosopher.tigerlang.compiler.absyn.DefaultVisitor;
import com.chaosopher.tigerlang.compiler.absyn.FunctionDec;
import com.chaosopher.tigerlang.compiler.absyn.SimpleVar;
import com.chaosopher.tigerlang.compiler.absyn.VarDec;

/**
 * This class analyses the abstract syntax tree and computes which
 * functions will require a static link. This visitor depends on
 * the Binding visitor. All static linkx are assume to escape.
 */
public class StaticLinkVisitor extends DefaultVisitor {

    private FunctionDec currentFunctionDec;
    private final Hashtable<VarDec, FunctionDec> definingFunctions = new Hashtable<>();
    private final LinkedList<SimpleVar> escapingVars = new LinkedList<>();

    @Override
    public void visit(FunctionDec exp) {
        // record the function dec we are visiting
        FunctionDec parent = this.currentFunctionDec;
        this.currentFunctionDec = exp;
        super.visit(exp);
        // check all escaping vars to see if they are defined here.
        for(int i = 0; i < this.escapingVars.size(); i++) {
            SimpleVar sv = this.escapingVars.get(i);
            VarDec varDec = (VarDec)sv.def;
            if(definingFunctions.get(varDec) == exp) {
                this.escapingVars.remove(sv);
                i--;
            }
        }
        // if there are escaping variables live across this function call, it needs a sl. 
        this.currentFunctionDec.sl = this.escapingVars.size() != 0;
        // restore the parent function dec.
        this.currentFunctionDec = parent;
    }
    
    @Override
    public void visit(VarDec exp) {
        super.visit(exp);
        this.definingFunctions.put(exp, this.currentFunctionDec);
    }
    
    @Override
    public void visit(SimpleVar exp) {
        // does this var escape ?
        // if it escapes, is it defined in the same function dec
        VarDec varDec = (VarDec)exp.def;
        // if variable escapes and is not defined in this function, mark this function
        // as needing static link, add the simple var to list of escaping variables
        if(varDec.escape && this.currentFunctionDec != this.definingFunctions.get(varDec) ) {
            this.currentFunctionDec.sl = true;
            this.escapingVars.add(exp);
        }
        super.visit(exp);
    }
}
