package com.chaosopher.tigerlang.compiler.cloner;

import com.chaosopher.tigerlang.compiler.absyn.AbsynVisitor;
import com.chaosopher.tigerlang.compiler.absyn.ArrayExp;
import com.chaosopher.tigerlang.compiler.absyn.ArrayTy;
import com.chaosopher.tigerlang.compiler.absyn.AssignExp;
import com.chaosopher.tigerlang.compiler.absyn.BreakExp;
import com.chaosopher.tigerlang.compiler.absyn.CallExp;
import com.chaosopher.tigerlang.compiler.absyn.Dec;
import com.chaosopher.tigerlang.compiler.absyn.DecList;
import com.chaosopher.tigerlang.compiler.absyn.Exp;
import com.chaosopher.tigerlang.compiler.absyn.ExpList;
import com.chaosopher.tigerlang.compiler.absyn.FieldExpList;
import com.chaosopher.tigerlang.compiler.absyn.FieldList;
import com.chaosopher.tigerlang.compiler.absyn.FieldVar;
import com.chaosopher.tigerlang.compiler.absyn.ForExp;
import com.chaosopher.tigerlang.compiler.absyn.FunctionDec;
import com.chaosopher.tigerlang.compiler.absyn.IfExp;
import com.chaosopher.tigerlang.compiler.absyn.IntExp;
import com.chaosopher.tigerlang.compiler.absyn.LetExp;
import com.chaosopher.tigerlang.compiler.absyn.NameTy;
import com.chaosopher.tigerlang.compiler.absyn.NilExp;
import com.chaosopher.tigerlang.compiler.absyn.OpExp;
import com.chaosopher.tigerlang.compiler.absyn.RecordExp;
import com.chaosopher.tigerlang.compiler.absyn.RecordTy;
import com.chaosopher.tigerlang.compiler.absyn.SeqExp;
import com.chaosopher.tigerlang.compiler.absyn.SimpleVar;
import com.chaosopher.tigerlang.compiler.absyn.StringExp;
import com.chaosopher.tigerlang.compiler.absyn.SubscriptVar;
import com.chaosopher.tigerlang.compiler.absyn.Ty;
import com.chaosopher.tigerlang.compiler.absyn.TypeDec;
import com.chaosopher.tigerlang.compiler.absyn.Var;
import com.chaosopher.tigerlang.compiler.absyn.VarDec;
import com.chaosopher.tigerlang.compiler.absyn.VarExp;
import com.chaosopher.tigerlang.compiler.absyn.WhileExp;
import com.chaosopher.tigerlang.compiler.util.Assert;

/**
 * Creates a copy of the original AST without bindings or 
 * Type information.
 */
public class AbsynCloner implements AbsynVisitor {

    public Exp visitedExp;
    public DecList visitedDecList;
    protected Ty visitedTy;
    protected Var visitedVar;
    protected Dec visitedDec;

    @Override
    public void visit(ArrayExp exp) {
        exp.size.accept(this);
        Exp sizeExp = this.visitedExp;
        exp.init.accept(this);
        Exp initExp = this.visitedExp;
        this.visitedExp = new ArrayExp(exp.pos, exp.typ, sizeExp, initExp);
    }

    @Override
    public void visit(ArrayTy exp) {
        this.visitedTy = new ArrayTy(exp.pos, exp.typ);
    }

    @Override
    public void visit(AssignExp exp) {
        exp.exp.accept(this);
        Exp expExp = this.visitedExp;
        exp.var.accept(this);
        Var varVar = this.visitedVar; 
        this.visitedExp = new AssignExp(exp.pos, varVar, expExp);
    }

    @Override
    public void visit(BreakExp exp) {
        this.visitedExp = new BreakExp(exp.pos);
    }

    @Override
    public void visit(CallExp exp) {
        ExpList cexpList = null, last = null, temp = null;
        for(ExpList expList = exp.args; expList != null; expList = expList.tail) {
            expList.head.accept(this);
            Exp expListHead = this.visitedExp;
            if(cexpList == null) {
                cexpList = last = new ExpList(expListHead, null);
            } else {
                temp = last;
                last = new ExpList(expListHead, null);
                temp.tail = last;
            }
        }
        this.visitedExp = new CallExp(exp.pos, exp.func, cexpList);
    }

