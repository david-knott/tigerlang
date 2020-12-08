package com.chaosopher.tigerlang.compiler.types;

interface GenVisitor {
    public void visit(Type ttype);

    public void visit(NIL tnil);

    public void visit(VOID tvoid);

    public void visit(INT tint);

    public void visit(STRING tstring);

    public void visit(NAME tname);

    public void visit(ARRAY tarray);

    public void visit(RECORD trecord);

    public void visit(FUNCTION tfunction);

}