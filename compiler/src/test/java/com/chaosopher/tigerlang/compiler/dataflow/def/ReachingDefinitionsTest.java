package com.chaosopher.tigerlang.compiler.dataflow.def;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.chaosopher.tigerlang.compiler.dataflow.GenKillSets;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.tree.Lexer;
import com.chaosopher.tigerlang.compiler.tree.Parser;
import com.chaosopher.tigerlang.compiler.tree.StmList;

import org.junit.Test;

public class ReachingDefinitionsTest {

    @Test
    public void def() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(a), const(5)) " + 
            "label(end) ";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Integer> genKillSets = DefGenKillSets.analyse(cfg);
        RDDataFlow defintionsDataFlow = RDDataFlow.analyze(cfg, genKillSets);
        defintionsDataFlow.toStream(System.out);
    }

    @Test
    public void defdef() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(a), const(5)) " + 
            "move(temp(b), const(7)) " + 
            "label(end) ";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Integer> genKillSets = DefGenKillSets.analyse(cfg);
        RDDataFlow defintionsDataFlow = RDDataFlow.analyze(cfg, genKillSets);
        defintionsDataFlow.toStream(System.out);
    }

    @Test
    public void defkill() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(a), const(5)) " + 
            "move(temp(a), const(7)) " + 
            "label(end) ";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Integer> genKillSets = DefGenKillSets.analyse(cfg);
        RDDataFlow defintionsDataFlow = RDDataFlow.analyze(cfg, genKillSets);
        defintionsDataFlow.toStream(System.out);
    }

    @Test
    public void deatomizeTest() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(t1), binop(PLUS, temp(a), temp(b))) " + 
            "move(temp(t2), binop(PLUS, temp(c), temp(t1))) " + 
            "label(end) ";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Integer> genKillSets = DefGenKillSets.analyse(cfg);
        RDDataFlow defintionsDataFlow = RDDataFlow.analyze(cfg, genKillSets);
        defintionsDataFlow.toStream(System.out);
        // gen kill will tell us how many definitions there are of a particular temporary.
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
        RDDataFlow defintionsDataFlow = RDDataFlow.analyze(cfg, genKillSets);
        defintionsDataFlow.toStream(System.out);
    }
}