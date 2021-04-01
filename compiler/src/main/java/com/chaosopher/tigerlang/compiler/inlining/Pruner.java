package com.chaosopher.tigerlang.compiler.inlining;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.Dec;
import com.chaosopher.tigerlang.compiler.absyn.DecList;
import com.chaosopher.tigerlang.compiler.absyn.Exp;
import com.chaosopher.tigerlang.compiler.absyn.FunctionDec;
import com.chaosopher.tigerlang.compiler.absyn.LetExp;
import com.chaosopher.tigerlang.compiler.absyn.NameTy;
import com.chaosopher.tigerlang.compiler.absyn.VarDec;
import com.chaosopher.tigerlang.compiler.callgraph.CallGraphVisitor;
import com.chaosopher.tigerlang.compiler.callgraph.FunctionCallGraph;
import com.chaosopher.tigerlang.compiler.cloner.AbsynCloner;
import com.chaosopher.tigerlang.compiler.graph.Node;

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
                decList.head.accept(this);
                if(this.visitedDec == null) {
                    continue;
                }
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



    @Override
    public void visit(DecList exp) {
        DecList last = null, first = null, temp = null;
        for(DecList decList = exp; decList != null; decList = decList.tail) {
            decList.head.accept(this);
            if(this.visitedDec == null) 
                continue;
            Dec clonedDec = this.visitedDec;
            if(first == null) {
                first = last = new DecList(clonedDec, null);
            } else {
                temp = last;
                last = new DecList(clonedDec, null);
                temp.tail = last;
            }
        }
        this.visitedDecList = first;
    }

    @Override
    public void visit(FunctionDec exp) {
        FunctionDec clonedFunctionDec = null, temp = null, first = null;
        for(;exp != null; exp = exp.next) {

            Node node = callGraph.getNode(exp);
            if(
                (node == null && exp.body != null && !exp.name.toString().equals("tigermain")) // dont prune primitives
                ||
                (node != null && node.pred() == null && !exp.name.toString().equals("tigermain")) // dont prune tigermain
            )
            {
                // if node is no in call graph
                // or node has no pred, then 
                // prune it.
                System.out.println("removing " + exp.name);
                pruneCount++;
                this.visitedDec = null;
                continue;
            }
            Exp clonedBody = null;
            if(exp.body != null) {
                exp.body.accept(this);
                clonedBody = this.visitedExp;
            }
            DecList clonedFieldList = null, temp1 = null, first1 = null;
            for(DecList decList = exp.params; decList != null; decList = decList.tail) {
                VarDec varDec = (VarDec)decList.head;
                varDec.typ.accept(this);
                NameTy clonedFieldListType = (NameTy)this.visitedTy;
                VarDec clonedVarDec = new VarDec(varDec.pos, varDec.name, clonedFieldListType, varDec.init);
                if(first1 == null) {
                    first1 = clonedFieldList = new DecList(clonedVarDec, null);
                } else {
                    temp1 = clonedFieldList;
                    clonedFieldList = new DecList(clonedVarDec, null);
                    temp1.tail = clonedFieldList;
                }
            }
            NameTy clonedReturnType = null;
            if(exp.result != null) {
                exp.result.accept(this);
                clonedReturnType = (NameTy)this.visitedTy;
            }
            if (first == null) {
                first = clonedFunctionDec = new FunctionDec(exp.pos, exp.name, first1, clonedReturnType, clonedBody, null);
            } else {
                temp = clonedFunctionDec;
                clonedFunctionDec = new FunctionDec(exp.pos, exp.name, first1, clonedReturnType, clonedBody, null);
                temp.next = clonedFunctionDec;
            }
        }
        this.visitedDec = first;
    }
}