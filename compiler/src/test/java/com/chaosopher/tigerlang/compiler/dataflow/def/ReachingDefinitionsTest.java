package com.chaosopher.tigerlang.compiler.dataflow.def;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.tree.Lexer;
import com.chaosopher.tigerlang.compiler.tree.Parser;
import com.chaosopher.tigerlang.compiler.tree.StmList;

import org.junit.Test;

public class ReachingDefinitionsTest {

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
        GenKillSets genKillSets = GenKillSets.analyse(cfg);
        ReachingDefinitions reachingDefinitions = ReachingDefinitions.analyze(cfg, genKillSets);
        reachingDefinitions.serialize(System.out);
        
        assertTrue(reachingDefinitions.getIn(1).size() == 0);
        assertTrue(reachingDefinitions.getOut(1).size() == 1);
        assertTrue(reachingDefinitions.getOut(1).contains(1));

        assertTrue(reachingDefinitions.getIn(6).size() == 3);
        assertTrue(reachingDefinitions.getIn(6).contains(1));
        assertTrue(reachingDefinitions.getIn(6).contains(2));
        assertTrue(reachingDefinitions.getIn(6).contains(4));
        assertTrue(reachingDefinitions.getOut(6).size() == 3);
        assertTrue(reachingDefinitions.getOut(6).contains(2));
        assertTrue(reachingDefinitions.getOut(6).contains(4));
        assertTrue(reachingDefinitions.getOut(6).contains(6));

        assertTrue(reachingDefinitions.getIn(7).size() == 3);
        assertTrue(reachingDefinitions.getIn(7).contains(2));
        assertTrue(reachingDefinitions.getIn(7).contains(4));
        assertTrue(reachingDefinitions.getIn(7).contains(6));
        assertTrue(reachingDefinitions.getOut(7).size() == 2);
        assertTrue(reachingDefinitions.getOut(7).contains(6));
        assertTrue(reachingDefinitions.getOut(7).contains(7));
    }
}