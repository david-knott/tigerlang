package com.chaosopher.tigerlang.compiler.dataflow.live;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.chaosopher.tigerlang.compiler.dataflow.GenKillSets;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.Lexer;
import com.chaosopher.tigerlang.compiler.tree.Parser;
import com.chaosopher.tigerlang.compiler.tree.StmList;

import org.junit.Test;

public class GenKillSetsTest {

    @Test
    public void genKill() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(t1), binop(PLUS, const(1), temp(a))) " +
            "label(end)";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Temp> genKillSets = LiveGenKillSets.analyse(cfg);
        genKillSets.serialize(System.out);
        assertTrue(genKillSets.compareGen(2, Stream.of(
            Temp.create("a")
        ).collect(Collectors.toCollection(HashSet::new))));
        assertTrue(genKillSets.compareKill(2, Stream.of(
            Temp.create("t1")
        ).collect(Collectors.toCollection(HashSet::new))));
    }

    @Test
    public void genKillKill() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(t1), binop(PLUS, const(1), temp(t1))) " +
            "label(end)";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Temp> genKillSets = LiveGenKillSets.analyse(cfg);
        genKillSets.serialize(System.out);
        assertTrue(genKillSets.compareGen(2, new HashSet<>()));
        assertTrue(genKillSets.compareKill(2, Stream.of(
            Temp.create("t1")
        ).collect(Collectors.toCollection(HashSet::new))));
    }
}
