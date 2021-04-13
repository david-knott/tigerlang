package com.chaosopher.tigerlang.compiler.dataflow.dead;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.chaosopher.tigerlang.compiler.dataflow.GenKillSets;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.dataflow.live.LiveGenKillSets;
import com.chaosopher.tigerlang.compiler.dataflow.live.LivenessDataFlow;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.translate.FragList;
import com.chaosopher.tigerlang.compiler.translate.FragmentPrinter;
import com.chaosopher.tigerlang.compiler.translate.FragmentVisitor;
import com.chaosopher.tigerlang.compiler.translate.ProcFrag;
import com.chaosopher.tigerlang.compiler.tree.Lexer;
import com.chaosopher.tigerlang.compiler.tree.Parser;
import com.chaosopher.tigerlang.compiler.tree.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.tree.StmList;

import org.junit.Test;


public class DeadCodeRemovalTest {

    @Test
    public void first() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(t1), binop(PLUS, temp(a), temp(b))) " +  //dead 
            "move(temp(t2), binop(MUL, temp(b), temp(c))) " +  
            "label(end)";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        CFG cfg = CFG.build(stmList);
        GenKillSets<Temp> genKillSets = LiveGenKillSets.analyse(cfg);
        LivenessDataFlow livenessDataFlow = LivenessDataFlow.analyze(cfg, genKillSets);
        DeadCodeRemoval deadCodeRemoval = new DeadCodeRemoval(livenessDataFlow);
        stmList.accept(deadCodeRemoval);
        stmList.accept( new PrettyPrinter(System.out));
        //FragmentPrinter.apply(stmList ,System.out);
    }
    
}
