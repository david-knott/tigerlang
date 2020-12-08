package com.chaosopher.tigerlang.compiler.cloner;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;

@RunWith(Theories.class)
public class AnsynClonerRegTest {

    @DataPoints
    public static String[] paths() {
        try (Stream<Path> walk = Files.walk(Paths.get("./src/test/java/com/chaosopher/tigerlang/compiler/fixtures/"))) {
            return walk.filter(Files::isRegularFile).map(x -> x.toString())
            .filter(f -> f.endsWith(".tig"))
            .collect(Collectors.toList()).toArray(String[]::new);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Theory
    public void good(String fileName) {
        System.out.println("Testing Cloner " + fileName);
        ErrorMsg errorMsg = new ErrorMsg("f", System.out);
        ParserService parserService = new ParserService(new ParserFactory());
        parserService.configure(config -> config.setNoPrelude(false));
        parserService.configure(config -> config.setParserTrace(false));
        try (FileInputStream fin = new FileInputStream(fileName)) {
            Absyn program = parserService.parse(fin, errorMsg);
            AbsynCloner absynCloner = new AbsynCloner();
            program.accept(absynCloner);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrettyPrinter prettyPrinter = new PrettyPrinter(new PrintStream(outputStream));
            absynCloner.visitedDecList.accept(prettyPrinter);
            InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            System.out.println(new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
            Absyn program2 = parserService.parse(inputStream, errorMsg);
            assertNotNull(program2);
        }   catch(IOException e) {
            e.printStackTrace();
        } 
    }
}
