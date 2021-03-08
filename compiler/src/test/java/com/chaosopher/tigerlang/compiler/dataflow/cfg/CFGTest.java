package com.chaosopher.tigerlang.compiler.dataflow.cfg;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.JUMP;
import com.chaosopher.tigerlang.compiler.tree.LABEL;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.tree.TEMP;

import org.junit.Test;

public class CFGTest {
    
    @Test(expected = java.lang.Error.class)
    public void statementNoLabel() {
        StmList test = new StmList(
            new MOVE(
                new TEMP(Temp.create()),
                new TEMP(Temp.create())
            )
        );
        CFG cfg = CFG.build(test);
        assertNotNull(cfg);
    }

    @Test
    public void simpleBlock() {
        Temp a = Temp.create();
        Temp b = Temp.create();
        Temp c = Temp.create();
        StmList test = new StmList(
            new LABEL(
                Label.create()
            ),
            new StmList(
                new MOVE(
                    new TEMP(a),
                    new TEMP(b)
                ),
                new StmList(
                    new MOVE(
                        new TEMP(c),
                        new TEMP(a))
                )
            )
        );
        CFG cfg = CFG.build(test);
        cfg.show(System.out);
        // expect one block, with 3 statements.
    }

    @Test
    public void simpleLoop() {
        Temp a = Temp.create();
        Temp b = Temp.create();
        Temp c = Temp.create();
        Label label = Label.create();
        StmList test = new StmList(
            new LABEL(
                label
            ),
            new StmList(
                new MOVE(
                    new TEMP(a),
                    new TEMP(b)
                ),
                new StmList(
                    new MOVE(
                        new TEMP(c),
                        new TEMP(a)
                    ),
                    new StmList(
                        new JUMP(label)
                    )
                )
            )
        );
        CFG cfg = CFG.build(test);
        cfg.show(System.out);
        // expect one block, with 3 statements.
    }



}
