package com.chaosopher.tigerlang.compiler.dataflow.exp;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.chaosopher.tigerlang.compiler.dataflow.GenKillSets;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.Exp;
import com.chaosopher.tigerlang.compiler.tree.Lexer;
import com.chaosopher.tigerlang.compiler.tree.Parser;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.tree.BINOP;
import com.chaosopher.tigerlang.compiler.tree.CONST;
import com.chaosopher.tigerlang.compiler.tree.Exp;
import com.chaosopher.tigerlang.compiler.tree.Lexer;
import com.chaosopher.tigerlang.compiler.tree.Parser;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.tree.TEMP;



import org.junit.Test;

public class AvailableExpressionsTest {
    
    @Test
    public void createInstance() throws IOException {
        //https://www.cs.umd.edu/class/spring2013/cmsc631/lectures/data-flow.pdf
        String code = 
            "label(start) " + 
            "move(temp(x), binop(PLUS, temp(a), temp(b))) " +  // gen( a + b, ) ;  kill ()
            "move(temp(y), binop(MUL, temp(a), temp(b))) " +  // get( a * b, ) ; kill ()
            "label(l1) " + 
            "cjump(LE, temp(y), temp(a), l2, end) " + 
            "label(l2) " + 
            "move(temp(a), binop(PLUS, temp(a), const(1))) " +  // gen ( ), kill ( a * b, a + b)
            "move(temp(x), binop(PLUS, temp(a), temp(b))) " +  // gen ( a + b, ), kill ()
            "jump(name(l1))" + 
            "label(end)";
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Exp> genKillSets = AEGenKillSets.analyse(cfg);
        //AvailableExpressions availableExpressions = AvailableExpressions.analyze(cfg, genKillSets);
        AEDataFlow availableExpressions = AEDataFlow.analyze(cfg, genKillSets);
        availableExpressions.toStream(System.out);
        // test the in and out sets.
        assertTrue(availableExpressions.compareIn(1, new HashSet<>()));
        assertTrue(availableExpressions.compareOut(1, new HashSet<>()));

        assertTrue(availableExpressions.compareIn(2, new HashSet<>()));
        assertTrue(availableExpressions.compareOut(2, Stream.of(
            new BINOP(
                BINOP.PLUS, 
                new TEMP(Temp.create("a")), 
                new TEMP(Temp.create("b"))
            )

        ).collect(Collectors.toCollection(HashSet::new))));

    }
}