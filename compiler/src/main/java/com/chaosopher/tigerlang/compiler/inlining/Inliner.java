package com.chaosopher.tigerlang.compiler.inlining;

import java.util.ArrayList;
import java.util.List;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.CallExp;
import com.chaosopher.tigerlang.compiler.absyn.DecList;
import com.chaosopher.tigerlang.compiler.absyn.Exp;
import com.chaosopher.tigerlang.compiler.absyn.ExpList;
import com.chaosopher.tigerlang.compiler.absyn.FieldList;
import com.chaosopher.tigerlang.compiler.absyn.FunctionDec;
import com.chaosopher.tigerlang.compiler.absyn.LetExp;
import com.chaosopher.tigerlang.compiler.absyn.SeqExp;
import com.chaosopher.tigerlang.compiler.absyn.SimpleVar;
import com.chaosopher.tigerlang.compiler.absyn.VarDec;
import com.chaosopher.tigerlang.compiler.absyn.VarExp;
import com.chaosopher.tigerlang.compiler.CallGraphVisitor;
import com.chaosopher.tigerlang.compiler.FunctionCallGraph;
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
        // if function is in cycle or if exp.body is null 
        // this is a primitive which we ignore.
        for(FunctionDec functionDec = exp; functionDec != null; functionDec = functionDec.next) {
            if(functionDec.body == null || this.callGraph.inCycle(functionDec)) {
                ignore.add(functionDec);
            }
        }
        // call super to perform clone.
        super.visit(exp);
    }

    @Override
    public void visit(CallExp exp) {
        FunctionDec functionDec = (FunctionDec)exp.def;
        if(!ignore.contains(functionDec)) {
            ExpList argList = exp.args;
            DecList decList = null, first = null, temp = null;
            for(DecList paramDecList = functionDec.params; paramDecList != null; paramDecList = paramDecList.tail){
                argList.head.accept(this);
                Exp clonedExp = this.visitedExp;
                VarDec fieldList = (VarDec)paramDecList.head;
                VarDec varDec = new VarDec(0, fieldList.name, fieldList.typ, clonedExp/* argument */);
                if(first == null) {
                    first = decList = new DecList(varDec, null);
                } else {
                    temp = decList;
                    decList = new DecList(varDec, null);
                    temp.tail = decList;
                }
                argList = argList.tail;
            }
            //TODO: if the return type is void, we dont need a res.
            functionDec.body.accept(this);
            Exp clonedBody = this.visitedExp;
            VarDec varDec = new VarDec(0, Symbol.symbol("res"), functionDec.result,  clonedBody/* exp */);
            DecList end = first;
            for(;end.tail != null; end = end.tail);
            end.tail = new DecList(varDec, null);
            Exp letExp = new LetExp(0, first, new SeqExp(0, new ExpList(new VarExp(0, new SimpleVar(0, Symbol.symbol("res"))), null)));
            inlinedCount++;
            this.visitedExp = letExp;
        } else {
            super.visit(exp);
        }
    }
}
