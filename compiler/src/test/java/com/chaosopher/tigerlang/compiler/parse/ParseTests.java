package com.chaosopher.tigerlang.compiler.parse;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.Test;

import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.util.TaskContext;

/**
 * Testing to ensure parser is working correctly.
 * 
 * https://www.lrde.epita.fr/~tiger/assignments.split/TC_002d1-Samples.html#TC_002d1-Samples
 */
public class ParseTests {

    @Test
    public void unterminatedComment() {
        // fails lexical analysis but parse, because extra chars are
        // treated as comments and therefore ignored by parser.
        ErrorMsg errorMsg = new ErrorMsg("test", System.out);
        String program = "1\n/* This comments starts at /* 2.2 */";
        InputStream targetStream = new ByteArrayInputStream(program.getBytes());
        new CupParser(targetStream, errorMsg).parse();
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void unterminatedString() {
        // fails lexical analysis and also fails parse because parser
        // attempts to parse the chars. Ideally, we should not parse 
        // if there are lexical errors.
        ErrorMsg errorMsg = new ErrorMsg("test", System.out);
        String program = "let\nvar s:string := \"characters\nin end";
        InputStream targetStream = new ByteArrayInputStream(program.getBytes());
        new CupParser(targetStream, errorMsg).parse();
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void invalidChar() {
        // ideally a lexical error should prevent parsing.
        ErrorMsg errorMsg = new ErrorMsg("invalidChar", System.out);
        String program = "$\n$\n$";
        InputStream targetStream = new ByteArrayInputStream(program.getBytes());
        new CupParser(targetStream, errorMsg).parse();
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void syntaxError() {
        // passes lexical analysis
        // fails parsing
        ErrorMsg errorMsg = new ErrorMsg("typeNil", System.out);
        String program = "let var a : nil := () in 1 end";
        InputStream targetStream = new ByteArrayInputStream(program.getBytes());
        new CupParser(targetStream, errorMsg).parse();
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void carriageReturn() {
        ErrorMsg errorMsg = new ErrorMsg("carriageReturn", System.out);
        String program = "let\nvar s:string := \"characters\n more test\" in end";
        InputStream targetStream = new ByteArrayInputStream(program.getBytes());
        new CupParser(targetStream, errorMsg).parse();
        assertFalse(errorMsg.anyErrors);
    }

    @Test
    public void success() {
        ErrorMsg errorMsg = new ErrorMsg("success", System.out);
        String program = "/* An array type and an array variable. */ let type arrtype = array of int var arr1 : arrtype := arrtype [10] of 0 in arr1[2] end";
        InputStream targetStream = new ByteArrayInputStream(program.getBytes());
        new CupParser(targetStream, errorMsg).parse();
        assertFalse(errorMsg.anyErrors);
    }

    @Test
    public void errorRecovery() {
        ErrorMsg errorMsg = new ErrorMsg("errorRecovery", System.out);
        String program = "(\n1;\n(2, 3);\n(4, 5);\n6\n)";
        InputStream targetStream = new ByteArrayInputStream(program.getBytes());
        new CupParser(targetStream, errorMsg).parse();
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void strangeChars() {
        ErrorMsg errorMsg = new ErrorMsg("errorRecovery", System.out);
        String program = "var a:int := 1 var b:int := a + c 1234";
        InputStream targetStream = new ByteArrayInputStream(program.getBytes());
        new CupParser(targetStream, errorMsg).parse();
        assertTrue(errorMsg.anyErrors);
    }



}