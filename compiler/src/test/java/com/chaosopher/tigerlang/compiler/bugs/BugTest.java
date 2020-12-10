package com.chaosopher.tigerlang.compiler.bugs;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.FileNotFoundException;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.bind.Binder;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.findescape.EscapeVisitor;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;
import com.chaosopher.tigerlang.compiler.translate.FragList;
import com.chaosopher.tigerlang.compiler.translate.TranslatorVisitor;

import org.junit.Test;

public class BugTest {
    
    @Test
    public void ifExpressionNullType() throws FileNotFoundException {
        ErrorMsg errorMsg = new ErrorMsg("f", System.out);
        ParserService parserService = new ParserService(new ParserFactory());
        Absyn program = parserService.parse("/* bubble sort, keep looping through array swapping adgacent elements until we do a complete loop without any swaps */ let type iat = array of int var src := iat[10] of 0 function bubblesort(ar:iat)  = ( let var first:int := 0 var second:int := 0 var temp:int := 0 var swaps:int := 1 /* boolean swaps */ in ( while swaps do ( swaps := 0; ( for i := 1 to 10 do ( first := ar[i - 1]; second := ar[i]; if ar[first] > ar[second] then ( temp := ar[second]; ar[first] := ar[second]; ar[second] := temp; swaps := 1 ) ) ) ) ) end ) in (   src[0] := 8; src[1] := 8; src[2] := 2; src[3] := 6; src[4] := 4; bubblesort(src) ) end", errorMsg);
        assertNotNull(program);
        program.accept(new EscapeVisitor(errorMsg));
        TranslatorVisitor translator = new TranslatorVisitor();
        program.accept(new Binder(errorMsg));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        assertNotNull(fragList);

    }
}