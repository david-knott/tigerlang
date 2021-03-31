package com.chaosopher.tigerlang.compiler.dataflow.def;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.chaosopher.tigerlang.compiler.dataflow.GenKillSets;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.tree.Lexer;
import com.chaosopher.tigerlang.compiler.tree.Parser;
import com.chaosopher.tigerlang.compiler.tree.StmList;

import org.junit.Test;


public class GenKillSetsTest {
    
    @Test
    public void singleGen() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(a), const(5)) ";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Integer> genKillSets = DefGenKillSets.analyse(cfg);
        genKillSets.serialize(System.out);
        assertTrue(genKillSets.compareGen(2, Stream.of(2).collect(Collectors.toCollection(HashSet::new))));
        assertTrue(genKillSets.compareKill(2, new HashSet<>()));
    }

    @Test
    public void twoGen() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(a), const(5)) " +
            "move(temp(c), const(5)) "
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Integer> genKillSets = DefGenKillSets.analyse(cfg);
        genKillSets.serialize(System.out);
        assertTrue(genKillSets.compareGen(2, Stream.of(2).collect(Collectors.toCollection(HashSet::new))));
        assertTrue(genKillSets.compareKill(2, new HashSet<>()));
        assertTrue(genKillSets.compareGen(3, Stream.of(3).collect(Collectors.toCollection(HashSet::new))));
        assertTrue(genKillSets.compareKill(3, new HashSet<>()));
    }

    @Test
    public void oneGenOneKill() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(a), const(5)) " +
            "move(temp(a), const(9)) "
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Integer> genKillSets = DefGenKillSets.analyse(cfg);
        genKillSets.serialize(System.out);
        assertTrue(genKillSets.compareGen(2, Stream.of(2).collect(Collectors.toCollection(HashSet::new))));
        assertTrue(genKillSets.compareKill(2, Stream.of(3).collect(Collectors.toCollection(HashSet::new))));
        assertTrue(genKillSets.compareGen(3, Stream.of(3).collect(Collectors.toCollection(HashSet::new))));
        assertTrue(genKillSets.compareKill(3, Stream.of(2).collect(Collectors.toCollection(HashSet::new))));
    }

    @Test
    public void createInstance() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(a), const(5)) " + 
            "move(temp(c), const(1)) " + 
            "label(l1) " + 
            "cjump(GT, temp(c), temp(a), l3, l2) " +
            "label(l2) " + 
            "move(temp(c), binop(PLUS, temp(c), temp(c))) " + 
            "jump(name(l1))" + 
            "label(l3) " + 
            "move(temp(a), binop(MINUS, temp(c), temp(a))) " + 
            "move(temp(c), const(0)) ";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Integer> genKillSets = DefGenKillSets.analyse(cfg);
        genKillSets.serialize(System.out);
        assertTrue(genKillSets.compareGen(2, Stream.of(2).collect(Collectors.toCollection(HashSet::new))));
        assertTrue(genKillSets.compareKill(2, Stream.of(10).collect(Collectors.toCollection(HashSet::new))));
        assertTrue(genKillSets.compareGen(3, Stream.of(3).collect(Collectors.toCollection(HashSet::new))));
        assertTrue(genKillSets.compareKill(3, Stream.of(7, 11).collect(Collectors.toCollection(HashSet::new))));
        assertTrue(genKillSets.compareGen(7, Stream.of(7).collect(Collectors.toCollection(HashSet::new))));
        assertTrue(genKillSets.compareKill(7, Stream.of(3, 11).collect(Collectors.toCollection(HashSet::new))));
    }
}
