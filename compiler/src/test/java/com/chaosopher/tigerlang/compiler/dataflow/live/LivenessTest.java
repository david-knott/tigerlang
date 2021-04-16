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

public class LivenessTest {
    
    /*
    @Test
    public void use() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(t1), binop(PLUS, temp(a), temp(b))) " + 
            "label(end)";
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Temp> genKillSets = LiveGenKillSets.analyse(cfg);
        Liveness liveness = Liveness.analyze(cfg, genKillSets, null);
        liveness.toStream(System.out);

        assertTrue(liveness.compareIn(1, Stream.of(
            Temp.create("a"),
            Temp.create("b")
        ).collect(Collectors.toCollection(HashSet::new))));
        assertTrue(liveness.compareOut(1, Stream.of(
            Temp.create("a"),
            Temp.create("b")
        ).collect(Collectors.toCollection(HashSet::new))));

        assertTrue(liveness.compareIn(3, new HashSet<>()));
        assertTrue(liveness.compareIn(3, new HashSet<>()));
    }*/


}
