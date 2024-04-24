package com.chaosopher.tigerlang.compiler.findescape;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.bind.Binder;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;

import org.junit.Test;
public class EscapeVisitorTest {
    
    private ParserService parserService;

    public EscapeVisitorTest() {
        parserService = new ParserService(new ParserFactory());
    }

    @Test
    public void prune() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let function prune() :int = 1 in 1 end", new ErrorMsg("", System.out));
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));

    }


}
