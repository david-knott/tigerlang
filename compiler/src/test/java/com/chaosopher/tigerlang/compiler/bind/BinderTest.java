package com.chaosopher.tigerlang.compiler.bind;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.PrintStream;
import java.util.Arrays;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;

@RunWith(Theories.class)
public class BinderTest {

    @DataPoints
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
            {"let type a = int var a:a := 1 in a end", false},
            {"let type a = int var a:b := 1 in a end", true},
            {"let type intArray = array of int var a := intArray [ 10 ] of 3 in a[3] end", false},
            {"let type intArray = array of int var a := badArray [ 10 ] of 3 in a[3] end", true},
            {"let type intArray = array of bint var a := intArray [ 10 ] of 3 in a[3] end", true},
            {"let type intArray = array of bint var a := badArray [ 10 ] of 3 in a[3] end", true},
            {"let type tuple = {first: int, second: int} var t1: tuple := tuple {first = 1, second = 2} in t1.first end", false},
            {"let type list = {first: int, rest: list} in 1 end", false},
            {"let var a:int := 0 in a + a end", false},
            {"let var a:int := 3 + 4 in a end", false},
            {"let var a := 0 in a end", false},
            {"let function a() = (1) in a() end", false},
            {"let function a():int = 1 in a() end", false},
            {"let function a():int = 1 in b() end", true},
            {"let function a(a: int, b:int):int = a + b in a(3,4) end", false},
            {"let function a() = let var b:int := 1 + 3 in end in a() end", false},
            {"let var x := 3 in while 1 do ( for i := 3 to 10 do ( x := x + i; if x >= 42 then break ); if x >= 51 then break ) end", false},
            {"let var x:int := 3 in if x >= 42 then break end", true},
            {"let var me := 0 in me end", false},
            {"let var me := 0 function id(me : int) : int = me in printi(me) end", false},
            {"let type me = {} type me = {} function twice(a: int, a: int) : int = a + a in me {} = me {} end", true},
            {"let var x := 0 in while 1 do ( for i := 0 to 10 do ( x := x + i; if x >= 42 then break ); if x >= 51 then break ) end", false},
            {"let type box = { value : int } type dup = { value : int, value : string } var box := box { value = 51 } in box.head end", false},
            {"let type rec = { a : unknown } in rec { a = 42 } end", true}
        });
    }

    private ParserService parserService;

    public BinderTest() {
        parserService = new ParserService(new ParserFactory());
    }

    @Theory
    public void template(Object[] data) {
        String code = (String)data[0];
        boolean res = (boolean)data[1];
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("binder-test", outputStream);
        System.out.println("Testing:" + code);
        Absyn program = parserService.parse(code, errorMsg);
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        assertEquals(res, errorMsg.anyErrors, code);
    }
}