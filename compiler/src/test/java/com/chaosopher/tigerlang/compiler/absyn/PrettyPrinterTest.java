package com.chaosopher.tigerlang.compiler.absyn;

import java.io.FileNotFoundException;

import org.junit.Test;

import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;

public class PrettyPrinterTest {

    @Test
    public void test4_9() throws FileNotFoundException {
        ParserService parserService = new ParserService(new ParserFactory());
        Absyn program = parserService.parse(
            "let /* Calculate n!. */ function fact (n : int) : int = if  n = 0 then 1 else n * fact (n - 1) in fact (10) end", 
            new ErrorMsg("f", System.out)
        );
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
    }

    @Test
    public void test4_10() throws FileNotFoundException {
        ParserService parserService = new ParserService(new ParserFactory());
        Absyn program = parserService.parse(
            "print(\"\\\"\u0045\u0050ITA\\\"\\n\")",
            new ErrorMsg("f", System.out)
        );
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.accept(prettyPrinter);
    }
}
