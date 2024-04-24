package com.chaosopher.tigerlang.compiler.absyn;

public interface AbsynVisitor {

    void visit(ArrayExp exp);

    void visit(ArrayTy exp);

    void visit(AssignExp exp);

    void visit(BreakExp exp);

    void visit(CallExp exp);

    void visit(DecList exp);

    void visit(ExpList exp);

    void visit(FieldExpList exp);

    void visit(FieldList exp);

    void visit(FieldVar exp);

    void visit(ForExp exp);

    void visit(FunctionDec exp);

    void visit(IfExp exp);

    void visit(IntExp exp);

    void visit(LetExp exp);

    void visit(NameTy exp);

    void visit(NilExp exp);

    void visit(OpExp exp);

    void visit(RecordExp exp);

    void visit(RecordTy exp);

    void visit(SeqExp exp);

    void visit(SimpleVar exp);

    void visit(StringExp exp);

    void visit(SubscriptVar exp);

    void visit(TypeDec exp);

    void visit(Var exp);

    void visit(VarDec exp);

    void visit(VarExp exp);

    void visit(WhileExp exp);
}
