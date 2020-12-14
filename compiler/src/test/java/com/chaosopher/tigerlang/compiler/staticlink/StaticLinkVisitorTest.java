package com.chaosopher.tigerlang.compiler.staticlink;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.bind.Binder;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;

import org.junit.Test;

public class StaticLinkVisitorTest {

    private ParserService parserService;

    public StaticLinkVisitorTest() {
        parserService = new ParserService(new ParserFactory());
    }

    @Test
    public void noStaticLink() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = this.parserService.parse("let in  end", errorMsg);
        program.accept(new Binder(errorMsg));
        program.accept(new PrettyPrinter(System.out));
        program.accept(new StaticLinkVisitor());
        program.accept(new PrettyPrinter(System.out));
    }

    @Test
    public void staticLink() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = this.parserService.parse("let var a:int := 1 function a():int = a + a in  end", errorMsg);
        program.accept(new Binder(errorMsg));
        program.accept(new PrettyPrinter(System.out));
        program.accept(new StaticLinkVisitor());
        program.accept(new PrettyPrinter(System.out));
    }

    @Test
    public void nestedStaticLink() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = this.parserService.parse("let var a:int := 1 function a():int = let function b():int = a + a in end in end", errorMsg);
        program.accept(new Binder(errorMsg));
        program.accept(new PrettyPrinter(System.out));
        program.accept(new StaticLinkVisitor());
        program.accept(new PrettyPrinter(System.out));
    }

    @Test
    public void nestedStaticLink2() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = this.parserService.parse("let  function a():int = let var a:int := 1 function b():int = a + a in end in end", errorMsg);
        program.accept(new Binder(errorMsg));
        program.accept(new PrettyPrinter(System.out));
        program.accept(new StaticLinkVisitor());
        program.accept(new PrettyPrinter(System.out));
    }

    @Test
    public void recursive() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = this.parserService.parse("let var c:int := 0 function a():int = let var a:int := 1 function b():int = b() + c + a in b() end in a() end", errorMsg);
        program.accept(new Binder(errorMsg));
        program.accept(new PrettyPrinter(System.out));
        program.accept(new StaticLinkVisitor());
        program.accept(new PrettyPrinter(System.out));
    }
}
