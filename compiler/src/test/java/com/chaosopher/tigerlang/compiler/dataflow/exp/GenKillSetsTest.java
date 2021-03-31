package com.chaosopher.tigerlang.compiler.dataflow.exp;


import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
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

public class GenKillSetsTest {
    
    @Test
    public void testLoop() throws IOException {
        // https://www.seas.harvard.edu/courses/cs252/2011sp/slides/Lec02-Dataflow.pdf
        String code = 
            "label(start) " + 
            "move(temp(x), binop(PLUS, temp(a), temp(b))) " +  // gen( a + b, ) ;  kill ()
            "move(temp(y), binop(MUL, temp(a), temp(b))) " +  // get( a * b, ) ; kill ()
            "label(l1) " + 
            "cjump(LE, temp(y), temp(a), l3, l2) " + // nothing 
            "move(temp(a), binop(PLUS, temp(a), const(1))) " +  // gen ( ), kill ( a + b, a * b, a + 1)
            "move(temp(x), binop(PLUS, temp(a), temp(b))) " +  // gen ( a + b, ), kill ()
            "jump(name(l1))" + 
            "label(end)";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Exp> genKillSets = AEGenKillSets.analyse(cfg);
        genKillSets.serialize(System.out);

        assertTrue(genKillSets.compareGen(1, new HashSet<>()));
        assertTrue(genKillSets.compareKill(1, new HashSet<>()));

        assertTrue(genKillSets.compareGen(2, Stream.of(
            new BINOP(
                BINOP.PLUS, 
                new TEMP(Temp.create("a")), 
                new TEMP(Temp.create("b"))
            )
        ).collect(Collectors.toCollection(HashSet::new))));
        assertTrue(genKillSets.compareKill(2, new HashSet<>()));

        assertTrue(genKillSets.compareGen(3, Stream.of(
            new BINOP(
                BINOP.MUL, 
                new TEMP(Temp.create("a")), 
                new TEMP(Temp.create("b"))
            )
        ).collect(Collectors.toCollection(HashSet::new))));
        assertTrue(genKillSets.compareKill(3, new HashSet<>()));

        assertTrue(genKillSets.compareGen(4, new HashSet<>()));
        assertTrue(genKillSets.compareKill(4, new HashSet<>()));

        assertTrue(genKillSets.compareGen(5, new HashSet<>()));
        assertTrue(genKillSets.compareKill(5, new HashSet<>()));

        assertTrue(genKillSets.compareGen(6, new HashSet<>()));
        assertTrue(genKillSets.compareKill(6, Stream.of(
            new BINOP(
                BINOP.MUL, 
                new TEMP(Temp.create("a")), 
                new TEMP(Temp.create("b"))
            ),
            new BINOP(
                BINOP.PLUS, 
                new TEMP(Temp.create("a")), 
                new TEMP(Temp.create("b"))
            ),
            new BINOP(
                BINOP.PLUS, 
                new TEMP(Temp.create("a")), 
                new CONST(1)
            )
        ).collect(Collectors.toCollection(HashSet::new))));
        








        
        //genKillSets.getDefId(1).getKill().size() == 2
        //genKillSets.getDefId(1).getKill().contains(new RQuad('a', '+', 'b'))
        //genKillSets.getDefId(1).getKill().contains(new RQuad('a', '*', 'b'))
        // b + c available at end of block start
    }

    @Test
    public void testMem() throws IOException {
        String code = 
            "label(start) " + 
            "move(mem(temp(z)), temp(x)) " +  // gen {}, kill { mem(temp(z), mem(temp(b)))}
            "move(temp(y), mem(temp(z))) " +  // gen { mem(temp(z)), }
            "move(temp(a), mem(temp(b)) " +  // gen { mem(temp(b)), }
            "label(end)";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Exp> genKillSets = AEGenKillSets.analyse(cfg);
        genKillSets.serialize(System.out);
    }

    @Test
    public void functionTest() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(a), call(name(x))) " +  // gen {}, kill { mem(temp(z)), a + b } 
            "move(temp(y), mem(temp(z))) " +  // gen { mem(temp(z)), } kill {}
            "move(temp(c), binop(PLUS, temp(a), temp(b))) " +  // gen { a + b, } kill {}
            "label(end)";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Exp> genKillSets = AEGenKillSets.analyse(cfg);
        genKillSets.serialize(System.out);
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
        GenKillSets<Exp> genKillSets = AEGenKillSets.analyse(cfg);
        genKillSets.serialize(System.out);
    }



}
