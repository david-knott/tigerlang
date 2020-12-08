package com.chaosopher.tigerlang.compiler.parse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;

public class DecWrapperTest {

    @Test
    public void wrapExp() {
        ErrorMsg errorMsg = new ErrorMsg("Dec Wrapper", System.out);
        String tiger = "1 = 1 | 2 = 2";
        InputStream targetStream = new ByteArrayInputStream(tiger.getBytes());
        Absyn program = new CupParser(targetStream, errorMsg).parse();
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.accept(prettyPrinter);
    }

    @Test
    public void wrapCallExp() {
        ErrorMsg errorMsg = new ErrorMsg("Dec Wrapper", System.out);
        String tiger = "print(\"hi\")";
        InputStream targetStream = new ByteArrayInputStream(tiger.getBytes());
        Absyn program = new CupParser(targetStream, errorMsg).parse();
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.accept(prettyPrinter);
    }

    @Test
    public void wrapFunctionExp() {
        ErrorMsg errorMsg = new ErrorMsg("Dec Wrapper", System.out);
        String tiger = "let function a():int = 1 in a() end";
        InputStream targetStream = new ByteArrayInputStream(tiger.getBytes());
        Absyn program = new CupParser(targetStream, errorMsg).parse();
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.accept(prettyPrinter);
    }
}