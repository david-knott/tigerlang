package com.chaosopher.tigerlang.compiler.bugs;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.FileNotFoundException;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.bind.Binder;
import com.chaosopher.tigerlang.compiler.canon.CanonVisitor;
import com.chaosopher.tigerlang.compiler.canon.CanonicalizationImpl;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.findescape.EscapeVisitor;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;
import com.chaosopher.tigerlang.compiler.translate.FragList;
import com.chaosopher.tigerlang.compiler.translate.TranslatorVisitor;
import com.chaosopher.tigerlang.compiler.types.TypeChecker;

import org.junit.Test;

public class BugTest {
     


    @Test
    public void twoJumps() {
        ErrorMsg errorMsg = new ErrorMsg("f", System.out);
        ParserService parserService = new ParserService(new ParserFactory());
        Absyn program = parserService.parse("while 101 do (if 102 then break)", errorMsg);
        program.accept(new Binder(errorMsg));
        program.accept(new TypeChecker(errorMsg));
        TranslatorVisitor translator = new TranslatorVisitor();
        program.accept(translator);
        CanonVisitor canonVisitor = new CanonVisitor(new CanonicalizationImpl());
        translator.getFragList().accept(canonVisitor);
        FragList fragList = canonVisitor.fragList;
        
        assertNotNull(program);
    }

    @Test
    public void syntaxError() throws FileNotFoundException {
        ErrorMsg errorMsg = new ErrorMsg("f", System.out);
        ParserService parserService = new ParserService(new ParserFactory());
        Absyn program = parserService.parse("print(\"Hello World\")dd", errorMsg);
        assertNotNull(program);
    }

    @Test
    public void argumentMismatch() throws FileNotFoundException {
        ErrorMsg errorMsg = new ErrorMsg("f", System.out);
        ParserService parserService = new ParserService(new ParserFactory());
        Absyn program = parserService.parse("let function a(a:int, b:int, c:int) = () in a(1,2) end", errorMsg);
        program.accept(new Binder(errorMsg));
        program.accept(new TypeChecker(errorMsg));
        assertNotNull(program);
    }

    @Test
    public void bug20202012191() throws FileNotFoundException {
        ErrorMsg errorMsg = new ErrorMsg("f", System.out);
        ParserService parserService = new ParserService(new ParserFactory());
        Absyn program = parserService.parse(" let var a:int := 10 in while a > 0 do if a = 5 then break else a := a - 1; a end", errorMsg);
        program.accept(new Binder(errorMsg));
        program.accept(new TypeChecker(errorMsg));
        assertNotNull(program);
    }

    @Test
    public void bug20202012192() throws FileNotFoundException {
        ErrorMsg errorMsg = new ErrorMsg("f", System.out);
        ParserService parserService = new ParserService(new ParserFactory());
        Absyn program = parserService.parse(" let var a:string := \"david\" var b:string := \"david\" in printi(a = b) end", errorMsg);
        program.accept(new Binder(errorMsg));
        program.accept(new TypeChecker(errorMsg));
        assertNotNull(program);
    }

    @Test
    public void bug20202012193() throws FileNotFoundException {
        ErrorMsg errorMsg = new ErrorMsg("f", System.out);
        ParserService parserService = new ParserService(new ParserFactory());
        Absyn program = parserService.parse(" let type list = {first: int, second:int, rest: list} var myList:list := list{first = 10, second = 20, rest=nil} in myList.first := 20; myList.second := 21; printi(myList.first); printi(myList.second) end ", errorMsg);
        program.accept(new Binder(errorMsg));
        program.accept(new TypeChecker(errorMsg));
        assertNotNull(program);
    }

    @Test
    public void ifExpressionNullType() throws FileNotFoundException {
        ErrorMsg errorMsg = new ErrorMsg("f", System.out);
        ParserService parserService = new ParserService(new ParserFactory());
        Absyn program = parserService.parse("/* bubble sort, keep looping through array swapping adgacent elements until we do a complete loop without any swaps */ let type iat = array of int var src := iat[10] of 0 function bubblesort(ar:iat)  = ( let var first:int := 0 var second:int := 0 var temp:int := 0 var swaps:int := 1 /* boolean swaps */ in ( while swaps do ( swaps := 0; ( for i := 1 to 10 do ( first := ar[i - 1]; second := ar[i]; if ar[first] > ar[second] then ( temp := ar[second]; ar[first] := ar[second]; ar[second] := temp; swaps := 1 ) ) ) ) ) end ) in (   src[0] := 8; src[1] := 8; src[2] := 2; src[3] := 6; src[4] := 4; bubblesort(src) ) end", errorMsg);
        assertNotNull(program);
        program.accept(new EscapeVisitor(errorMsg));
        TranslatorVisitor translator = new TranslatorVisitor();
        program.accept(new Binder(errorMsg));
        program.accept(new TypeChecker(errorMsg));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        assertNotNull(fragList);
    }

}