    @Override
    public void visit(DecList exp) {
        DecList last = null, first = null, temp = null;
        for(DecList decList = exp; decList != null; decList = decList.tail) {
            decList.head.accept(this);
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
    public void visit(ExpList exp) {
        Assert.unreachable();
    }

    @Override
    public void visit(FieldExpList exp) {
        Assert.unreachable();
    }

    @Override
    public void visit(FieldList exp) {
        Assert.unreachable();
    }

    @Override
    public void visit(FieldVar exp) {
        exp.var.accept(this);
        Var clonedVar = this.visitedVar;
        this.visitedVar = new FieldVar(exp.pos, clonedVar, exp.field);
    }

    @Override
    public void visit(ForExp exp) {
        exp.var.accept(this);
        VarDec clonedVarDec = (VarDec)this.visitedDec;
        exp.hi.accept(this);
        Exp clonedHiExp = this.visitedExp;
        exp.body.accept(this);
        Exp clonedBodyExp = this.visitedExp;
        this.visitedExp = new ForExp(exp.pos, clonedVarDec, clonedHiExp, clonedBodyExp);
    }

    @Override
    public void visit(FunctionDec exp) {
        FunctionDec clonedFunctionDec = null, temp = null, first = null;
        for(;exp != null; exp = exp.next) {
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

    @Override
    public void visit(IfExp exp) {
        exp.test.accept(this);
        Exp clonedTest = this.visitedExp;
        exp.thenclause.accept(this);
        Exp clonedThen = this.visitedExp;
        Exp clonedElse = null;
        if(exp.elseclause != null) {
            exp.elseclause.accept(this);
            clonedElse = this.visitedExp;
        }
        this.visitedExp = new IfExp(exp.pos, clonedTest, clonedThen, clonedElse);
    }

    @Override
    public void visit(IntExp exp) {
        this.visitedExp = new IntExp(exp.pos, exp.value);
    }

    @Override
    public void visit(LetExp exp) {
        DecList clonedDecList = null, first = null, temp = null;
        if(exp.decs != null) {
            for(DecList decList = exp.decs; decList != null; decList = decList.tail) {
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

    @Override
    public void visit(NameTy exp) {
        this.visitedTy = new NameTy(exp.pos, exp.name);
    }

    @Override
    public void visit(NilExp exp) {
        this.visitedExp = new NilExp(exp.pos);
    }

    @Override
    public void visit(OpExp exp) {
        exp.left.accept(this);
        Exp clonedLeft = this.visitedExp;
        exp.right.accept(this);
        Exp clonedRight = this.visitedExp;
        this.visitedExp = new OpExp(exp.pos, clonedLeft, exp.oper, clonedRight);
    }

    @Override
    public void visit(RecordExp exp) {
        FieldExpList clonedFieldExpList = null, first = null, temp = null;
        for(FieldExpList fieldExpList = exp.fields; fieldExpList != null; fieldExpList = fieldExpList.tail) {
            fieldExpList.init.accept(this);
            Exp clonedInitExp = this.visitedExp;
            if(first == null) {
                first = clonedFieldExpList = new FieldExpList(fieldExpList.pos, fieldExpList.name, clonedInitExp, null);
            } else {
                temp = clonedFieldExpList;
                clonedFieldExpList = new FieldExpList(fieldExpList.pos, fieldExpList.name, clonedInitExp, null);
                temp.tail = clonedFieldExpList;
            }
        }
        this.visitedExp = new RecordExp(exp.pos, exp.typ, first);
    }

    @Override
    public void visit(RecordTy exp) {
        FieldList clonedFields = null, first = null, temp = null;
        for(FieldList fieldList = exp.fields; fieldList != null; fieldList = fieldList.tail) {
            fieldList.typ.accept(this);
            NameTy clonedNameTy = (NameTy)this.visitedTy;
            if(first == null) {
                clonedFields = first = new FieldList(fieldList.pos, fieldList.name, clonedNameTy, null);
            } else {
                temp = clonedFields;
                clonedFields = new FieldList(fieldList.pos, fieldList.name, clonedNameTy, null);
                temp.tail = clonedFields;
            }
        }
        this.visitedTy = new RecordTy(exp.pos, first);
    }

    @Override
    public void visit(SeqExp exp) {
        ExpList clonedExpList = null, first = null, temp = null;
        if(exp.list != null) {
            for(ExpList expList = exp.list; expList != null; expList = expList.tail) {
                expList.head.accept(this);
                Exp clonedHead = this.visitedExp;
                if(first == null) {
                    first = clonedExpList = new ExpList(clonedHead, null);
                } else {
                    temp = clonedExpList;
                    clonedExpList = new ExpList(clonedHead, null);
                    temp.tail = clonedExpList;
                }
            }
        }
        this.visitedExp = new SeqExp(exp.pos, first);

    }

    @Override
    public void visit(SimpleVar exp) {
        this.visitedVar = new SimpleVar(exp.pos, exp.name);
    }

    @Override
    public void visit(StringExp exp) {
        this.visitedExp = new StringExp(exp.pos, exp.value);
    }

    @Override
    public void visit(SubscriptVar exp) {
        exp.var.accept(this);
        Var clonedVar = this.visitedVar;
        exp.index.accept(this);
        Exp clonedIndex = this.visitedExp;
        this.visitedVar = new SubscriptVar(exp.pos, clonedVar, clonedIndex);
    }

    @Override
    public void visit(TypeDec exp) {
        TypeDec clonedTypeDec = null, temp = null, first = null;
        for (; exp != null; exp = exp.next) {
            exp.ty.accept(this);
            Ty clonedTy = this.visitedTy;
            if(first == null) {
                first = clonedTypeDec = new TypeDec(exp.pos, exp.name, clonedTy, null);
            } else {
                temp = clonedTypeDec;
                clonedTypeDec = new TypeDec(exp.pos, exp.name, clonedTy, null);
                temp.next = clonedTypeDec;
            }
        }
        this.visitedDec = first;
    }

    @Override
    public void visit(Var exp) {
        Assert.unreachable();
    }

    @Override
    public void visit(VarDec exp) {
        exp.init.accept(this);
        Exp clonedInitExp = this.visitedExp;
        NameTy clonedNameTy = null; 
        if(exp.typ != null) {
            exp.typ.accept(this);
            clonedNameTy = (NameTy)(this.visitedTy);
        }
        this.visitedDec = new VarDec(exp.pos, exp.name, clonedNameTy, clonedInitExp);
    }

    @Override
    public void visit(VarExp exp) {
        exp.var.accept(this);
        this.visitedExp = new VarExp(exp.pos,this.visitedVar);
    }

    @Override
    public void visit(WhileExp exp) {
        exp.test.accept(this);
        Exp clonedTestExp = this.visitedExp;
        exp.body.accept(this);
        Exp clonedBodyExp = this.visitedExp;
        this.visitedExp = new WhileExp(exp.pos, clonedTestExp, clonedBodyExp);
    }

}
