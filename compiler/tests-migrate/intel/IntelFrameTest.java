package com.chaosopher.tigerlang.compiler.intel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.BINOP;
import com.chaosopher.tigerlang.compiler.tree.CONST;
import com.chaosopher.tigerlang.compiler.tree.EmptyStm;
import com.chaosopher.tigerlang.compiler.tree.IR;
import com.chaosopher.tigerlang.compiler.tree.MEM;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.SEQ;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.tree.TEMP;
import com.chaosopher.tigerlang.compiler.util.BoolList;

public class IntelFrameTest {

    @Test
    public void calleeSaveRestoreTest() {
        IntelFrame intelFrame = new IntelFrame(new Label(), null);
        EmptyStm emptyStm = new EmptyStm();
        Stm result = intelFrame.procEntryExit1(emptyStm);
        //expect that result is type of sequence
        //with first n items
        System.out.println(result);
    }

    @Test
    public void moveFunctionArg1IntoPosition() {
        IntelFrame intelFrame = new IntelFrame(new Label(), new BoolList(true, null));
        EmptyStm emptyStm = new EmptyStm();
        Stm result = intelFrame.procEntryExit1(emptyStm);
        SEQ seq = (SEQ)result;
        SEQ nseq = seq.normalise();
        System.out.println(nseq);
    }

    @Test
    public void testMoveArgs() {
        IntelFrame intelFrame = new IntelFrame(new Label(), 
        new BoolList(false, 
        new BoolList(false, 
        new BoolList(false, 
        new BoolList(false, 
        new BoolList(false, 
        new BoolList(false, 
        new BoolList(true, null))))))));
        /*
        StmList moveArgs = intelFrame.moveArgs();
        matchesMoveSrc(moveArgs.head, IntelFrame.rdi);
        matchesMoveSrc(moveArgs.tail.head, IntelFrame.rsi);
        matchesMoveSrc(moveArgs.tail.tail.head, IntelFrame.rdx);
        matchesMoveSrc(moveArgs.tail.tail.tail.head, IntelFrame.rcx);
        matchesMoveSrc(moveArgs.tail.tail.tail.tail.head, IntelFrame.r8);
        matchesMoveSrc(moveArgs.tail.tail.tail.tail.tail.head, IntelFrame.r9);
        matchesMoveSrcMem(moveArgs.tail.tail.tail.tail.tail.tail.head, -8);*/
    }

    @Test
    public void testCalleeSaveList() {
        IntelFrame intelFrame = new IntelFrame(new Label(), 
        new BoolList(false, 
        new BoolList(false, 
        new BoolList(false, 
        new BoolList(false, 
        new BoolList(false, 
        new BoolList(false, 
        new BoolList(true, null))))))));
        /*
        StmList calleeSaveList = intelFrame.calleeSaveList();
        matchesMoveSrc(calleeSaveList.head, IntelFrame.rbx);
        matchesMoveSrc(calleeSaveList.tail.head, IntelFrame.r12);
        matchesMoveSrc(calleeSaveList.tail.tail.head, IntelFrame.r13);
        matchesMoveSrc(calleeSaveList.tail.tail.tail.head, IntelFrame.r14);
        matchesMoveSrc(calleeSaveList.tail.tail.tail.tail.head, IntelFrame.r15);*/
    }

    @Test
    public void testCalleeRestoreList() {
        IntelFrame intelFrame = new IntelFrame(new Label(), 
        new BoolList(false, 
        new BoolList(false, 
        new BoolList(false, 
        new BoolList(false, 
        new BoolList(false, 
        new BoolList(false, 
        new BoolList(true, null))))))));
        //have to call save first as it populates a hashtable
        intelFrame.calleeSaveToTemp();
        /*
        StmList calleeRestoreList = intelFrame.calleeRestoreList();
        matchesMoveDest(calleeRestoreList.head, IntelFrame.rbx);
        matchesMoveDest(calleeRestoreList.tail.head, IntelFrame.r12);
        matchesMoveDest(calleeRestoreList.tail.tail.head, IntelFrame.r13);
        matchesMoveDest(calleeRestoreList.tail.tail.tail.head, IntelFrame.r14);
        matchesMoveDest(calleeRestoreList.tail.tail.tail.tail.head, IntelFrame.r15);*/
    }

    private void matchesMoveDest(Stm stm, Temp dst) {
        assertTrue(stm instanceof MOVE);
        MOVE move = (MOVE)stm;
        TEMP mvDst = (TEMP)move.dst;
        assertEquals(dst, mvDst.temp);
    }

    private void matchesMoveSrc(Stm stm, Temp src) {
        assertTrue(stm instanceof MOVE);
        MOVE move = (MOVE)stm;
        TEMP mvSrc = (TEMP)move.src;
        assertEquals(src, mvSrc.temp);
    }

    private void matchesMoveSrcMem(Stm stm, int offset) {
        assertTrue(stm instanceof MOVE);
        MOVE move = (MOVE)stm;
        MEM mvSrc = (MEM)move.src;
        assertTrue(mvSrc.exp instanceof BINOP);
        BINOP binop = (BINOP)mvSrc.exp;
        assertTrue(binop.left instanceof CONST);
        CONST cont = (CONST)binop.left;
        assertEquals(cont.value, offset);
    }
}