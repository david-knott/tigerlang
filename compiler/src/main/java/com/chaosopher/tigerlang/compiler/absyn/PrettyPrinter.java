package com.chaosopher.tigerlang.compiler.absyn;

import java.io.PrintStream;

import com.chaosopher.tigerlang.compiler.util.Assert;
import com.chaosopher.tigerlang.compiler.symbol.Symbol;

/**
 * Returns a formatted version of the AST. This formatted version can be feed
 * back into the compiler. See
 * https://www.lrde.epita.fr/~tiger/assignments.split/TC_002d2-Pretty_002dPrinting-Samples.html#TC_002d2-Pretty_002dPrinting-Samples
 */
public class PrettyPrinter implements AbsynVisitor {
    private final PrintStream out;
    private boolean spaces = true;
    private final int indentation = 4;
    private int currentIndentation = 0;
    public boolean escapesDisplay = false;
    public boolean bindingsDisplay = false;

    public PrettyPrinter(PrintStream o) {
        this(o, false, false);
    }

    public PrettyPrinter(PrintStream printStream, boolean escapesDisplay, boolean bindingsDisplay) {
        Assert.assertNotNull(printStream);
        this.out = printStream;
        this.escapesDisplay = escapesDisplay;
        this.bindingsDisplay = bindingsDisplay;
        say("\n/* == Abstract Syntax Tree. == */");
	}

    private void lineBreakAndIndent() {
        say(System.lineSeparator());
        for (int i = 0; i < this.currentIndentation * this.indentation; i++)
            out.print(this.spaces ? " " : "\t");
    }

    private void say(Symbol symbol) {
        say(symbol.toString());
    }

    private void space() {
        say(" ");
    }

    private void say(String str) {
        out.print(str);
    }

    @Override
    public void visit(ArrayExp exp) {
        say(exp.typ);
        say("[");
        exp.size.accept(this);
        say("]");
        space();
        say("of");
        space();
        exp.init.accept(this);
    }

    @Override
    public void visit(ArrayTy exp) {
        say("array");
        space();
        say("of");
        space();
        say(exp.typ);
    }

    @Override
    public void visit(AssignExp exp) {
        exp.var.accept(this);
        space();
        say(":=");
        space();
        exp.exp.accept(this);
    }

    @Override
    public void visit(BreakExp exp) {
        space();
        say("break");
        if(this.bindingsDisplay) {
            space();
            say("/* " + System.identityHashCode(exp.loop) + " */");
            space();
        }
    }

    @Override
    public void visit(CallExp exp) {
        say(exp.func.toString());
        say("(");
        for (ExpList expList = exp.args; expList != null; expList = expList.tail) {
            expList.head.accept(this);
            if (expList.tail != null)
                say(",");
        }
        say(")");
        if(this.bindingsDisplay) {
            space();
            say("/* " + System.identityHashCode(exp.def) + " */");
            space();
        }
    }

    @Override
    public void visit(DecList exp) {
        for(;exp != null; exp = exp.tail) {
            exp.head.accept(this);
        }
    }

    @Override
    public void visit(ExpList exp) {
        for(;exp != null; exp = exp.tail) {
            exp.head.accept(this);
            if(exp.tail != null) {
                say(";");
                lineBreakAndIndent();
            }
        }
    }

    @Override
    public void visit(FieldExpList exp) {
        for(;exp != null; exp = exp.tail) {
            say(exp.name);
            space();
            say("=");
            space();
            exp.init.accept(this);
            if(exp.tail != null)
                say(",");
        }
    }

    @Override
    public void visit(FieldList exp) {
        if(exp != null) {
            do
            {
                say(exp.name);
                space();
                // definition
                if(this.bindingsDisplay) {
                    space();
                    say("/* " + System.identityHashCode(exp) + " */");
                    space();
                }
                if(this.escapesDisplay && exp.escape) {
                    space();
                    say("/* escapes */");
                }
                say(":");
                space();
                say(exp.typ.name);
                // type usage.
                if(this.bindingsDisplay) {
                    space();
                    say("/* " + System.identityHashCode(exp.def) + " */");
                    space();
                }
                if(exp.tail != null) {
                    say(",");
                }
                exp = exp.tail;
            }while(exp != null);
        }
    }

    @Override
    public void visit(FieldVar exp) {
        exp.var.accept(this);
        say(".");
        say(exp.field);
    }

    @Override
    public void visit(ForExp exp) {
        say("for");  
        if(this.bindingsDisplay) {
            space();
            say("/* " + System.identityHashCode(exp) + " */");
            space();
        }
        space();
        say(exp.var.name);
        if(this.bindingsDisplay) {
            space();
            say("/* " + System.identityHashCode(exp.var) + " */");
            space();
        }
        space();
        say(":=");
        space();
        exp.var.init.accept(this);
        space();
        say("to");
        space();
        exp.hi.accept(this);
        space();
        say("do");
        lineBreakAndIndent();
        exp.body.accept(this);
    }

    @Override
    public void visit(FunctionDec functionDec) {
        lineBreakAndIndent();
        say(functionDec.body != null ? "function" : "primitive");
        space();
        say(functionDec.name.toString());
        if(this.bindingsDisplay) {
            space();
            say("/* " + System.identityHashCode(functionDec) + " */");
            space();
        }
        say("(");
        if (functionDec.params != null) {
            for(DecList decList = functionDec.params; decList != null; decList = decList.tail) {
                VarDec exp = (VarDec)decList.head;
                say(exp.name);
                space();
                // definition
                if(this.bindingsDisplay) {
                    space();
                    say("/* " + System.identityHashCode(exp) + " */");
                    space();
                }
                if(this.escapesDisplay && exp.escape) {
                    space();
                    say("/* escapes */");
                }
                say(":");
                space();
                exp.typ.accept(this);
                if(decList.tail != null) {
                    say(",");
                }
            }
        }
        say(")");
        if (functionDec.result != null) {
            space();
            say(":");
            space();
            functionDec.result.accept(this);
        }
        if(functionDec.body != null) {
            space();
            say("=");
            space();
            currentIndentation++;
            functionDec.body.accept(this);
            currentIndentation--;
        }
        if(functionDec.next != null) {
            functionDec.next.accept(this);
        }
    }

