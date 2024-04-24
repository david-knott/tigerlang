package com.chaosopher.tigerlang.compiler.sugar;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.bind.Binder;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;

import org.junit.Test;


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

        Desugar absynCloner = new Desugar(true, true);
        program.accept(absynCloner);
        absynCloner.visitedDecList.accept(new Binder(errorMsg));
        absynCloner.visitedDecList.accept(new PrettyPrinter(System.out, false, false));
    }

    @Test
    public void stringEquals() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = this.parserService.parse("\"foo\" = \"bar\"", errorMsg);
        program.accept(new Binder(errorMsg));
        Desugar absynCloner = new Desugar(true, true);
        program.accept(absynCloner);
        absynCloner.visitedDecList.accept(new Binder(errorMsg));
        absynCloner.visitedDecList.accept(new PrettyPrinter(System.out, true, true));
    }

    @Test
    public void stringLess() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = this.parserService.parse("\"foo\" < \"bar\"", errorMsg);
        program.accept(new Binder(errorMsg));
        Desugar absynCloner = new Desugar(true, true);
        program.accept(absynCloner);
        absynCloner.visitedDecList.accept(new Binder(errorMsg));
        absynCloner.visitedDecList.accept(new PrettyPrinter(System.out, true, true));
    }


    @Test
    public void stringGreater() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = this.parserService.parse("\"foo\" > \"bar\"", errorMsg);
        program.accept(new Binder(new ErrorMsg("", System.out)));

        Desugar absynCloner = new Desugar(true, true);
        program.accept(absynCloner);
        absynCloner.visitedDecList.accept(new Binder(errorMsg));
        absynCloner.visitedDecList.accept(new PrettyPrinter(System.out, true, true));
    }


    @Test
    public void tbiDesugar() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = this.parserService.parse("let var N := 8 function printb() = (for i := 0 to N - 1 do (for j := 0 to N - 1 do print(if i > j then \"x\" else \"y\"); print(\"\\\n\")); print(\"\\\n\")) in printb() end", errorMsg);
        program.accept(new Binder(new ErrorMsg("", System.out)));
        Desugar absynCloner = new Desugar(true, false);
        program.accept(absynCloner);
        absynCloner.visitedDecList.accept(new Binder(errorMsg));
        absynCloner.visitedDecList.accept(new PrettyPrinter(System.out, true, true));
    }

}

