package com.chaosopher.tigerlang.compiler.parse;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.Test;

import com.chaosopher.tigerlang.compiler.absyn.DecList;
import com.chaosopher.tigerlang.compiler.absyn.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;

public class PrimitiveTest {

    @Test
    public void wrapExp() throws FileNotFoundException {
        PrintStream out = new PrintStream(new ByteArrayOutputStream());
        ErrorMsg errorMsg = new ErrorMsg("Dec Wrapper", out);
        String tiger = "1";
        InputStream targetStream = new ByteArrayInputStream(tiger.getBytes());
        ParserService parserService = new ParserService(new ParserFactory());
     //   DecList decList = parserService.parse(targetStream, errorMsg);
      //  assertNotNull(decList);
      //  decList.accept(new PrettyPrinter(System.out));
    }
}
