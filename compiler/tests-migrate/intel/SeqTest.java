package com.chaosopher.tigerlang.compiler.intel;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.IR;


public class SeqTest extends BaseCodeGenTest {

    @Test
    public void seqTwoItems() throws Exception {
        IR tree = new com.chaosopher.tigerlang.compiler.tree.SEQ(
            new com.chaosopher.tigerlang.compiler.tree.MOVE(
                new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create()),
                new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create())
            ),
            new com.chaosopher.tigerlang.compiler.tree.MOVE(
                new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create()),
                new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create())
            )
        );
        TestEmitter testEmitter = new TestEmitter();
        this.createCodeGen(testEmitter).burm(tree);
        assertTrue(testEmitter.get(0) == AssemInstructionEnum.MOVE_EXP_TO_TEMP);
        assertTrue(testEmitter.get(1) == AssemInstructionEnum.MOVE_EXP_TO_TEMP);
        assertEquals(2, testEmitter.size());
    }

    @Test
    public void seqThreeItems() throws Exception {
        IR tree = new com.chaosopher.tigerlang.compiler.tree.SEQ(
            new com.chaosopher.tigerlang.compiler.tree.MOVE(
                new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create()),
                new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create())
            ),
            new com.chaosopher.tigerlang.compiler.tree.SEQ(
                new com.chaosopher.tigerlang.compiler.tree.MOVE(
                    new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create()),
                    new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create())
                ),
                new com.chaosopher.tigerlang.compiler.tree.MOVE(
                    new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create()),
                    new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create())
                )
            )
        );
        TestEmitter testEmitter = new TestEmitter();
        this.createCodeGen(testEmitter).burm(tree);
        assertTrue(testEmitter.get(0) == AssemInstructionEnum.MOVE_EXP_TO_TEMP);
        assertTrue(testEmitter.get(1) == AssemInstructionEnum.MOVE_EXP_TO_TEMP);
        assertTrue(testEmitter.get(2) == AssemInstructionEnum.MOVE_EXP_TO_TEMP);
        assertEquals(3, testEmitter.size());
    }


}