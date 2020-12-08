package com.chaosopher.tigerlang.compiler.inlining;

import java.util.ArrayList;
import java.util.List;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.CallExp;
import com.chaosopher.tigerlang.compiler.absyn.Dec;
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
import com.chaosopher.tigerlang.compiler.callgraph.CallGraphVisitor;
import com.chaosopher.tigerlang.compiler.callgraph.FunctionCallGraph;
import com.chaosopher.tigerlang.compiler.cloner.AbsynCloner;
import com.chaosopher.tigerlang.compiler.graph.Node;
import com.chaosopher.tigerlang.compiler.symbol.Symbol;


import com.chaosopher.tigerlang.compiler.cloner.AbsynCloner;

public class Pruner extends AbsynCloner {

    FunctionCallGraph callGraph;
    public int pruneCount = 0;

    public Pruner(Absyn absyn) {
        CallGraphVisitor callGraphVisitor = new CallGraphVisitor();
        absyn.accept(callGraphVisitor);
        this.callGraph = callGraphVisitor.functionCallGraph;
    }

    @Override
    public void visit(LetExp exp) {
        DecList clonedDecList = null, first = null, temp = null;
        if(exp.decs != null) {
            for(DecList decList = exp.decs; decList != null; decList = decList.tail) {
                if(decList.head instanceof FunctionDec){
                    Node node = callGraph.getNode((FunctionDec)decList.head);
                    if(node == null)  {
                        pruneCount++;
                        continue;
                    }
                } 
                decList.head.accept(this);
                Dec clonedDec = this.visitedDec;
                if(first == null) {
                    first = clonedDecList = new DecList(clonedDec, null);
                } else {
                    temp = clonedDecList;
                    clonedDecList = new DecList(clonedDec, null);
                    temp.tail = clonedDecList;
                }
            }
        }
        Exp clonedBody = null;
        if(exp.body != null) {
            exp.body.accept(this);
            clonedBody = this.visitedExp;
        }
        this.visitedExp = new LetExp(exp.pos, first, clonedBody);
    }
}