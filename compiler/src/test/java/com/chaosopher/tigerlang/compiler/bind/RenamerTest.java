package com.chaosopher.tigerlang.compiler.bind;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.PrintStream;
import java.util.Arrays;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;
import com.chaosopher.tigerlang.compiler.types.TypeChecker;

@RunWith(Theories.class)
public class RenamerTest {

    @DataPoints
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
            {"let type a = { a: int } function a(a: a): a = a { a = a + a } var a : a := a(1, 2) in a.a end", false},
            {"let function foo() : int = bar() function bar() : int = foo() function foobar() : int = let function foofoo() : int = bar() in 1 end in 0 end", false},
            {"let type a = { a: string } function a(a: a): a = a { a = a.a + a.a } var a : a := a(a{a = \"\"}) in a.a end", false},
            {"printi(1)", false},
            {"let function sub(i: int, j: int) :int = i + j in printi(sub(1, 2)) end", false}
        });
    }

    private ParserService parserService;

    public RenamerTest() {
        parserService = new ParserService(new ParserFactory());
    }

    @Theory
    public void template(Object[] data) {
        String code = (String)data[0];
        boolean res = (boolean)data[1];
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("renamer-test", outputStream);
     //   System.out.println("Testing:" + code);
        Absyn program = parserService.parse(code, errorMsg);
        program.accept(new Binder(errorMsg));
   //     program.accept(new TypeChecker(errorMsg));
        program.accept(new Renamer());
        program.accept(new PrettyPrinter(System.out, false, false));
        assertEquals(res, errorMsg.anyErrors, code);
    }
}