    @Override
    public void visit(IfExp exp) {
        lineBreakAndIndent();
        say("if");
        space();
        exp.test.accept(this);
        space();
        lineBreakAndIndent();
        say("then");
        space();
        exp.thenclause.accept(this);
        if (exp.elseclause != null) {
            lineBreakAndIndent();
            say("else");
            space();
            exp.elseclause.accept(this);
        }
    }

    @Override
    public void visit(IntExp exp) {
        say(Integer.toString(exp.value));
    }
    
    @Override
    public void visit(LetExp letExp) {
        lineBreakAndIndent();
        say("let");
        if (letExp.decs != null) {
            currentIndentation++;
            letExp.decs.accept(this);
            currentIndentation--;
        }
        lineBreakAndIndent();
        say("in");
        if (letExp.body != null) {
            currentIndentation++;
            letExp.body.accept(this);
            currentIndentation--;
        }
        lineBreakAndIndent();
        say("end");
    }

    @Override
    public void visit(NameTy exp) {
        say(exp.name);
        if(this.bindingsDisplay) {
            space();
            say("/* " + System.identityHashCode(exp.def) + " */");
            space();
        }
    }

    @Override
    public void visit(NilExp exp) {
        space();
        say("nil");
    }

    @Override
    public void visit(OpExp exp) {
        say("(");
        exp.left.accept(this);
        space();
        switch(exp.oper) {
            case OpExp.PLUS: say("+"); break;
            case OpExp.MINUS: say("-"); break;
            case OpExp.MUL: say("*"); break;
            case OpExp.DIV: say("/"); break;
            case OpExp.EQ: say("="); break;
            case OpExp.NE: say("!="); break;
            case OpExp.LT: say("<"); break;
            case OpExp.LE: say("<="); break;
            case OpExp.GT: say(">"); break;
            case OpExp.GE: say(">="); break;
        }
        space();
        exp.right.accept(this);
        say(")");
    }

    @Override
    public void visit(RecordExp exp) {
        say(exp.typ);
        // type usage
        if(this.bindingsDisplay) {
            space();
            say("/* " + System.identityHashCode(exp.def) + " */");
            space();
        }
        space();
        say("{");
        space();
        if(exp.fields != null) {
            exp.fields.accept(this);
        }
        space();
        say("}");
    }

    @Override
    public void visit(RecordTy exp) {
        say("{");
        if(exp.fields != null) {
            exp.fields.accept(this);
        }
        say("}");
    }

    private SeqExp foldSeqExp(SeqExp exp) {
        if(exp.list == null) {
            return exp;
        }
        if(exp.list.tail == null && exp.list.head instanceof SeqExp) {
           return foldSeqExp((SeqExp)exp.list.head);
        }
        return exp;
    }

    @Override
    public void visit(SeqExp exp) {
        exp = this.foldSeqExp(exp);
        this.lineBreakAndIndent();
        say("(");
        this.currentIndentation++;
        this.lineBreakAndIndent();
        if(exp.list != null) {
            exp.list.accept(this);
        }
        this.currentIndentation--;
        this.lineBreakAndIndent();
        say(")");
    }

    @Override
    public void visit(SimpleVar exp) {
        say(exp.name);
        if(this.bindingsDisplay) {
            space();
            say("/* " + System.identityHashCode(exp.def) + " */");
            space();
        }
    }

    @Override
    public void visit(StringExp exp) {
        say("\"" + exp.value + "\"");
    }

    @Override
    public void visit(SubscriptVar exp) {
        exp.var.accept(this);
        say("[");
        exp.index.accept(this);
        say("]");
    }

    @Override
    public void visit(TypeDec exp) {
        lineBreakAndIndent();
        say("type");
        space();
        say(exp.name);
        if(this.bindingsDisplay) {
            space();
            say("/* " + System.identityHashCode(exp) + " */");
            space();
        }
        space();
        say("=");
        space();
        exp.ty.accept(this);
        if(exp.next != null) {
            exp.next.accept(this);
        }
    }

    @Override
    public void visit(Var exp) {

    }

    @Override
    public void visit(VarDec exp) {
        lineBreakAndIndent();
        say("var");
        space();
        say(exp.name);
        if(this.bindingsDisplay) {
            space();
            say("/* " + System.identityHashCode(exp) + " */");
            space();
        }
        space();
        if(exp.typ != null) {
            say(":");
            space();
            exp.typ.accept(this);
            space();
        }
        say(":=");
        space();
        if(exp.init != null) {
            exp.init.accept(this);
        }
        if(this.escapesDisplay && exp.escape) {
            space();
            say("/* escapes */");
            space();
        }
    }

    @Override
    public void visit(VarExp exp) {
        exp.var.accept(this);
    }

    @Override
    public void visit(WhileExp exp) {
        lineBreakAndIndent();
        say("while");
        if(this.bindingsDisplay) {
            space();
            say("/* " + System.identityHashCode(exp) + " */");
            space();
        }
        space();
        exp.test.accept(this);
        space();
        say("do");
        lineBreakAndIndent();
        exp.body.accept(this);
        space();
    }
}
