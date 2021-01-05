package com.chaosopher.tigerlang.compiler.inlining;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.bind.Binder;
import com.chaosopher.tigerlang.compiler.bind.Renamer;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class PrunerRegTest {

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
        System.out.println("Testing Inliner:" + fileName);
        ErrorMsg errorMsg = new ErrorMsg("f", System.out);
        ParserService parserService = new ParserService(new ParserFactory());
        try (FileInputStream fin = new FileInputStream(fileName)) {
            Absyn program = parserService.parse(fin, errorMsg);
            program.accept(new Binder(errorMsg));
            program.accept(new Renamer());
           // program.accept(new PrettyPrinter(System.out));
            Inliner inliner = new Inliner(program);
            program.accept(inliner);
            inliner.visitedDecList.accept(new Binder(errorMsg));
            Pruner pruner = new Pruner(inliner.visitedDecList);
            inliner.visitedDecList.accept(pruner);
            System.out.println("pruned " + pruner.pruneCount);
            pruner.visitedDecList.accept(new PrettyPrinter(System.out));
        }   catch(IOException e) {
            e.printStackTrace();
        } 
    }
}