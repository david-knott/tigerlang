package com.chaosopher.tigerlang.compiler.types;

import java.io.PrintStream;

public class PrettyPrinter implements GenVisitor {

    private final PrintStream out;

    public PrettyPrinter(PrintStream o) {
        this.out = o;
    }

    @Override
    public void visit(Type ttype) {
        out.print(ttype);
    }

    @Override
    public void visit(NIL tnil) {
        out.print("nil=");
        out.print("(null)");
    }

    @Override
    public void visit(VOID tvoid) {
        out.print("void");
    }

    @Override
    public void visit(INT tint) {
        out.print("int");
    }

    @Override
    public void visit(STRING tstring) {
        out.print("string");
    }

    @Override
    public void visit(NAME tname) {
        out.print(tname.name);
        out.print("=(");
        tname.actual().accept(this);
        out.print(")");
    }

    @Override
    public void visit(ARRAY tarray) {
        this.out.print("array=(");
        tarray.element.accept(this);
        this.out.print(")");
    }

    @Override
    public void visit(RECORD trecord) {
        this.out.print("record=(");
        for(; trecord != null; trecord = trecord.tail) {
            this.out.print(trecord.fieldName);
            this.out.print("=");
            trecord.fieldType.accept(this);
            if(trecord.tail != null) {
                this.out.print(",");
            }
        }
        out.print(")");
    }

    @Override
    public void visit(FUNCTION tfunction) {
        // TODO Auto-generated method stub

    }

}