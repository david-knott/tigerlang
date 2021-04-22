package com.chaosopher.tigerlang.compiler.inlining;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.bind.Binder;
import com.chaosopher.tigerlang.compiler.bind.Renamer;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;
import com.chaosopher.tigerlang.compiler.types.TypeChecker;

import org.junit.Test;

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

    @Test
    public void relopLt() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let var a:int := 2 var b:int := 1 in printi(a > b) end", new ErrorMsg("", System.out));
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(new Renamer());
        program.accept(new PrettyPrinter(System.out));
        Inliner inliner = new Inliner(program);
        program.accept(inliner);
        inliner.visitedDecList.accept(new PrettyPrinter(System.out));
        assertEquals(0, inliner.inlinedCount);
    }

    @Test
    public void primeTest() {
    ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let function aa(a:int) : int = a function bb():int = ( aa(3);aa(2) ) in bb() end", new ErrorMsg("", System.out));
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(new Renamer());
        program.accept(new PrettyPrinter(System.out));
        Inliner inliner = new Inliner(program);
        program.accept(inliner);
    //    inliner.callGraph.show(System.out);
        inliner.visitedDecList.accept(new PrettyPrinter(System.out));
   //     Pruner pruner = new Pruner(inliner.visitedDecList);
  //      inliner.visitedDecList.accept(pruner);
       // pruner.visitedDecList.accept(new PrettyPrinter(System.out));

    }

    @Test
    public void forTest() {
    ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse(" let function a() = (for i := 0 to 10 do printi(i)) in a() end", new ErrorMsg("", System.out));
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(new Renamer());
      //  program.accept(new PrettyPrinter(System.out));

        Inliner inliner = new Inliner(program);
        program.accept(inliner);
        inliner.visitedDecList.accept(new Binder(errorMsg));
        inliner.visitedDecList.accept(new PrettyPrinter(System.out));
        //inliner.callGraph.show(System.out);

        Pruner pruner = new Pruner(inliner.visitedDecList);
        inliner.visitedDecList.accept(pruner);
  //      pruner.visitedDecList.accept(new Binder(errorMsg));
        pruner.visitedDecList.accept(new PrettyPrinter(System.out));

    }

    @Test
    public void inlineInline() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let function a():int = 1 function b(): int = 1 in a() end", errorMsg);
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(new Renamer());
        program.accept(new PrettyPrinter(System.out));

        Inliner inliner = new Inliner(program);
        program.accept(inliner);
        inliner.visitedDecList.accept(new Binder(errorMsg));
        inliner.visitedDecList.accept(new PrettyPrinter(System.out));
        //inliner.callGraph.show(System.out);

        Pruner pruner = new Pruner(inliner.visitedDecList);
       inliner.visitedDecList.accept(pruner);
        pruner.visitedDecList.accept(new Binder(errorMsg));
        pruner.visitedDecList.accept(new PrettyPrinter(System.out));

    }





}
