package com.chaosopher.tigerlang.compiler.inlining;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.bind.Binder;
import com.chaosopher.tigerlang.compiler.bind.Renamer;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;

import org.junit.Test;

public class PrunerTest {

    private ParserService parserService;

    public PrunerTest() {
        parserService = new ParserService(new ParserFactory());
    }

    @Test
    public void prune() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let function prune() :int = 1 in 1 end", new ErrorMsg("", System.out));
        program.accept(new Binder(errorMsg));
        program.accept(new Renamer());
        Pruner pruner = new Pruner(program);
        program.accept(pruner);
        pruner.visitedDecList.accept(new PrettyPrinter(System.out));
        assertEquals(1, pruner.pruneCount);
    }

    @Test
    public void noPrune() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let function prune() :int = 1 in prune() end", new ErrorMsg("", System.out));
        program.accept(new Binder(errorMsg));
        program.accept(new Renamer());
        Pruner pruner = new Pruner(program);
        program.accept(pruner);
        pruner.visitedDecList.accept(new PrettyPrinter(System.out));
        assertEquals(0, pruner.pruneCount);
    }
}