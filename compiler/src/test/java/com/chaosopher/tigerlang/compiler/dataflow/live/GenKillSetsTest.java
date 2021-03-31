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
    public void testBinop() throws IOException {
        // https://www.seas.harvard.edu/courses/cs252/2011sp/slides/Lec02-Dataflow.pdf
        String code = 
            "label(start) " + 
            "move(temp(x), binop(PLUS, temp(a), temp(b))) " +  // gen(a, b), kill()
            "move(temp(y), binop(MUL, temp(a), temp(b))) " +  // get(a, b), kill()
            "label(l1) " + 
            "cjump(LE, temp(y), temp(a), l3, l2) " + // gen(y, a), kill()
            "move(temp(a), binop(PLUS, temp(a), const(1))) " +  // gen(a), kill(a)
            "move(temp(x), binop(PLUS, temp(a), temp(b))) " +  // gen(a, b), kill(x)
            "jump(name(l1))" + 
            "label(end)";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Temp> genKillSets = LiveGenKillSets.analyse(cfg);
        genKillSets.serialize(System.out);
        assertTrue(genKillSets.compareGen(2, Stream.of(
            Temp.create("a"),
            Temp.create("b")
        ).collect(Collectors.toCollection(HashSet::new))));
        //assertTrue(genKillSets.compareKill(2, new HashSet<>()));

    }
}
