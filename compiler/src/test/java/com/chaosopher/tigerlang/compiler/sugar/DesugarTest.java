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
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;
import com.chaosopher.tigerlang.compiler.parse.Program;



public class DesugarTest {

    private ParserService parserService;

    public DesugarTest() {
        parserService = new ParserService(new ParserFactory());
    }

     @Test
    public void foorLoop() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = this.parserService.parse("for i := 0 to 10 do print_int(i)", errorMsg);
        program.accept(new Binder(errorMsg));

        Desugar absynCloner = new Desugar();
        program.accept(absynCloner);
        absynCloner.visitedDecList.accept(new Binder(errorMsg));
        absynCloner.visitedDecList.accept(new PrettyPrinter(System.out, true, true));
    }

    @Test
    public void stringEquals() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = this.parserService.parse("\"foo\" = \"bar\"", errorMsg);
        program.accept(new Binder(errorMsg));
        Desugar absynCloner = new Desugar();
        program.accept(absynCloner);
        absynCloner.visitedDecList.accept(new Binder(errorMsg));
        absynCloner.visitedDecList.accept(new PrettyPrinter(System.out, true, true));
    }

    @Test
    public void stringLess() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = this.parserService.parse("\"foo\" < \"bar\"", errorMsg);
        program.accept(new Binder(errorMsg));
        Desugar absynCloner = new Desugar();
        program.accept(absynCloner);
        absynCloner.visitedDecList.accept(new Binder(errorMsg));
        absynCloner.visitedDecList.accept(new PrettyPrinter(System.out, true, true));
    }


    @Test
    public void stringGreater() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = this.parserService.parse("\"foo\" > \"bar\"", errorMsg);
        program.accept(new Binder(new ErrorMsg("", System.out)));

        Desugar absynCloner = new Desugar();
        program.accept(absynCloner);
        absynCloner.visitedDecList.accept(new Binder(errorMsg));
        absynCloner.visitedDecList.accept(new PrettyPrinter(System.out, true, true));
    }
}

