package com.chaosopher.tigerlang.compiler.sugar;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.PrintStream;

import org.junit.Test;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.bind.Binder;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.parse.CupParser;
import com.chaosopher.tigerlang.compiler.parse.Parser;
import com.chaosopher.tigerlang.compiler.parse.Program;



public class DesugarTest {

     @Test
    public void foorLoop() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Parser parser = new CupParser("for i := 0 to 10 do print_int(i)", new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        program.accept(new Binder(errorMsg));

        Desugar absynCloner = new Desugar();
        program.accept(absynCloner);
        absynCloner.visitedExp.accept(new Binder(errorMsg));
        absynCloner.visitedExp.accept(new PrettyPrinter(System.out, true, true));
    }

    @Test
    public void stringEquals() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Parser parser = new CupParser("\"foo\" = \"bar\"", new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        program.accept(new Binder(errorMsg));

        Desugar absynCloner = new Desugar();
        program.accept(absynCloner);
        absynCloner.visitedExp.accept(new Binder(errorMsg));
        absynCloner.visitedExp.accept(new PrettyPrinter(System.out, true, true));
    }

    @Test
    public void stringLess() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Parser parser = new CupParser("\"foo\" < \"bar\"", new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        program.accept(new Binder(errorMsg));

        Desugar absynCloner = new Desugar();
        program.accept(absynCloner);
        absynCloner.visitedExp.accept(new Binder(errorMsg));
        absynCloner.visitedExp.accept(new PrettyPrinter(System.out, true, true));
    }


    @Test
    public void stringGreater() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Parser parser = new CupParser("\"foo\" > \"bar\"", new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        program.accept(new Binder(new ErrorMsg("", System.out)));

        Desugar absynCloner = new Desugar();
        program.accept(absynCloner);
        absynCloner.visitedExp.accept(new Binder(errorMsg));
        absynCloner.visitedExp.accept(new PrettyPrinter(System.out, true, true));
    }
}

