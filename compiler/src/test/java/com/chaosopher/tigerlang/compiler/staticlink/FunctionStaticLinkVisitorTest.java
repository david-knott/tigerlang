package com.chaosopher.tigerlang.compiler.staticlink;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.bind.Binder;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.findescape.EscapeVisitor;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;

import org.junit.Test;

public class FunctionStaticLinkVisitorTest {

    private ParserService parserService;

    public FunctionStaticLinkVisitorTest() {
        parserService = new ParserService(new ParserFactory());
    }

    @Test
    public void noStaticLink() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = this.parserService.parse("let in  end", errorMsg);
        program.accept(new Binder(errorMsg));
        program.accept(new PrettyPrinter(System.out));
        program.accept(new FunctionStaticLinkVisitor());
        program.accept(new PrettyPrinter(System.out));
    }

    @Test
    public void staticLink() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = this.parserService.parse("let var a:int := 1 function a():int = a + a in a() end", errorMsg);
        program.accept(new Binder(errorMsg));
        program.accept(new PrettyPrinter(System.out));
        program.accept(new FunctionStaticLinkVisitor());
        program.accept(new StaticLinkEscapeVisitor(program));
        program.accept(new PrettyPrinter(System.out));
    }

    @Test
    public void nestedStaticLink() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = this.parserService.parse("let var a:int := 1 function a():int = let function b():int = a + a in end in a() end", errorMsg);
        program.accept(new Binder(errorMsg));
        program.accept(new PrettyPrinter(System.out));
        program.accept(new FunctionStaticLinkVisitor());
        program.accept(new PrettyPrinter(System.out));
    }

    @Test
    public void nestedStaticLink2() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = this.parserService.parse("let  function a():int = let var a:int := 1 function b():int = a + a in end in a() end", errorMsg);
        program.accept(new Binder(errorMsg));
        program.accept(new PrettyPrinter(System.out));
        program.accept(new FunctionStaticLinkVisitor());
        program.accept(new PrettyPrinter(System.out));
    }

    @Test
    public void recursive() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = this.parserService.parse("let var c:int := 0 function a():int = let var a:int := 1 function b():int = b() + c + a in b() end in a() end", errorMsg);
        program.accept(new Binder(errorMsg));
        program.accept(new PrettyPrinter(System.out));
        program.accept(new FunctionStaticLinkVisitor());
        program.accept(new PrettyPrinter(System.out));
    }


    @Test
    public void nonEscapingStaticLink() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = this.parserService.parse("let var foo := 1 function foo() : int = foo in foo() end", errorMsg);
        program.accept(new Binder(errorMsg));
        program.accept(new PrettyPrinter(System.out));
        program.accept(new FunctionStaticLinkVisitor());
        program.accept(new StaticLinkEscapeVisitor(program));
        program.accept(new PrettyPrinter(System.out));
    }


    @Test
    public void factorial() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = this.parserService.parse("let function fact(n : int) : int = if (n = 0) then 1 else n * fact((n - 1)) in fact(10) end", errorMsg);
        program.accept(new Binder(errorMsg));
        program.accept(new PrettyPrinter(System.out));
        program.accept(new FunctionStaticLinkVisitor());
        program.accept(new StaticLinkEscapeVisitor(program));
        program.accept(new PrettyPrinter(System.out));
    }

    @Test
    public void improve() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = this.parserService.parse("let var toto := 1 function outer() : int = let function inner() : int = toto in inner() end in outer() end", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        program.accept(new PrettyPrinter(System.out));
        program.accept(new FunctionStaticLinkVisitor());
        program.accept(new StaticLinkEscapeVisitor(program));
        program.accept(new PrettyPrinter(System.out, true, false));
    }

    @Test
    public void improve2() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = this.parserService.parse("let var v := 1 function outer() : int = let function inner() : int = v in inner() end function sister() : int = outer() in sister() end", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        program.accept(new PrettyPrinter(System.out));
        program.accept(new FunctionStaticLinkVisitor());
        program.accept(new StaticLinkEscapeVisitor(program));
        program.accept(new PrettyPrinter(System.out, true, false));
        //sister does not need a static link on its frame, this can be in a register.

        //outer does need the staticlink on its frame as inner needs access to the variable v outside of outer.

        // if a function calls another function at the same level, the first function's static link does not
        
        // escape unless that function calls a function higher up as well as the function at the same level.

        //how do we know if a function is at the same level ? Its contained in the same dec block.
    }
}
