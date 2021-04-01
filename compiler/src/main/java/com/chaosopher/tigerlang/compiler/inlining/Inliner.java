package com.chaosopher.tigerlang.compiler.inlining;

import java.util.ArrayList;
import java.util.List;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.CallExp;
import com.chaosopher.tigerlang.compiler.absyn.DecList;
import com.chaosopher.tigerlang.compiler.absyn.Exp;
import com.chaosopher.tigerlang.compiler.absyn.ExpList;
import com.chaosopher.tigerlang.compiler.absyn.FunctionDec;
import com.chaosopher.tigerlang.compiler.absyn.LetExp;
import com.chaosopher.tigerlang.compiler.absyn.SeqExp;
import com.chaosopher.tigerlang.compiler.absyn.SimpleVar;
import com.chaosopher.tigerlang.compiler.absyn.VarDec;
import com.chaosopher.tigerlang.compiler.absyn.VarExp;
import com.chaosopher.tigerlang.compiler.callgraph.CallGraphVisitor;
import com.chaosopher.tigerlang.compiler.callgraph.FunctionCallGraph;
import com.chaosopher.tigerlang.compiler.cloner.AbsynCloner;
import com.chaosopher.tigerlang.compiler.symbol.Symbol;

/**
 * Identifies functions that are only called once and
 * inlines them into the call site.
 */
public class Inliner extends AbsynCloner {
    
    List<FunctionDec> ignore = new ArrayList<FunctionDec>();
    FunctionCallGraph callGraph;
    public int inlinedCount;

    public Inliner(Absyn absyn) {
        CallGraphVisitor callGraphVisitor = new CallGraphVisitor();
        absyn.accept(callGraphVisitor);
        this.callGraph = callGraphVisitor.functionCallGraph;
    }

    @Override
    public void visit(FunctionDec exp) {
        // Do not inline if 
        // 1) function is in cycle / recursive
        // 2) function is a primitive
        // 3) function is tigermain
        for(FunctionDec functionDec = exp; functionDec != null; functionDec = functionDec.next) {
            if(
                functionDec.body == null 
                || functionDec.name.toString().equals("tigermain") /* must go before next statement or we get errors */
                || this.callGraph.getNode(functionDec) == null
                || this.callGraph.inCycle(functionDec)
                ) {
                ignore.add(functionDec);
            }
        }
        super.visit(exp);
    }

    private LetExp rewrite(CallExp callExp) {
        FunctionDec functionDec = (FunctionDec)callExp.def;
        // translate call actual arguments into a declist with variable declarations.
        ExpList argList = callExp.args;
        DecList decList = null, first = null, temp = null;
        for(DecList paramDecList = functionDec.params; paramDecList != null; paramDecList = paramDecList.tail){
            argList.head.accept(this);
            Exp clonedExp = this.visitedExp;
            VarDec fieldList = (VarDec)paramDecList.head;
            VarDec varDec = new VarDec(0, fieldList.name, fieldList.typ, clonedExp);
            if(first == null) {
                first = decList = new DecList(varDec, null);
            } else {
                temp = decList;
                decList = new DecList(varDec, null);
                temp.tail = decList;
            }
            argList = argList.tail;
        }
        // clone the function body.
        functionDec.body.accept(this);
        Exp clonedBody = this.visitedExp;
        Exp letBody = null;
        // if function return type is VOID ( == null), then evaluate the function
        // body in the let body. If function return type is not VOID, then evaluate
        // the function body into a new variable called res, which is added to the 
        // end of the declist.
        if(functionDec.result == null) {
            letBody = clonedBody;
        } else {
            VarDec varDec = new VarDec(0, Symbol.symbol("res"), functionDec.result,  clonedBody);
            DecList end = first;
            if(end != null) {
                for(;end.tail != null; end = end.tail);
                end.tail = new DecList(varDec, null);
            } else {
                first = new DecList(varDec, null);
            }
            letBody = new SeqExp(0, new ExpList(new VarExp(0, new SimpleVar(0, Symbol.symbol("res"))), null));
        }
        return new LetExp(0, first, letBody);
    }

    @Override
    public void visit(CallExp exp) {
        FunctionDec functionDec = (FunctionDec)exp.def;
        if(!ignore.contains(functionDec)) {
            System.out.println("inlining function " + functionDec.name);
            inlinedCount++;
            this.visitedExp = this.rewrite(exp);
        } else {
            super.visit(exp);
        }
    }
}
