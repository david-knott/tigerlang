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

    @Test
    public void basicBlockGen_TwoStatement() throws IOException {
        //https://courses.cs.washington.edu/courses/csep501/18sp/lectures/T-dataflow.pdf
        String code = 
            "label(start) " +  // start of first block
            "move(temp(t1), binop(MUL, const(2), temp(a))) " + // gen 2 * a 
            "move(temp(t2), binop(MUL, const(3), temp(b))) " +  // gen 3 + b
            "label(end)"; // start of second block
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Exp> genKillSets = AEGenKillSets.analyse(cfg);
        //AvailableExpressions availableExpressions = AvailableExpressions.analyze(cfg, genKillSets);
        AEDataFlow availableExpressions = AEDataFlow.analyze(cfg, genKillSets);
        availableExpressions.toStream(System.out);
        // test first in and out sets.
        assertTrue(availableExpressions.compareIn(1, new HashSet<>()));
        assertTrue(availableExpressions.compareOut(1, new HashSet<>()));

        // test last statement in and out set
        assertTrue(availableExpressions.compareIn(4, Stream.of(
            new BINOP(
                BINOP.MUL, 
                new CONST(2), 
                new TEMP(Temp.create("a"))
            ),
            new BINOP(
                BINOP.MUL, 
                new CONST(3), 
                new TEMP(Temp.create("b"))
            )

        ).collect(Collectors.toCollection(HashSet::new))));
        assertTrue(availableExpressions.compareOut(4, Stream.of(
            new BINOP(
                BINOP.MUL, 
                new CONST(2), 
                new TEMP(Temp.create("a"))
            ),
            new BINOP(
                BINOP.MUL, 
                new CONST(3), 
                new TEMP(Temp.create("b"))
            )
        ).collect(Collectors.toCollection(HashSet::new))));
    }

    @Test
    public void basicBlockGen_FourStatement() throws IOException {
        //https://courses.cs.washington.edu/courses/csep501/18sp/lectures/T-dataflow.pdf
        String code = 
            "label(start) " +  // start of first block
            "move(temp(t1), binop(MUL, temp(x), temp(b))) " + 
            "move(temp(t2), binop(MUL, temp(a), temp(c))) " + 
            "move(temp(t3), binop(MUL, temp(a), temp(b))) " + 
            "move(temp(t4), binop(MUL, temp(a), temp(b))) " + 
            "label(end)"; // start of second block
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Exp> genKillSets = AEGenKillSets.analyse(cfg);
        //AvailableExpressions availableExpressions = AvailableExpressions.analyze(cfg, genKillSets);
        AEDataFlow availableExpressions = AEDataFlow.analyze(cfg, genKillSets);
        availableExpressions.toStream(System.out);
        // test first in and out sets.
        assertTrue(availableExpressions.compareIn(1, new HashSet<>()));
        assertTrue(availableExpressions.compareOut(1, new HashSet<>()));
    }



    @Test
    public void basicBlockGen_ThreeStatement() throws IOException {
        String code = 
            "label(start) " +  // start of first block
            "move(temp(t1), binop(MUL, const(2), temp(a))) " + // gen 2 * a 
            "move(temp(t2), binop(MUL, const(3), temp(b))) " +  // gen 3 + b
            "move(temp(t3), binop(MUL, const(4), temp(c))) " +  // gen 4 + c
            "label(end)"; // start of second block
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Exp> genKillSets = AEGenKillSets.analyse(cfg);
        //AvailableExpressions availableExpressions = AvailableExpressions.analyze(cfg, genKillSets);
        AEDataFlow availableExpressions = AEDataFlow.analyze(cfg, genKillSets);
        // test first in and out sets.
        assertTrue(availableExpressions.compareIn(1, new HashSet<>()));
        assertTrue(availableExpressions.compareOut(1, new HashSet<>()));

        // test last statement in and out set
        assertTrue(availableExpressions.compareIn(4, Stream.of(
            new BINOP(
                BINOP.MUL, 
                new CONST(2), 
                new TEMP(Temp.create("a"))
            ),
            new BINOP(
                BINOP.MUL, 
                new CONST(3), 
                new TEMP(Temp.create("b"))
            ),
            new BINOP(
                BINOP.MUL, 
                new CONST(4), 
                new TEMP(Temp.create("c"))
            )

        ).collect(Collectors.toCollection(HashSet::new))));
        assertTrue(availableExpressions.compareOut(4, Stream.of(
            new BINOP(
                BINOP.MUL, 
                new CONST(2), 
                new TEMP(Temp.create("a"))
            ),
            new BINOP(
                BINOP.MUL, 
                new CONST(3), 
                new TEMP(Temp.create("b"))
            ),
            new BINOP(
                BINOP.MUL, 
                new CONST(4), 
                new TEMP(Temp.create("c"))
            )
        ).collect(Collectors.toCollection(HashSet::new))));
    }

    @Test
    public void basicBlockKill() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(t1), binop(MUL, const(2), temp(a))) " + // gen 2 * a 
            "move(temp(a), const(1)) " +  // kills 2 * 2
            "label(end)";
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Exp> genKillSets = AEGenKillSets.analyse(cfg);
        genKillSets.serialize(System.out);
        //AvailableExpressions availableExpressions = AvailableExpressions.analyze(cfg, genKillSets);
        AEDataFlow availableExpressions = AEDataFlow.analyze(cfg, genKillSets);
        availableExpressions.toStream(System.out);
        // test the in and out sets.
        assertTrue(availableExpressions.compareIn(1, new HashSet<>()));
        assertTrue(availableExpressions.compareOut(1, new HashSet<>()));

        assertTrue(availableExpressions.compareIn(2, new HashSet<>()));
        assertTrue(availableExpressions.compareOut(2, Stream.of(
            new BINOP(
                BINOP.MUL, 
                new CONST(2), 
                new TEMP(Temp.create("a"))
            )

        ).collect(Collectors.toCollection(HashSet::new))));

        assertTrue(availableExpressions.compareIn(4, new HashSet<>()));
        assertTrue(availableExpressions.compareOut(4, new HashSet<>()));
    }


    @Test
    public void test2() throws IOException {
        //https://courses.cs.washington.edu/courses/csep501/18sp/lectures/T-dataflow.pdf
        String code = 
            "label(start) " + 
            "move(temp(t1), binop(MUL, const(2), temp(a))) " + 
            "label(l1) " + 
            "cjump(LE, temp(y), temp(a), l2, end) " + 
            "label(l2) " + 
            "move(temp(t2), binop(MUL, const(2), temp(a))) " + 
            "jump(name(l1))" + 
            "label(end)" +
            "move(temp(b), binop(MUL, const(2), temp(a))) " 
            ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Exp> genKillSets = AEGenKillSets.analyse(cfg);
        //AvailableExpressions availableExpressions = AvailableExpressions.analyze(cfg, genKillSets);
        AEDataFlow availableExpressions = AEDataFlow.analyze(cfg, genKillSets);
        availableExpressions.toStream(System.out);
        // test the in and out sets.
    }

    @Test
    public void test3() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(t1), binop(MUL, const(2), temp(a))) " + 
            "label(l1) " + 
            "cjump(LE, temp(y), temp(a), l2, end) " + 
            "label(l2) " + 
            "move(temp(t2), binop(MUL, const(2), temp(a))) " + 
            "move(temp(a), binop(MUL, const(2), temp(a))) " + 
            "jump(name(l1))" + 
            "label(end)" +
            "move(temp(b), binop(MUL, const(2), temp(a))) " 
            ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Exp> genKillSets = AEGenKillSets.analyse(cfg);
        //AvailableExpressions availableExpressions = AvailableExpressions.analyze(cfg, genKillSets);
        AEDataFlow availableExpressions = AEDataFlow.analyze(cfg, genKillSets);
        availableExpressions.toStream(System.out);
        // test the in and out sets.
    }
}