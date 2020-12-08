package com.chaosopher.tigerlang.compiler.inlining;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.bind.Binder;
import com.chaosopher.tigerlang.compiler.bind.Renamer;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.inlining.Inliner;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;

public class InlinerTest {

    private ParserService parserService;

    public InlinerTest() {
        parserService = new ParserService(new ParserFactory());
    }

    @Test
    public void simpleAdd() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let function sub(i: int, j: int) :int = i + j in sub(1, 2) end", new ErrorMsg("", System.out));
        program.accept(new Binder(errorMsg));
        program.accept(new Renamer());
        program.accept(new PrettyPrinter(System.out));
        Inliner inliner = new Inliner(program);
        program.accept(inliner);
        inliner.visitedDecList.accept(new PrettyPrinter(System.out));
        assertEquals(1, inliner.inlinedCount);
    }

    @Test
    public void nestedFunction() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let function sub(i: int, j: int) :int = i + j in printi(sub(1, 2)) end", new ErrorMsg("", System.out));
        program.accept(new Binder(errorMsg));
        program.accept(new Renamer());
        program.accept(new PrettyPrinter(System.out));
        Inliner inliner = new Inliner(program);
        program.accept(inliner);
        inliner.visitedDecList.accept(new PrettyPrinter(System.out));
        assertEquals(1, inliner.inlinedCount);
    }

    @Test
    public void recursiveFunction() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let function sub(i: int, j: int) :int = sub(i, j) in sub(1, 2) end", new ErrorMsg("", System.out));
        program.accept(new Binder(errorMsg));
        program.accept(new Renamer());
        program.accept(new PrettyPrinter(System.out));
        Inliner inliner = new Inliner(program);
        program.accept(inliner);
        inliner.visitedDecList.accept(new PrettyPrinter(System.out));
        assertEquals(0, inliner.inlinedCount);
    }

    @Test
    public void nestedRecursiveFunction() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let function sub(i: int, j: int) :int = sub(i, j) in printi(sub(1, 2)) end", new ErrorMsg("", System.out));
        program.accept(new Binder(errorMsg));
        program.accept(new Renamer());
        program.accept(new PrettyPrinter(System.out));
        Inliner inliner = new Inliner(program);
        program.accept(inliner);
        inliner.visitedDecList.accept(new PrettyPrinter(System.out));
        assertEquals(0, inliner.inlinedCount);
    }
}