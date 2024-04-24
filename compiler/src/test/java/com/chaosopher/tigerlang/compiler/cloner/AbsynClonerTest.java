package com.chaosopher.tigerlang.compiler.cloner;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;

import org.junit.Test;

public class AbsynClonerTest {

    @Test
    public void typeDef() throws FileNotFoundException {
        ErrorMsg errorMsg = new ErrorMsg("f", System.out);
        ParserService parserService = new ParserService(new ParserFactory());
        Absyn program = parserService.parse("let type a = int var a:a := 1 in a end", errorMsg);
        AbsynCloner absynCloner = new AbsynCloner();
        program.accept(absynCloner);
        //create an outstream and write new pretty printed program to it
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrettyPrinter prettyPrinter = new PrettyPrinter(new PrintStream(outputStream));
        absynCloner.visitedDecList.accept(prettyPrinter);
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        //System.out.println(new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
        Absyn program2 = parserService.parse(inputStream, errorMsg);
        assertNotNull(program2);
    }
}