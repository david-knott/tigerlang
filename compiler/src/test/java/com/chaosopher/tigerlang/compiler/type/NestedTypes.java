package com.chaosopher.tigerlang.compiler.type;

import static org.junit.Assert.assertFalse;

import java.io.PrintStream;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.bind.Binder;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.parse.CupParser;
import com.chaosopher.tigerlang.compiler.parse.Parser;
import com.chaosopher.tigerlang.compiler.types.TypeChecker;

import org.junit.Test;

public class NestedTypes {
    
    @Test
    public void arrayInRecord() {
        Parser parser = new CupParser(" let type intArray = array of int type Array = {elts:intArray, size:int} var size:int := 10 var x:=Array{elts=intArray[size] of 0, size=size} in x.elts[0] end",
                new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        TypeChecker.create(program, errorMsg);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, true);
        program.accept(prettyPrinter);
        assertFalse(errorMsg.anyErrors);
    }
}