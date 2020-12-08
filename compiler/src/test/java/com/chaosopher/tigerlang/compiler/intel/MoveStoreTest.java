package com.chaosopher.tigerlang.compiler.intel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.IR;
import com.chaosopher.tigerlang.compiler.tree.MEM;

public class MoveStoreTest extends BaseCodeGenTest {
    
    @Test
    public void moveTempToMem() throws Exception {
        IR tree = new Tree.MOVE(
            new MEM(
                new Tree.TEMP(Temp.create())
            ),
            new Tree.TEMP(Temp.create())
        );
        TestEmitter testEmitter = new TestEmitter();
        this.createCodeGen(testEmitter).burm(tree);
        assertTrue(testEmitter.get(0) == AssemInstructionEnum.STORE_INDIRECT);
        assertEquals(testEmitter.size(), 1);
    }

    @Test
    public void moveTempToOffsetMem() throws Exception {
        IR tree = new Tree.MOVE(
            new MEM(
                new Tree.BINOP(
                    Tree.BINOP.PLUS,
                    new Tree.TEMP(Temp.create()),
                    new Tree.CONST(5)
                )
            ),
            new Tree.TEMP(Temp.create())
        );
        TestEmitter testEmitter = new TestEmitter();
        this.createCodeGen(testEmitter).burm(tree);
        assertTrue(testEmitter.get(0) == AssemInstructionEnum.STORE_INDIRECT_DISP);
        assertEquals(testEmitter.size(), 1);
    }

    @Test
    public void moveTempToIndexedMem() throws Exception {
        IR tree = new Tree.MOVE(
            new MEM(
                new Tree.BINOP(
                    Tree.BINOP.PLUS,
                    new Tree.TEMP(Temp.create()),
                    new Tree.BINOP(
                        Tree.BINOP.MUL,
                        new Tree.TEMP(Temp.create()),
                        new Tree.CONST(1)
                    )
                )
            ),
            new Tree.TEMP(Temp.create())
        );
        TestEmitter testEmitter = new TestEmitter();
        this.createCodeGen(testEmitter).burm(tree);
        assertTrue(testEmitter.get(0) == AssemInstructionEnum.STORE_INDIRECT_DISP_SCALED);
        assertEquals(testEmitter.size(), 1);
    }
}