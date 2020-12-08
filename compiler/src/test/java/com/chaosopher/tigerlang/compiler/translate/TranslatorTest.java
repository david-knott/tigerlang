package com.chaosopher.tigerlang.compiler.translate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Before;
import org.junit.Test;

import Frame.Frame;
import com.chaosopher.tigerlang.compiler.intel.IntelFrame;
import Semant.Semant;
import Symbol.Symbol;
import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.tree.BINOP;
import com.chaosopher.tigerlang.compiler.tree.CALL;
import com.chaosopher.tigerlang.compiler.tree.CONST;
import com.chaosopher.tigerlang.compiler.tree.EXP;
import com.chaosopher.tigerlang.compiler.tree.MEM;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.TEMP;
import com.chaosopher.tigerlang.compiler.types.ARRAY;
import com.chaosopher.tigerlang.compiler.types.RECORD;

public class TranslatorTest {

    private Translator translator;
    private Frame frame;
    private Level level;
    private Level childLevel;

    @Before
    public void setup() {
        translator = new Translator(false, false);
        Label label = Label.create("test");
        frame = new IntelFrame(label, null);
        level = new Level(frame);
        childLevel = new Level(level, label, null, true);
    }

    @Test
    public void simpleVarInReg() {
        Access access = level.allocLocal(false);
        Exp simpleVar = translator.simpleVar(access, level);
        assertNotNull(simpleVar);
        assertTrue(simpleVar instanceof Ex);
        Ex ex = (Ex) simpleVar;
        assertNotNull(ex);
        assertTrue(ex.exp instanceof TEMP);
    }

    @Test
    public void simpleVarInFrame() {
        Access access = level.allocLocal(true);
        Exp simpleVar = translator.simpleVar(access, level);
        assertNotNull(simpleVar);
        assertTrue(simpleVar instanceof Ex);
        Ex ex = (Ex) simpleVar;
        assertNotNull(ex);
        assertTrue(ex.exp instanceof MEM);
        assertTrue(((MEM)ex.exp).exp instanceof BINOP);
        MEM mem = ((MEM)ex.exp);
        assertTrue(((BINOP)mem.exp).left instanceof TEMP);
        //expect left to base pointer temp
        assertTrue(((BINOP)mem.exp).right instanceof CONST);
        //expect right to be const offset
    }

    @Test
    public void simpleVarStaticLink() {
        Access access = level.allocLocal(true);
        Exp simpleVar = translator.simpleVar(access, childLevel);
        assertNotNull(simpleVar);
        assertTrue(simpleVar instanceof Ex);
        Ex ex = (Ex) simpleVar;
        assertNotNull(ex);
        assertTrue(ex.exp instanceof MEM);
    }

    @Test
    public void varExpInReg() {
        Access access = level.allocLocal(false);
        Exp simpleVar = translator.simpleVar(access, level);
        ExpTy expTy = new ExpTy(simpleVar, Semant.INT);
        Exp varExp = translator.varExp(expTy);
        assertNotNull(varExp);
        assertEquals(simpleVar, varExp);
    }

    @Test
    public void varExpInFrame() {
        Access access = level.allocLocal(true);
        Exp simpleVar = translator.simpleVar(access, level);
        ExpTy expTy = new ExpTy(simpleVar, Semant.INT);
        Exp varExp = translator.varExp(expTy);
        assertNotNull(varExp);
        assertEquals(simpleVar, varExp);
    }

    @Test
    public void initArray() {
        ExpTy integerExp = new ExpTy(translator.integer(1), Semant.INT);
        Exp array = translator.array(level, integerExp, integerExp);
        assertNotNull(array);
        var linear = Canon.Canon.linearize(array.unNx());
        assertTrue(linear.head instanceof MOVE);
        assertTrue(((MOVE)linear.head).dst instanceof TEMP);
        assertTrue(((MOVE)linear.head).src instanceof CALL);
        assertTrue(linear.tail.head instanceof EXP); //
        assertTrue(((EXP)linear.tail.head).exp instanceof TEMP);
        assertNotNull(linear);
    }

    @Test
    public void subscriptVar() {
        Access access = level.allocLocal(true);
        ExpTy integerExp = new ExpTy(translator.integer(1), Semant.INT);
        ExpTy arrayVar = new ExpTy(translator.simpleVar(access, level), new ARRAY(Semant.INT));
        Exp subscriptVar = translator.subscriptVar(integerExp, arrayVar, level);
        assertNotNull(subscriptVar);
    }

    @Test
    public void initRecord() {
        ExpTyList expTyList = null;
        Exp record = translator.record(level, expTyList);
        assertNotNull(record);
        var linear = Canon.Canon.linearize(record.unNx());
        assertNotNull(linear);
    }

    @Test
    public void fieldVar() {
        Access access = level.allocLocal(false);
        RECORD r = new RECORD(Symbol.symbol("field1"),Semant.INT, null);
        ExpTy recordVar = new ExpTy(translator.simpleVar(access, level), r);
        Exp fieldVar = translator.fieldVar(recordVar.exp, 0, level);
        var linear = Canon.Canon.linearize(fieldVar.unNx());
        assertNotNull(linear);
    }

    @Test
    public void binopEq() {
        Exp binaryOperator = translator.binaryOperator(0, new ExpTy(translator.integer(1), Semant.INT), new ExpTy(translator.integer(1), Semant.INT));
        assertNotNull(binaryOperator);
        var linear = Canon.Canon.linearize(binaryOperator.unNx());
        assertNotNull(linear);
        assertTrue(linear.head instanceof EXP);
        assertTrue(((EXP)linear.head).exp instanceof BINOP);
        assertTrue(((BINOP)((EXP)linear.head).exp).left instanceof CONST);
        assertTrue(((BINOP)((EXP)linear.head).exp).right instanceof CONST);
    }

    @Test
    public void whileLoopExp() {
        Label loopEnd = Label.create();
        ExpTy testExp = new ExpTy(translator.relativeOperator(0, new ExpTy(translator.integer(1), Semant.INT), new ExpTy(translator.integer(0), Semant.INT)), Semant.INT);
        ExpTy transBody = new ExpTy(translator.integer(1), Semant.INT);
        Exp whileLoopExp = translator.whileL(level, loopEnd, testExp, transBody);
        assertNotNull(whileLoopExp);
        var linear = Canon.Canon.linearize(whileLoopExp.unNx());
        assertNotNull(linear);
    }

    @Test
    public void forLoopExp() {
        Label loopEnd = Label.create();
        ExpTy transBody = new ExpTy(translator.Noop(), Semant.VOID);
        Access access = level.allocLocal(false);
        Exp exploVar = translator.simpleVar(access, childLevel);
        ExpTy explo = new ExpTy(exploVar, Semant.INT);
        ExpTy exphi = new ExpTy(translator.integer(1), Semant.INT);
        Exp foorExp = translator.forL(level, loopEnd, access, explo, exphi, transBody);
        var linear = Canon.Canon.linearize(foorExp.unNx());
        assertNotNull(linear);
    }


}
