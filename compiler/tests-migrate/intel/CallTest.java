package com.chaosopher.tigerlang.compiler.intel;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.IR;

public class CallTest extends BaseCodeGenTest {

    @Test
    public void call0Args() throws Exception {
        IR tree = new com.chaosopher.tigerlang.compiler.tree.EXP(
            new com.chaosopher.tigerlang.compiler.tree.CALL( new com.chaosopher.tigerlang.compiler.tree.NAME(Label.create()), null)
        );
        TestEmitter testEmitter = new TestEmitter();
        this.createCodeGen(testEmitter).burm(tree);
        assertTrue(testEmitter.get(0) == AssemInstructionEnum.CALL);
    }

    @Test
    public void call1Args() throws Exception {
        IR tree = new com.chaosopher.tigerlang.compiler.tree.EXP(
            new com.chaosopher.tigerlang.compiler.tree.CALL(
                new com.chaosopher.tigerlang.compiler.tree.NAME(
                    Label.create()
                ), 
                new com.chaosopher.tigerlang.compiler.tree.ExpList(
                    new com.chaosopher.tigerlang.compiler.tree.BINOP(
                        com.chaosopher.tigerlang.compiler.tree.BINOP.PLUS,
                        new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create()),
                        new com.chaosopher.tigerlang.compiler.tree.BINOP(
                            com.chaosopher.tigerlang.compiler.tree.BINOP.MUL,
                            new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create()),
                            new com.chaosopher.tigerlang.compiler.tree.CONST(1)
                        )
                    )
                )
            )
        );
        TestEmitter testEmitter = new TestEmitter();
        this.createCodeGen(testEmitter).burm(tree);
        assertTrue(testEmitter.get(0) == AssemInstructionEnum.BINOP);
        assertTrue(testEmitter.get(1) == AssemInstructionEnum.BINOP);
        assertTrue(testEmitter.get(2) == AssemInstructionEnum.CALL);
    }

    @Test
    public void call2Args() throws Exception {
        IR tree = new com.chaosopher.tigerlang.compiler.tree.EXP(
            new com.chaosopher.tigerlang.compiler.tree.CALL(
                new com.chaosopher.tigerlang.compiler.tree.NAME(
                    Label.create()
                ), 
                new com.chaosopher.tigerlang.compiler.tree.ExpList(
                    new com.chaosopher.tigerlang.compiler.tree.BINOP(
                        com.chaosopher.tigerlang.compiler.tree.BINOP.PLUS,
                        new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create()),
                        new com.chaosopher.tigerlang.compiler.tree.BINOP(
                            com.chaosopher.tigerlang.compiler.tree.BINOP.MUL,
                            new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create()),
                            new com.chaosopher.tigerlang.compiler.tree.CONST(1)
                        )
                    ),
                    new com.chaosopher.tigerlang.compiler.tree.ExpList(
                        new com.chaosopher.tigerlang.compiler.tree.BINOP(
                            com.chaosopher.tigerlang.compiler.tree.BINOP.AND,
                            new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create()),
                            new com.chaosopher.tigerlang.compiler.tree.BINOP(
                                com.chaosopher.tigerlang.compiler.tree.BINOP.DIV,
                                new com.chaosopher.tigerlang.compiler.tree.TEMP(Temp.create()),
                                new com.chaosopher.tigerlang.compiler.tree.CONST(1)
                            )
                        )
                    )
                )
            )
        );
        TestEmitter testEmitter = new TestEmitter();
        this.createCodeGen(testEmitter).burm(tree);
        assertTrue(testEmitter.get(0) == AssemInstructionEnum.BINOP);
        assertTrue(testEmitter.get(1) == AssemInstructionEnum.BINOP);
        assertTrue(testEmitter.get(2) == AssemInstructionEnum.BINOP);
        assertTrue(testEmitter.get(3) == AssemInstructionEnum.BINOP);
        assertTrue(testEmitter.get(4) == AssemInstructionEnum.CALL);
    }
}