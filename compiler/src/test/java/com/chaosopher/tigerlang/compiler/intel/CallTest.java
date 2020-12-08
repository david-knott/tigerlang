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
        IR tree = new Tree.EXP(
            new Tree.CALL( new Tree.NAME(Label.create()), null)
        );
        TestEmitter testEmitter = new TestEmitter();
        this.createCodeGen(testEmitter).burm(tree);
        assertTrue(testEmitter.get(0) == AssemInstructionEnum.CALL);
    }

    @Test
    public void call1Args() throws Exception {
        IR tree = new Tree.EXP(
            new Tree.CALL(
                new Tree.NAME(
                    Label.create()
                ), 
                new Tree.ExpList(
                    new Tree.BINOP(
                        Tree.BINOP.PLUS,
                        new Tree.TEMP(Temp.create()),
                        new Tree.BINOP(
                            Tree.BINOP.MUL,
                            new Tree.TEMP(Temp.create()),
                            new Tree.CONST(1)
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
        IR tree = new Tree.EXP(
            new Tree.CALL(
                new Tree.NAME(
                    Label.create()
                ), 
                new Tree.ExpList(
                    new Tree.BINOP(
                        Tree.BINOP.PLUS,
                        new Tree.TEMP(Temp.create()),
                        new Tree.BINOP(
                            Tree.BINOP.MUL,
                            new Tree.TEMP(Temp.create()),
                            new Tree.CONST(1)
                        )
                    ),
                    new Tree.ExpList(
                        new Tree.BINOP(
                            Tree.BINOP.AND,
                            new Tree.TEMP(Temp.create()),
                            new Tree.BINOP(
                                Tree.BINOP.DIV,
                                new Tree.TEMP(Temp.create()),
                                new Tree.CONST(1)
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