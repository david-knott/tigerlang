package com.chaosopher.tigerlang.compiler.dataflow;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.LABEL;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.tree.TEMP;

import org.junit.Test;

public class CFGTest {
    
    @Test
    public void canCreateInstance() {
        StmList test = new StmList(
            new MOVE(
                new TEMP(Temp.create()),
                new TEMP(Temp.create())
            )
        );
        CFG cfg = new CFG(test);
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
        CFG cfg = new CFG(test);
        // expect one block, with 3 statements.
    }



}
