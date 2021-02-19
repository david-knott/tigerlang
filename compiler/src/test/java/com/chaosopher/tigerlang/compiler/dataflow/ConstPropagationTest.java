package com.chaosopher.tigerlang.compiler.dataflow;

import com.chaosopher.tigerlang.compiler.e2e.RegressionTestBase;
import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.BINOP;
import com.chaosopher.tigerlang.compiler.tree.CONST;
import com.chaosopher.tigerlang.compiler.tree.LABEL;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.tree.TEMP;

import org.junit.Test;

public class ConstPropagationTest {

    @Test
    public void propagates() {
        StmList stmList = new StmList(
            new LABEL(Label.create("start")),
            new StmList(
                new MOVE(
                    new TEMP(Temp.create("a")), 
                    new CONST(3)
                ),
                new StmList(
                    new MOVE(
                        new TEMP(Temp.create("b")), 
                        new CONST(0)
                    ),
                    new StmList(
                        new MOVE(
                            new TEMP(Temp.create("c")), 
                            new BINOP(0, new TEMP(Temp.create("a")), new TEMP(Temp.create("b")))
                        )
                    )
                )
            )
        );
        CFG cfg = new CFG(stmList);
        GenKillSets genKillSets = new GenKillSets(cfg);
        // move into constructor
        genKillSets.generate();
//        CycleDetector cd = new CycleDetector(cfg);


        ConstPropagation constPropagation = new ConstPropagation(genKillSets);
        stmList.accept(constPropagation);
        new GenKillSetsXmlSerializer(genKillSets).serialize(System.out);
        constPropagation.getStmList().accept(new PrettyPrinter(System.out));
    }
}