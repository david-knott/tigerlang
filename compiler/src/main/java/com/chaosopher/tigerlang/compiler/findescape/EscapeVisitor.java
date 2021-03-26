package com.chaosopher.tigerlang.compiler.findescape;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.DefaultVisitor;
import com.chaosopher.tigerlang.compiler.absyn.FunctionDec;
import com.chaosopher.tigerlang.compiler.absyn.LetExp;
import com.chaosopher.tigerlang.compiler.absyn.SimpleVar;
import com.chaosopher.tigerlang.compiler.absyn.VarDec;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;

/**
 * The EscapeVisitor class traverses the Abtract Syntax Tree looking
 * for variables that are defined at a lower static nesting level than
 * where they are used. This generally occurs when a nested function
 * calls a variable that is defined outside of it.
 */
public class EscapeVisitor extends DefaultVisitor {

    public static EscapeVisitor apply(ErrorMsg errorMsg, Absyn absyn) {
        EscapeVisitor escapeVisitor = new EscapeVisitor(errorMsg);
        absyn.accept(escapeVisitor);
        return escapeVisitor;
    }
    final ErrorMsg  errorMsg;
    final com.chaosopher.tigerlang.compiler.symbol.GenericTable<Escape> escEnv = new com.chaosopher.tigerlang.compiler.symbol.GenericTable<Escape>();
    int depth = 0;

    public EscapeVisitor(ErrorMsg errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public void visit(LetExp letExp) {
        this.escEnv.beginScope();
        for (var dec = letExp.decs; dec != null; dec = dec.tail) {
            letExp.decs.accept(this);
        }
        if(letExp.body != null) {
            letExp.body.accept(this);
        }
        this.escEnv.endScope();
    }

    @Override
    public void visit(FunctionDec e) {
        this.escEnv.beginScope();
        this.depth++;
        for (var fl = e.params; fl != null; fl = fl.tail) {
            VarDec vd = (VarDec)fl.head;
            var formalEscape = new FormalEscape(this.depth, vd);
            this.escEnv.put(vd.name, formalEscape);
        }
        if(e.body != null) {
            e.body.accept(this);
        }
        this.depth--;
        this.escEnv.endScope();
        if(e.next != null) {
            e.next.accept(this);
        }
    }
    
    @Override
    public void visit(VarDec e) {
        e.init.accept(this);
        this.escEnv.put(e.name, new VarEscape(depth, e));
    }

    @Override
    public void visit(SimpleVar simpleVar) {
        var escape = this.escEnv.get(simpleVar.name);
        //escape is null if using loop var within a for loop
        if(escape == null){
            this.errorMsg.error(simpleVar.pos, "Symbol '" + simpleVar.name + "' has not been declared.");
            return;
        }
        // we check for undefined variable in the semant phase
        if (escape.depth < this.depth) {
            escape.setEscape();
        }
    }
}