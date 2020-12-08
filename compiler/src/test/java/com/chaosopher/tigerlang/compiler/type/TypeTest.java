package com.chaosopher.tigerlang.compiler.type;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.PrintStream;

import org.junit.Test;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.bind.Binder;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.parse.CupParser;
import com.chaosopher.tigerlang.compiler.parse.Parser;
import com.chaosopher.tigerlang.compiler.parse.Program;
import com.chaosopher.tigerlang.compiler.types.TypeChecker;

public class TypeTest {

    @Test
    public void test_4_31a() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("1 + \"2\"",errorMsg);
        Absyn program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }
 
    @Test
    public void test_4_31b() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("\"1\" + 2",errorMsg);
        Absyn program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void test_4_32() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("/* error: index variable erroneously assigned to.  */ for i := 10 to 1 do i := i - 1", errorMsg);
        Absyn program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void varDec() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let var a:string := 1 in end",errorMsg);
        Absyn program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void binopAssignIntToString() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let var a:string := 1 + 2 in end",errorMsg);
        Absyn program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void binopAssignIntOk() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let var a:int := 1 + 2 in end",errorMsg);
        Absyn program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
        assertFalse(errorMsg.anyErrors);
    }

    @Test
    public void binopAssignStringToInt() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let var a:int := \"one\" + \"two\" in end",errorMsg);
        Absyn program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void binopAssignStringOk() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let var a:string := \"one\" + \"two\" in end",errorMsg);
        Absyn program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
        assertFalse(errorMsg.anyErrors);
    }



    @Test
    public void ifThenElseOk() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let var r:int := 0 in r := (if (1) then 2 else 3) end",errorMsg);
        Absyn program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
        assertFalse(errorMsg.anyErrors);
    }

    @Test
    public void ifThenElseAssignType() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let var r:string := \"\" in r := (if (1) then 2 else 3) end",errorMsg);
        Absyn program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void ifThenElseReturnType() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let var r:int := 0 in r := (if (1) then 2 else \"string\") end",errorMsg);
        Absyn program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void ifThenElseTestType() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let var r:int := 0 in r := (if (\"string\") then 2 else 3) end",errorMsg);
        Absyn program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void sequenceAssign() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let var r:int := 0 in r := (1; 2; 3; 4; \"string\") end",errorMsg);
        Absyn program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void sequenceAssignVoid() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let var r:int := 0 in r := (1; 2; 3; 4; r := r + 1) end",errorMsg);
        Absyn program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void sequenceAssignOk() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let var r:int := 0 in r := (1; 2; 3; 4; 5) end",errorMsg);
        Absyn program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
        assertFalse(errorMsg.anyErrors);
    }

    @Test
    public void whileTestType() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let var r:int := 0 in while (\"string\") do r := r + 1 end",errorMsg);
        Absyn program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void whileBodyType() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let var r:int := 0 in while (1) do r end",errorMsg);
        Absyn program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void whileOk() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let var r:int := 0 in while (1) do r := r + 1 end",errorMsg);
        Absyn program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
        assertFalse(errorMsg.anyErrors);
    }

    @Test
    public void whileOk2() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let  in while (1) do () end",errorMsg);
        Absyn program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
        assertFalse(errorMsg.anyErrors);
    }

    @Test
    public void functionInvalidArgType() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let function a(a:int, b:int) = () in a(1, \"string\") end",errorMsg);
        Absyn program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void functionMissingArgs() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let function a(a:int, b:int) = () in a() end",errorMsg);
        Absyn program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void functionExtraArgs() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let function a(a:int, b:int) = () in a(1, 2, 3) end",errorMsg);
        Absyn program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }


    @Test
    public void recAssignToInt() {
        Parser parser = new CupParser("let type rec = { a : int} var r := rec { a = 42} in r := 1 end",
                new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, true);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void recAssignField() {
        Parser parser = new CupParser("let type rec = { a : string} var r := rec { a = 42} in r := nil end",
                new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, true);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void recAssignToNil() {
        Parser parser = new CupParser("let type rec = { a : int} var r := rec { a = 42} in r := nil end",
                new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, true);
        program.accept(prettyPrinter);
        assertFalse(errorMsg.anyErrors);
    }

    @Test
    public void arraySizeString() {
        Parser parser = new CupParser("let type iat = array of int var a:iat := iat[\"\"] of 0 in end",
                new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, true);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void arrayInitialiserInt() {
        Parser parser = new CupParser("let type iat = array of string var a:iat := iat[0] of 1 in end",
                new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, true);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void arrayInitialiserString() {
        Parser parser = new CupParser("let type iat = array of int var a:iat := iat[0] of \"string\" in end",
                new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, true);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void arrayAssignToInt() {
        Parser parser = new CupParser("let type iat = array of int var a:iat := iat[10] of 0 in a := 1 end",
                new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, true);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void arrayOk() {
        Parser parser = new CupParser("let type iat = array of int var a:iat := iat[10] of 0 in end",
                new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, true);
        program.accept(prettyPrinter);
        assertFalse(errorMsg.anyErrors);
    }

    @Test
    public void desugarStringEquality() {
        Parser parser = new CupParser("\"foo\" = \"bar\"",
                new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, true);
        program.accept(prettyPrinter);
        assertFalse(errorMsg.anyErrors);
    }

    @Test
    public void desugarStringLess() {
        Parser parser = new CupParser("\"foo\" < \"bar\"",
                new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, true);
        program.accept(prettyPrinter);
        assertFalse(errorMsg.anyErrors);
    }

    @Test
    public void desugarFor() {
        Parser parser = new CupParser("for i := 0 to 10 do print_int(i)",
                new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, true);
        program.accept(prettyPrinter);
        assertFalse(errorMsg.anyErrors);
    }

    @Test
    public void voidTest() {
        Parser parser = new CupParser("let var void1 := () var void2 := () var void3 := () in void1 :=  void2 := void3 := () end",
                new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        program.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, true);
        program.accept(prettyPrinter);
        assertFalse(errorMsg.anyErrors);
    }






}