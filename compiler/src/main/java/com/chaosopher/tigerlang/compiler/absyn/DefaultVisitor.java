package com.chaosopher.tigerlang.compiler.absyn;

public class DefaultVisitor implements AbsynVisitor{

    @Override
    public void visit(ArrayExp exp) {
        exp.init.accept(this);
        exp.size.accept(this);
    }

    @Override
    public void visit(ArrayTy exp) {
    }

    @Override
    public void visit(AssignExp exp) {
        exp.exp.accept(this);
        exp.var.accept(this);
    }

    @Override
    public void visit(BreakExp exp) {
    }

    @Override
    public void visit(CallExp exp) {
        if(exp.args != null) {
            exp.args.accept(this);
        }
    }

    @Override
    public void visit(DecList exp) {
        for(; exp != null; exp = exp.tail) {
            exp.head.accept(this);
        }
    }

    @Override
    public void visit(ExpList exp) {
        for(; exp != null; exp = exp.tail) {
            exp.head.accept(this);
        }
    }

    @Override
    public void visit(FieldExpList exp) {
        for(; exp != null; exp = exp.tail) {
            exp.init.accept(this);
        }
    }

    @Override
    public void visit(FieldList exp) {
    }

    @Override
    public void visit(FieldVar exp) {
        exp.var.accept(this);
    }

    @Override
    public void visit(ForExp exp) {
        exp.var.accept(this);
        exp.body.accept(this);
        exp.hi.accept(this);
    }

    @Override
    public void visit(FunctionDec exp) {
        if(exp.params != null) {
            exp.params.accept(this);
        }
        if(exp.result != null) {
            exp.result.accept(this);
        }
        if(exp.body != null) {
            exp.body.accept(this);
        }
        if(exp.next != null) {
            exp.next.accept(this);
        }
    }

    @Override
    public void visit(IfExp exp) {
        exp.test.accept(this);
        exp.thenclause.accept(this);
        if(exp.elseclause != null) {
            exp.elseclause.accept(this);
        }
    }

    @Override
    public void visit(IntExp exp) {
    }

    @Override
    public void visit(LetExp exp) {
        if(exp.decs != null) {
            exp.decs.accept(this);
        }
        if(exp.body != null) {
            exp.body.accept(this);
        }
    }

    @Override
    public void visit(NameTy exp) {
    }

    @Override
    public void visit(NilExp exp) {
    }

    @Override
    public void visit(OpExp exp) {
        exp.left.accept(this);
        exp.right.accept(this);
    }

    @Override
    public void visit(RecordExp exp) {
        if(exp.fields != null) {
            exp.fields.accept(this);
        }
    }

    @Override
    public void visit(RecordTy exp) {
        if(exp.fields != null) {
            exp.fields.accept(this);
        }
    }

    @Override
    public void visit(SeqExp exp) {
        if(exp.list != null) {
            exp.list.accept(this);
        }
    }

    @Override
    public void visit(SimpleVar exp) {
    }

    @Override
    public void visit(StringExp exp) {
    }

    @Override
    public void visit(SubscriptVar exp) {
        exp.var.accept(this);
        exp.index.accept(this);
    }

    @Override
    public void visit(TypeDec exp) {
    }

    @Override
    public void visit(Var exp) {
    }

    @Override
    public void visit(VarDec exp) {
        // exp.init is null when vardec is used as function formal.
        if(exp.init != null) {
            exp.init.accept(this);
        }
        if(exp.typ != null) {
            exp.typ.accept(this);
        }
    }

    @Override
    public void visit(VarExp exp) {
        exp.var.accept(this);
    }

    @Override
    public void visit(WhileExp exp) {
        exp.test.accept(this);
        exp.body.accept(this);
    }
}