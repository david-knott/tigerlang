package com.chaosopher.tigerlang.compiler.dataflow.exp;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.tree.Lexer;
import com.chaosopher.tigerlang.compiler.tree.Parser;
import com.chaosopher.tigerlang.compiler.tree.StmList;

import org.junit.Test;

public class AvailableExpressionsTest {
    
    @Test
    public void createInstance() throws IOException {
        //https://www.cs.umd.edu/class/spring2013/cmsc631/lectures/data-flow.pdf
        String code = 
            "label(start) " + 
            "move(temp(x), binop(PLUS, temp(a), temp(b))) " +  // gen( a - b, ) ;  kill ()
            "move(temp(y), binop(MUL, temp(a), temp(b))) " +  // get( a * b, ) ; kill ()
            "label(l1) " + 
            "cjump(LE, temp(y), temp(a), l2, end) " + 
            "label(l2) " + 
            "move(temp(a), binop(PLUS, temp(a), const(1))) " +  // gen ( ), kill ( a - b, a * b, a + b)
            "move(temp(x), binop(PLUS, temp(a), temp(b))) " +  // gen ( a + b, ), kill ()
            "jump(name(l1))" + 
            "label(end)";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets genKillSets = GenKillSets.analyse(cfg);
        AvailableExpressions availableExpressions = AvailableExpressions.analyze(cfg, genKillSets);
        availableExpressions.serialize(System.out);
    }
}