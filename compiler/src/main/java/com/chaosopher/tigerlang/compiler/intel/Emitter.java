package com.chaosopher.tigerlang.compiler.intel;

import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.JUMP;

/**
 * Interface that code emitters must implement. This abstraction
 * was created to faciliate testing of the JBURG rules.
 */
public interface Emitter {

	public com.chaosopher.tigerlang.compiler.assem.InstrList getInstrList();
	
    public void loadIndirect(Temp dst, Temp src);

	public void loadIndirectDisp(int binop, Temp base, Temp src, int offset);

	public void loadIndrectDispScale(int i, Temp dst, Temp base, Temp index, int value);

	public void moveConstToTemp(Temp dst, int arg1);

	public void moveExpToTemp(Temp dst, Temp arg1);

	public void binop(Temp arg0, int value, Temp temp);

	public void binop(int value, Temp arg0, Temp temp);

	public void binop(int value, int arg0, Temp temp);

	public void binop(Temp arg0, Temp arg1, Temp temp);

    public void storeIndirect(Temp src1, Temp src2);

    public void storeIndirectDisp(int binop, Temp src1, Temp src2, int offset);

	public void call(Object call);

	public void cjump(int relop, Temp arg0, Temp arg1, Label iftrue, Label iffalse);

	public void jump(Temp arg0, JUMP jump);

	public void label(Label label);

}