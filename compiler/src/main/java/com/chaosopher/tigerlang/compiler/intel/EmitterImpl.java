package com.chaosopher.tigerlang.compiler.intel;

import com.chaosopher.tigerlang.compiler.assem.Instr;
import com.chaosopher.tigerlang.compiler.assem.OPER;
import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.temp.LabelList;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.temp.TempList;
import com.chaosopher.tigerlang.compiler.tree.BINOP;
import com.chaosopher.tigerlang.compiler.tree.CJUMP;
import com.chaosopher.tigerlang.compiler.tree.IR;
import com.chaosopher.tigerlang.compiler.tree.JUMP;
public class EmitterImpl implements Emitter {

    private com.chaosopher.tigerlang.compiler.assem.InstrList iList = null, last = null;
	// AssemInstructionEnum.InstrList iList = null, last = null;

	@Override
	public com.chaosopher.tigerlang.compiler.assem.InstrList getInstrList() {
		return this.iList;
	}

	private void emit(Instr instr) {
		if (last != null) {
			last = last.tail = new com.chaosopher.tigerlang.compiler.assem.InstrList(instr, null);
		} else {
			last = iList = new com.chaosopher.tigerlang.compiler.assem.InstrList(instr, null);
		}
	}

	public void loadIndirect(Temp dst, Temp src) {
		emit(new com.chaosopher.tigerlang.compiler.assem.OPER("movq (%`s0), %`d0 # load to offset", new TempList(dst), new TempList(src)));
	}

	public void loadIndirectDisp(int binop, Temp dst, Temp src, int offset) {
		offset = binop == BINOP.PLUS ? offset : -offset;
		emit(new com.chaosopher.tigerlang.compiler.assem.OPER("movq " + offset + "(%`s0), %`d0 # load to offset", new TempList(dst), new TempList(src)));
	}

	public void startLoadIndirectDispScaled(Temp arg0, Temp arg1, IR arg2) {
			//emit(new com.chaosopher.tigerlang.compiler.assem.OPER("movq (%`s0, %`s1, " + wordSize +"), %`d0 # load array", 
     //                   new TempList(dstTemp), 
       //                 new TempList(srcTemp, new TempList(indexTemp))
         //               ));

	}

	public void endLoadIndirectDispScaled(Temp arg0, Temp arg1) {
	}

	public void moveConstToTemp(Temp dst, int arg1) {
		emit(new OPER("movq $"  + arg1 + ", %`d0 # const", new TempList(dst), null));
	}

	public void moveExpToTemp(Temp arg0, Temp arg1) {
	}

	@Override
	public void binop(Temp arg0, Temp arg1, Temp temp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void storeIndirect(Temp dst, Temp src) {
		emit(new com.chaosopher.tigerlang.compiler.assem.OPER("movq %`s0, (%`s1)", null, new TempList(src, new TempList(dst))));

	}

	@Override
	public void call(Object call) {
		//emit(new OPER("call " + name.label + " # exp call ( no return value )", IntelFrame.callDefs, l));

	}

	@Override
	public void cjump(int relop, Temp leftTemp, Temp rightTemp, Label iftrue, Label iffalse) {
		emit(new OPER("cmp %`s0, %`s1", null, new TempList(rightTemp, new TempList(leftTemp, null))));
            String op = "";
            switch(relop) {
                case CJUMP.EQ:
                    op = "je";
                break;
                case CJUMP.GE:
                    op = "jge";
                break;
                case CJUMP.GT:
                    op = "jg";
                break;
                case CJUMP.LE:
                    op = "jle";
                break;
                case CJUMP.LT:
                    op = "jl";
                break;
                case CJUMP.NE:
                    op = "jne";
                break;
                case CJUMP.UGE:
                    op = "jae";
                break;
                case CJUMP.UGT:
                    op = "ja";
                break;
                case CJUMP.ULE:
                    op = "jbe";
                break;
                case CJUMP.ULT:
                    op = "jb";
                break;
            }
            emit(new OPER(op + " `j0", null, null, new LabelList(iftrue, new LabelList(iffalse, null))));
	}

	@Override
	public void jump(Temp arg0, JUMP jump) {
		emit(new OPER("jmp `j0", null, null, jump.targets));
	}

	@Override
	public void label(Label label) {
        emit(new com.chaosopher.tigerlang.compiler.assem.LABEL(label.toString() + ":", label));
	}

	@Override
	public void binop(Temp arg0, int value, Temp temp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void binop(int value, Temp arg0, Temp temp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void binop(int value, int arg0, Temp temp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void storeIndirectDisp(int binop, Temp arg0, Temp arg1, int offset) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadIndrectDispScale(int i, Temp arg0, Temp base, Temp index, int value) {
		// TODO Auto-generated method stub

	}

    //store Operations

    //binop Operations


    //jump Operations

    //cjump Operations
}