package com.chaosopher.tigerlang.compiler.dataflow.exp;

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

public class REGenKillSetsTest {
    
    @Test
    public void testExpDef() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(c), binop(PLUS, temp(a), temp(b))) " + 
            "label(end)";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Integer> genKillSets = REGenKillSets.analyse(cfg);
        genKillSets.serialize(System.out);

        assertTrue(genKillSets.compareGen(1, new HashSet<>()));
        assertTrue(genKillSets.compareKill(1, new HashSet<>()));

        assertTrue(genKillSets.compareGen(2, Stream.of(
            2
        ).collect(Collectors.toCollection(HashSet::new))));
        assertTrue(genKillSets.compareKill(2, new HashSet<>()));
    }

    @Test
    public void testNoveMoveKillOp1() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(t1), binop(PLUS, temp(a), temp(b))) " + 
            "move(temp(a), binop(PLUS, temp(c), temp(d))) " + 
            "label(end)";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Integer> genKillSets = REGenKillSets.analyse(cfg);
        genKillSets.serialize(System.out);

        // s3 changes value of a, which kills s2
        assertTrue(genKillSets.compareGen(1, new HashSet<>()));
        assertTrue(genKillSets.compareKill(1, new HashSet<>()));

        assertTrue(genKillSets.compareGen(2, Stream.of(
            2
        ).collect(Collectors.toCollection(HashSet::new))));
        assertTrue(genKillSets.compareKill(2, new HashSet<>()));

        assertTrue(genKillSets.compareGen(3, Stream.of(
            3
        ).collect(Collectors.toCollection(HashSet::new))));
        assertTrue(genKillSets.compareKill(3, Stream.of(
            2
        ).collect(Collectors.toCollection(HashSet::new))));
    }

    @Test
    public void testNoveMoveKillOp2() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(t1), binop(PLUS, temp(a), temp(b))) " + 
            "move(temp(b), binop(PLUS, temp(c), temp(d))) " + // testing assignment to b instead of a
            "label(end)";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Integer> genKillSets = REGenKillSets.analyse(cfg);
        genKillSets.serialize(System.out);

        // s3 changes value of a, which kills s2
        assertTrue(genKillSets.compareGen(1, new HashSet<>()));
        assertTrue(genKillSets.compareKill(1, new HashSet<>()));

        assertTrue(genKillSets.compareGen(2, Stream.of(
            2
        ).collect(Collectors.toCollection(HashSet::new))));
        assertTrue(genKillSets.compareKill(2, new HashSet<>()));

        assertTrue(genKillSets.compareGen(3, Stream.of(
            3
        ).collect(Collectors.toCollection(HashSet::new))));
        assertTrue(genKillSets.compareKill(3, Stream.of(
            2
        ).collect(Collectors.toCollection(HashSet::new))));
    }

    @Test
    public void testExpDefKill() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(c), binop(PLUS, temp(a), temp(c))) " + // note c is both definition and use. 
            "label(end)";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Integer> genKillSets = REGenKillSets.analyse(cfg);
        genKillSets.serialize(System.out);

        assertTrue(genKillSets.compareGen(1, new HashSet<>()));
        assertTrue(genKillSets.compareKill(1, new HashSet<>()));

        assertTrue(genKillSets.compareGen(2, new HashSet<>()));
        assertTrue(genKillSets.compareKill(2, Stream.of(
            2
        ).collect(Collectors.toCollection(HashSet::new))));
    }


    @Test
    public void testMem() throws IOException {
        String code = 
            "label(start) " + 
            "move(mem(temp(z)), temp(x)) " +  
            "move(temp(y), mem(temp(z))) " +  // both moves killed by mem[z] <- x because mem[z] might reference to same location 
            "move(temp(a), mem(temp(b)) " +   // as mem(temp(b)) or mem(temp(z))
            "label(end)";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Integer> genKillSets = REGenKillSets.analyse(cfg);
        genKillSets.serialize(System.out);

        assertTrue(genKillSets.compareGen(1, new HashSet<>()));
        assertTrue(genKillSets.compareKill(1, new HashSet<>()));

        assertTrue(genKillSets.compareGen(2, new HashSet<>()));
        assertTrue(genKillSets.compareKill(2, Stream.of(
            3, 4
        ).collect(Collectors.toCollection(HashSet::new))));

        assertTrue(genKillSets.compareGen(3 , Stream.of(
            3
        ).collect(Collectors.toCollection(HashSet::new))));
        assertTrue(genKillSets.compareKill(3, new HashSet<>()));

        assertTrue(genKillSets.compareGen(4 , Stream.of(
            4
        ).collect(Collectors.toCollection(HashSet::new))));
        assertTrue(genKillSets.compareKill(4, new HashSet<>()));
    }

    @Test
    public void functionTest() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(a), call(name(x))) " + 
            "move(temp(y), mem(temp(z))) " +  
            "move(temp(c), binop(PLUS, temp(a), temp(b))) " + 
            "label(end)";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Integer> genKillSets = REGenKillSets.analyse(cfg);
        genKillSets.serialize(System.out);
        // 2 : a <- call(x)  gen : {}, kill all mem uses and a uses { 3, 4}
        // 3 : y <- M[z] gen : {3} kill {}
        // 4 : c <- a + b gen {4} kill {}
        assertTrue(genKillSets.compareGen(1, new HashSet<>()));
        assertTrue(genKillSets.compareKill(1, new HashSet<>()));

        assertTrue(genKillSets.compareGen(2, new HashSet<>()));
        assertTrue(genKillSets.compareKill(2 , Stream.of(
            3, 4
        ).collect(Collectors.toCollection(HashSet::new))));
        
        assertTrue(genKillSets.compareGen(3 , Stream.of(
            3
        ).collect(Collectors.toCollection(HashSet::new))));
        assertTrue(genKillSets.compareKill(3, new HashSet<>()));

        assertTrue(genKillSets.compareGen(4 , Stream.of(
            4
        ).collect(Collectors.toCollection(HashSet::new))));
        assertTrue(genKillSets.compareKill(4, new HashSet<>()));
    }

    @Test
    public void procTest() throws IOException {
        String code = 
            "label(start) " + 
            "sxp(call(name(x))) " +  // gen {}, kill { mem(temp(z)), } 
            "move(temp(y), mem(temp(z))) " +  // gen { mem(temp(z)), } kill {}
            "move(temp(c), binop(PLUS, temp(a), temp(b))) " +  // gen {a + b, }
            "label(end)";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Integer> genKillSets = REGenKillSets.analyse(cfg);
        genKillSets.serialize(System.out);
        // 2 : call(x)  gen : {}, kill all mem uses {3, }
        // 3 : y <- M[z] gen : {3} kill {}
        // 4 : c <- a + b gen {4} kill {}
        assertTrue(genKillSets.compareGen(1, new HashSet<>()));
        assertTrue(genKillSets.compareKill(1, new HashSet<>()));

        assertTrue(genKillSets.compareGen(2, new HashSet<>()));
        assertTrue(genKillSets.compareKill(2 , Stream.of(
            3
        ).collect(Collectors.toCollection(HashSet::new))));
        
        assertTrue(genKillSets.compareGen(3 , Stream.of(
            3
        ).collect(Collectors.toCollection(HashSet::new))));
        assertTrue(genKillSets.compareKill(3, new HashSet<>()));

        assertTrue(genKillSets.compareGen(4 , Stream.of(
            4
        ).collect(Collectors.toCollection(HashSet::new))));
        assertTrue(genKillSets.compareKill(4, new HashSet<>()));
    }
}