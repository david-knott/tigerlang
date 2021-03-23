package com.chaosopher.tigerlang.compiler.dataflow.live;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
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
        GenKillSets genKillSets = GenKillSets.analyse(cfg);
        genKillSets.serialize(System.out);
        //genKillSets.getDefId(1).getKill().size() == 2
        //genKillSets.getDefId(1).getKill().contains(new RQuad('a', '+', 'b'))
        //genKillSets.getDefId(1).getKill().contains(new RQuad('a', '*', 'b'))
        // b + c available at end of block start
    }
}
