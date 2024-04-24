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

public class ReachingExpressionsTest {
    
    @Test
    public void def() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(x), binop(PLUS, temp(a), temp(b))) " +  // gen( a + b, ) ;  kill ()
            "label(end)";
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Integer> reGenKillSets = REGenKillSets.analyse(cfg);
        REDataFlow reachingExpressions = REDataFlow.analyze(cfg, reGenKillSets);
        reachingExpressions.toStream(System.out);
        assertTrue(reachingExpressions.compareIn(3, Stream.of(
            2
        ).collect(Collectors.toCollection(HashSet::new))));

        assertTrue(reachingExpressions.compareOut(3, Stream.of(
            2
        ).collect(Collectors.toCollection(HashSet::new))));
    }

    @Test
    public void kills() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(x), binop(PLUS, temp(a), temp(b))) " +  // def s2
            "move(temp(a), binop(PLUS, temp(a), const(1))) " +  // kills s2 as assigns to a
            "label(end)";
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Integer> reGenKillSets = REGenKillSets.analyse(cfg);
        REDataFlow reachingExpressions = REDataFlow.analyze(cfg, reGenKillSets);
        reachingExpressions.toStream(System.out);
        assertTrue(reachingExpressions.compareIn(4, new HashSet<>()));
        assertTrue(reachingExpressions.compareOut(4, new HashSet<>()));
    }


    @Test
    public void recompute() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(x), binop(PLUS, temp(a), temp(b))) " +  //first def 2
            "move(temp(y), binop(PLUS, temp(a), temp(b))) " +  //second def 3
            "label(end)";
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Integer> reGenKillSets = REGenKillSets.analyse(cfg);
        REDataFlow reachingExpressions = REDataFlow.analyze(cfg, reGenKillSets);
        reachingExpressions.toStream(System.out);
        assertTrue(reachingExpressions.compareOut(4, Stream.of(
            3
        ).collect(Collectors.toCollection(HashSet::new))));
    }

    @Test
    public void meet() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(x), binop(PLUS, temp(a), temp(b))) " +  // expression
            "cjump(LE, temp(y), const(10), l1, l2) " + 
            "label(l1) " + 
            "move(temp(z0), binop(PLUS, temp(a), temp(b))) " +  // left branch, s2 should reach here, s5 out
            "jump(name(join))" + 
            "label(l2) " + 
            "move(temp(z1), binop(PLUS, temp(a), temp(b))) " +  // right branch, s2 should reach here, s7 out
            "label(join)" + //join
            "move(temp(z2), binop(PLUS, temp(a), temp(b))) " +  // main branch, in empty ?, out s10
            "label(end)"; //end
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Integer> reGenKillSets = REGenKillSets.analyse(cfg);
        REDataFlow reachingExpressions = REDataFlow.analyze(cfg, reGenKillSets);
        reachingExpressions.toStream(System.out);
    }
}