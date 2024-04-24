package com.chaosopher.tigerlang.compiler.intel;

import java.util.Vector;

import com.chaosopher.tigerlang.compiler.assem.InstrList;
import com.chaosopher.tigerlang.compiler.core.LL;
import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.CALL;
import com.chaosopher.tigerlang.compiler.tree.IR;
import com.chaosopher.tigerlang.compiler.tree.JUMP;

public class TestEmitter implements Emitter {

    LL<AssemInstructionEnum> assem;

    private void add(AssemInstructionEnum assemInstructionEnum) {
        this.assem = LL.<AssemInstructionEnum>insertRear(this.assem, assemInstructionEnum);
    }

    public AssemInstructionEnum get(int i) {
        return i < LL.<AssemInstructionEnum>size(this.assem) ? LL.<AssemInstructionEnum>get(this.assem, i) : null;
    }

    public int size() {
        return LL.<AssemInstructionEnum>size(this.assem);
    }

    @Override
    public void loadIndirect(Temp arg0, Temp arg1) {
        this.add(AssemInstructionEnum.LOAD_INDIRECT);
    }

    @Override
    public void loadIndirectDisp(int binop, Temp arg0, Temp arg1, int offset) {
        this.add(AssemInstructionEnum.LOAD_INDIRECT_DISP);
    }

    @Override
    public void moveExpToTemp(Temp arg0, Temp arg1) {
        this.add(AssemInstructionEnum.MOVE_EXP_TO_TEMP);
    }

    @Override
    public void binop(Temp arg0, Temp arg1, Temp temp) {
        this.add(AssemInstructionEnum.BINOP);
    }

    @Override
    public void storeIndirect(Temp arg0, Temp arg1) {
        this.add(AssemInstructionEnum.STORE_INDIRECT);
    }

    @Override
    public void storeIndirectDisp(int binop, Temp arg0, Temp arg1, int offset) {
        this.add(AssemInstructionEnum.STORE_INDIRECT_DISP);
    }

    @Override
    public void call(Object call) {
        this.add(AssemInstructionEnum.CALL);
    }

    @Override
    public void cjump(int relop, Temp arg0, Temp arg1, Label iftrue, Label iffalse) {
        this.add(AssemInstructionEnum.CJUMP);
    }

    @Override
    public void jump(Temp arg0, JUMP jump) {
        this.add(AssemInstructionEnum.JUMP);
    }

    @Override
    public void label(Label label) {
        // TODO Auto-generated method stub

    }

    @Override
    public InstrList getInstrList() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void moveConstToTemp(Temp arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void binop(Temp arg0, int value, Temp temp) {
        // TODO Auto-generated method stub
        this.add(AssemInstructionEnum.BINOP);

    }

    @Override
    public void binop(int value, Temp arg0, Temp temp) {
        // TODO Auto-generated method stub
        this.add(AssemInstructionEnum.BINOP);

    }

    @Override
    public void binop(int value, int arg0, Temp temp) {
        // TODO Auto-generated method stub
        this.add(AssemInstructionEnum.BINOP);

    }

    @Override
    public void loadIndrectDispScale(int i, Temp arg0, Temp base, Temp index, int value) {
        this.add(AssemInstructionEnum.LOAD_INDIRECT_DISP_SCALED);

    }
}