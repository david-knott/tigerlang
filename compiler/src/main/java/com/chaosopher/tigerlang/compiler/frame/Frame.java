package com.chaosopher.tigerlang.compiler.frame;

import com.chaosopher.tigerlang.compiler.assem.InstrList;
import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.temp.TempList;
import com.chaosopher.tigerlang.compiler.temp.TempMap;

public abstract class Frame implements TempMap {
    public Label name;
    public AccessList formals;

    /**
     * Frame pointer register
     * @return a temporary register
     */
    abstract public Temp FP();

    /**
     * Return the value register. This is the
     * register where a function puts its return
     * value
     * @return a temporary register
     */
    abstract public Temp RV();

    /**
     * Word size of the machine in bytes.
     * @return wordsize in bytes
     */
    abstract public int wordSize();

    /**
     * Creates a new frame for a function with label name
     * and formal list formals 
     * @param name the name of the function
     * @param formals the formal argument list
     * @return a new Frame
     */
    abstract public Frame newFrame(Label name, com.chaosopher.tigerlang.compiler.util.BoolList formals);

    /**
     * Allocate a new local variable in a register or the
     * current frame
     * @param escape
     * @return the variable access
     */
    abstract public Access allocLocal(boolean escape);

    /**
     * For each incoming register parameter, move it to the place from which it is seen from within the function. 
     * This could be a fresh temporary. One good way to handle this is for newFrame to create a sequence of 
     * com.chaosopher.tigerlang.compiler.tree.MOVE statements as it creates all the formal parameter "accesses." newFrame can put this into the 
     * frame data structure, and procEntryExit1 can just concatenate it onto the procedure body.
     * Also concatenated to the body are statements for saving and restoring of callee-save registers 
     * (including the return-address register). If your register allocator does not implement spilling, 
     * all the callee-save (and return-address) registers should be written to the frame at the beginning 
     * of the procedure body and fetched back afterward. 
     * Therefore, procEntryExit1 should call allocLocal for each register to be saved, 
     * and generate com.chaosopher.tigerlang.compiler.tree.MOVE instructions to save and restore the registers. 
     * With luck, saving and restoring the callee-save registers will give the register allocator 
     * enough headroom to work with, so that some nontrivial programs can be compiled. 
     * Of course, some programs just cannot be compiled without spilling.
     * 
     * If your register allocator implements spilling, then the callee-save registers should not 
     * always be written to the frame. 
     * Instead, if the register allocator needs the space, it may choose to spill only some 
     * of the callee-save registers. But "precolored" temporaries are never spilled; 
     * so procEntryExit1 should make up new temporaries for each callee-save (and return-address) 
     * register. On entry, it should move all these registers to their new temporary locations, 
     * and on exit, it should move them back. Of course, these moves (for nonspilled registers) 
     * will be eliminated by register coalescing, so they cost nothing.
     */
    abstract public com.chaosopher.tigerlang.compiler.tree.Stm procEntryExit1(com.chaosopher.tigerlang.compiler.tree.Stm body);
    
    /**
     * This function appends a sink addres to the function body. This is used in the
     * register allocation. An empty instruction is added that contains source
     * registers. This way we know that certain registers are live at the end of the 
     * last instruction 
     */
    abstract public com.chaosopher.tigerlang.compiler.assem.InstrList procEntryExit2(com.chaosopher.tigerlang.compiler.assem.InstrList body);
    
    /**
     * Creates the procedure prologue and epilogue assembly language. 
     * First (for some machines) it calculates the size of the outgoing parameter space in the frame. 
     * This is equal to the maximum number of outgoing parameters of any CALL instruction in the procedure body. 
     * Unfortunately, after conversion to Assem trees the procedure calls have been separated from their arguments, 
     * so the outgoing parameters are not obvious. 
     * Either procEntryExit2 should scan the body and record this information in some new component of the frame type, 
     * or procEntryExit3 should use the maximum legal value.
     */
    abstract public Proc procEntryExit3(com.chaosopher.tigerlang.compiler.assem.InstrList body);
    

    /**
     * Calls an external function outside of Tiger
     */
    abstract public com.chaosopher.tigerlang.compiler.tree.Exp externalCall(String func, com.chaosopher.tigerlang.compiler.tree.ExpList args);

    abstract public String string(Label l, String literal);

    abstract public TempList registers();

	public abstract InstrList tempToMemory(Temp temp, Temp spillTemp, Access access); 

    public abstract InstrList memoryToTemp(Temp temp, Temp spillTemp, Access access);
    
}
