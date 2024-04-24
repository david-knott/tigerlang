package com.chaosopher.tigerlang.compiler.helpers;

import com.chaosopher.tigerlang.compiler.assem.Instr;
import com.chaosopher.tigerlang.compiler.assem.InstrList;
import com.chaosopher.tigerlang.compiler.assem.OPER;
import com.chaosopher.tigerlang.compiler.frame.Access;
import com.chaosopher.tigerlang.compiler.frame.Frame;
import com.chaosopher.tigerlang.compiler.frame.Proc;
import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.temp.TempList;
import com.chaosopher.tigerlang.compiler.tree.Exp;
import com.chaosopher.tigerlang.compiler.tree.ExpList;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.util.BoolList;

public class TestFrame extends Frame {
    private TempList precoloured; 
    private TempList registers;

    public TestFrame(TempList precoloured, TempList registers) {
        this.precoloured = precoloured;
        this.registers = registers;
    }

    @Override
    public String tempMap(Temp t) {
        return precoloured.toTempMap().get(t);
    }

    @Override
    public Temp FP() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Temp RV() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int wordSize() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Frame newFrame(Label name, BoolList formals) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Access allocLocal(boolean escape) {
        return new Access() {

            @Override
            public Exp exp(Exp framePtr) {
                // TODO Auto-generated method stub
                return null;
            }
        };
    }

    @Override
    public Stm procEntryExit1(Stm body) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InstrList procEntryExit2(InstrList body) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Proc procEntryExit3(InstrList body) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Exp externalCall(String func, ExpList args) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String string(Label l, String literal) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TempList registers() {
        return this.registers;
    }

    @Override
    public InstrList tempToMemory(Temp temp, Temp spillTemp, Access access) {
        Instr moveTempToNewTemp = new com.chaosopher.tigerlang.compiler.assem.MOVE("movq %`s0, %`d0; ttm", spillTemp, temp);
        Instr moveNewTempToFrame = new OPER("movq %`s0, " + 0 + "(%`d0); ttm", this.precoloured, new TempList(spillTemp, null));
        return new InstrList(moveTempToNewTemp, new InstrList(moveNewTempToFrame, null)); 
    }

    @Override
    public InstrList memoryToTemp(Temp temp, Temp spillTemp, Access access) {
        Instr moveFrameToNewTemp = new OPER("movq " + 0 + "(%`s0), %`d0; mtt", new TempList(spillTemp, null), this.precoloured);
        Instr moveNewTempToTemp = new com.chaosopher.tigerlang.compiler.assem.MOVE("movq %`s0, %`d0; mtt", temp, spillTemp);
        return new InstrList(moveFrameToNewTemp, new InstrList(moveNewTempToTemp, null)); 
    }
    
}