package com.chaosopher.tigerlang.compiler.tree;

import java.io.OutputStream;
import java.io.PrintStream;

public class PrettyPrinter2 implements TreeVisitor {

    final PrintStream printStream;
    int level = 0;

    void incLevel() {
        level++;
    }

    void decLevel() {
        level--;
    }

    public PrettyPrinter2(OutputStream out) {
        this.printStream = new PrintStream(out);
    }

    private void write(String s) {
        this.printStream.print(s);
    }

    @Override
    public void visit(BINOP op) {
        op.left.accept(this);
        switch (op.binop) {
            case BINOP.PLUS:
                this.write(" + ");
                break;
            case BINOP.MINUS:
                this.write(" - ");
                break;
            case BINOP.MUL:
                this.write(" * ");
                break;
            case BINOP.DIV:
                this.write(" / ");
                break;
            default:
                this.write(Integer.toString(op.binop));
                break;
        }
        op.right.accept(this);
    }

    @Override
    public void visit(CALL op) {
        this.write("call(");
        this.incLevel();
        ;
        op.func.accept(this);
        for (ExpList el = op.args; el != null; el = el.tail) {
            el.head.accept(this);
        }
        this.decLevel();
        this.write(")");
    }

    @Override
    public void visit(CONST op) {
        this.write(Integer.toString(op.value));
    }

    @Override
    public void visit(ESEQ op) {
        this.write("eseq(");
        this.incLevel();
        op.stm.accept(this);
        op.exp.accept(this);
        this.decLevel();
        this.write(")");
    }

    @Override
    public void visit(EXP op) {
        this.write("sxp(");
        this.incLevel();
        op.exp.accept(this);
        this.decLevel();
        this.write(")");
    }

    @Override
    public void visit(JUMP op) {
        this.write("jump:");
        op.exp.accept(this);
    }

    @Override
    public void visit(LABEL op) {
        this.write("label:" + op.label);
    }

    @Override
    public void visit(MEM op) {
        this.write("mem[");
        this.incLevel();
        op.exp.accept(this);
        this.decLevel();
        this.write("]");
    }

    @Override
    public void visit(MOVE op) {
        op.dst.accept(this);
        this.write(" ← ");
        op.src.accept(this);
    }

    @Override
    public void visit(NAME op) {
        this.write(op.label.toString());
    }

    @Override
    public void visit(SEQ op) {
        this.write("seq(");
        this.incLevel();
        op.left.accept(this);
        op.right.accept(this);
        this.decLevel();
        this.write(")");
    }

    @Override
    public void visit(TEMP op) {
        this.write(op.temp.toString());
    }

    @Override
    public void visit(CJUMP cjump) {
        this.write("cjump:");
        cjump.left.accept(this);
        this.write(" ");
        String op = null;
        switch(cjump.relop) {
            case CJUMP.EQ:
                op = "==";
                break;
             case CJUMP.GE:
                op = ">=";
                break;
             case CJUMP.GT:
                op = ">";
                break;
             case CJUMP.LE:
                op = "<=";
                break;
             case CJUMP.LT:
                op = "<";
                break;
             case CJUMP.NE:
                op = "<>";
                break;
             case CJUMP.UGE:
                op = ">=";
                break;
             case CJUMP.UGT:
                op = ">";
                break;
              case CJUMP.ULE:
                op = "<=";
                break;
               case CJUMP.ULT:
                op = "<";
                break;
        }
        this.write(op);
        this.write(" ");
        cjump.right.accept(this);
        this.write(" ? ");
        this.write(cjump.iftrue.toString());
        this.write(" : ");
        this.write(cjump.iffalse.toString());
    }

    @Override
    public void visit(StmList stmList) {
        for(;stmList != null; stmList = stmList.tail) {
            stmList.head.accept(this);
            this.write("\n");
        }
    }
}