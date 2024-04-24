package com.chaosopher.tigerlang.compiler.intel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.IR;
import com.chaosopher.tigerlang.compiler.tree.MEM;

public class MoveLoadTest extends BaseCodeGenTest {

    @Test
    public void moveMemToTemp() throws Exception {
        IR tree = new com.chaosopher.tigerlang.compiler.tree.MOVE(
            new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create()),
            new MEM(
                new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create())
            )
        );
        TestEmitter testEmitter = new TestEmitter();
        this.createCodeGen(testEmitter).burm(tree);
        assertTrue(testEmitter.get(0) == AssemInstructionEnum.LOAD_INDIRECT);
        assertEquals(testEmitter.size(), 1);
    }

    @Test
    public void moveOffsetMemToTemp() throws Exception {
        IR tree = new com.chaosopher.tigerlang.compiler.tree.MOVE(
            new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create()),
            new MEM(
                new com.chaosopher.tigerlang.compiler.tree.BINOP(
                    com.chaosopher.tigerlang.compiler.tree.BINOP.PLUS,
                    new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create()),
                    new com.chaosopher.tigerlang.compiler.tree.CONST(5)
                )
            )
        );
        TestEmitter testEmitter = new TestEmitter();
        this.createCodeGen(testEmitter).burm(tree);
        assertTrue(testEmitter.get(0) == AssemInstructionEnum.LOAD_INDIRECT_DISP);
        assertEquals(testEmitter.size(), 1);
    }

    @Test
    public void moveOffsetMemExpToTemp() throws Exception {
        IR tree = new com.chaosopher.tigerlang.compiler.tree.MOVE(
            new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create()),
            new MEM(
                new com.chaosopher.tigerlang.compiler.tree.BINOP(
                    com.chaosopher.tigerlang.compiler.tree.BINOP.PLUS,
                    super.getBinopExp(),
                    new com.chaosopher.tigerlang.compiler.tree.CONST(5)
                )
            )
        );
        TestEmitter testEmitter = new TestEmitter();
        this.createCodeGen(testEmitter).burm(tree);
        assertTrue(testEmitter.get(0) == AssemInstructionEnum.BINOP);
        assertTrue(testEmitter.get(1) == AssemInstructionEnum.LOAD_INDIRECT_DISP);
        assertEquals(testEmitter.size(), 2);
    }

    @Test
    public void moveOffsetMemNestedExpToTemp() throws Exception {
        IR tree = new com.chaosopher.tigerlang.compiler.tree.MOVE(
            new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create()),
            new MEM(
                new com.chaosopher.tigerlang.compiler.tree.BINOP(
                    com.chaosopher.tigerlang.compiler.tree.BINOP.PLUS,
                    super.getNestedBinopExp(),
                    new com.chaosopher.tigerlang.compiler.tree.CONST(5)
                )
            )
        );
        TestEmitter testEmitter = new TestEmitter();
        this.createCodeGen(testEmitter).burm(tree);
        assertTrue(testEmitter.get(0) == AssemInstructionEnum.BINOP);
        assertTrue(testEmitter.get(1) == AssemInstructionEnum.BINOP);
        assertTrue(testEmitter.get(2) == AssemInstructionEnum.LOAD_INDIRECT_DISP);
        assertEquals(testEmitter.size(), 3);
    }

    @Test
    public void moveIndexedMemToTemp() throws Exception {
        IR tree = new com.chaosopher.tigerlang.compiler.tree.MOVE(
            new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create()),
            new MEM(
                new com.chaosopher.tigerlang.compiler.tree.BINOP(
                    com.chaosopher.tigerlang.compiler.tree.BINOP.PLUS,
                    new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create()),
                    new com.chaosopher.tigerlang.compiler.tree.BINOP(
                        com.chaosopher.tigerlang.compiler.tree.BINOP.MUL,
                        new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create()),
                        new com.chaosopher.tigerlang.compiler.tree.CONST(4)
                    )
                )
            )
        );
        TestEmitter testEmitter = new TestEmitter();
        this.createCodeGen(testEmitter).burm(tree);
        assertTrue(testEmitter.get(0) == AssemInstructionEnum.LOAD_INDIRECT_DISP_SCALED);
        assertEquals(testEmitter.size(), 1);
    }

    @Test
    public void moveIndexedMemExpToTemp() throws Exception {
        IR tree = new com.chaosopher.tigerlang.compiler.tree.MOVE(
            new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create()),
            new MEM(
                new com.chaosopher.tigerlang.compiler.tree.BINOP(
                    com.chaosopher.tigerlang.compiler.tree.BINOP.PLUS,
                    super.getBinopExp(),
                    new com.chaosopher.tigerlang.compiler.tree.BINOP(
                        com.chaosopher.tigerlang.compiler.tree.BINOP.MUL,
                        super.getBinopExp(),
                        new com.chaosopher.tigerlang.compiler.tree.CONST(4)
                    )
                )
            )
        );
        TestEmitter testEmitter = new TestEmitter();
        this.createCodeGen(testEmitter).burm(tree);
        assertTrue(testEmitter.get(0) == AssemInstructionEnum.BINOP);
        assertTrue(testEmitter.get(1) == AssemInstructionEnum.BINOP);
        assertTrue(testEmitter.get(2) == AssemInstructionEnum.LOAD_INDIRECT_DISP_SCALED);
        assertEquals(testEmitter.size(), 3);
    }
}