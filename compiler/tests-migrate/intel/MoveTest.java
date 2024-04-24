package com.chaosopher.tigerlang.compiler.intel;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.IR;

public class MoveTest extends BaseCodeGenTest {

    @Test
    public void moveTempToTemp() throws Exception {
        IR tree = new com.chaosopher.tigerlang.compiler.tree.MOVE(
            new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create()),
            new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create())
        );
        TestEmitter testEmitter = new TestEmitter();
        this.createCodeGen(testEmitter).burm(tree);
        assertTrue(testEmitter.get(0) == AssemInstructionEnum.MOVE_EXP_TO_TEMP);
        assertEquals(testEmitter.size(), 1);
    }

    @Test
    public void moveConstToTemp() throws Exception {
        IR tree = new com.chaosopher.tigerlang.compiler.tree.MOVE(
            new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create()),
            new com.chaosopher.tigerlang.compiler.tree.CONST(3)
        );
        TestEmitter testEmitter = new TestEmitter();
        this.createCodeGen(testEmitter).burm(tree);
        assertTrue(testEmitter.get(0) == AssemInstructionEnum.MOVE_CONST_TO_TEMP);
        assertEquals(testEmitter.size(), 1);
    }

    @Test
    public void moveBinopToTemp() throws Exception {
        IR tree = new com.chaosopher.tigerlang.compiler.tree.MOVE(
            new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create()),
            super.getBinopExp()
        );
        TestEmitter testEmitter = new TestEmitter();
        this.createCodeGen(testEmitter).burm(tree);
        assertTrue(testEmitter.get(0) == AssemInstructionEnum.BINOP);
        assertTrue(testEmitter.get(1) == AssemInstructionEnum.MOVE_EXP_TO_TEMP);
        assertEquals(testEmitter.size(), 2);
    }

    @Test
    public void moveBinopBinopToTemp() throws Exception {
        IR tree = new com.chaosopher.tigerlang.compiler.tree.MOVE(
            new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create()),
            super.getNestedBinopExp()
        );
        TestEmitter testEmitter = new TestEmitter();
        this.createCodeGen(testEmitter).burm(tree);
        assertTrue(testEmitter.get(0) == AssemInstructionEnum.BINOP);
        assertTrue(testEmitter.get(1) == AssemInstructionEnum.BINOP);
        assertTrue(testEmitter.get(2) == AssemInstructionEnum.MOVE_EXP_TO_TEMP);
        assertEquals(testEmitter.size(), 3);
    }
}