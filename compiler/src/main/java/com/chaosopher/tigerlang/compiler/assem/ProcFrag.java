package com.chaosopher.tigerlang.compiler.assem;

import com.chaosopher.tigerlang.compiler.frame.Frame;

public class ProcFrag extends Fragment {

    /**
     * Intermediate representation function body
     */
    public final InstrList body;
    
    /**
     * Activation record for this function
     */
    public final Frame frame;

    /**
     * Initialises a new instance of a ProcFrag
     * 
     * @param bdt the function body
     * @param frm the activation record for this function
     */
    public ProcFrag(InstrList bdt, Frame frm) {
        body = bdt;
        frame = frm;
    }

    @Override
    public Frame getFrame() {
        return this.frame;
    }

    @Override
    public void accept(FragmentVisitor fragmentVisitor) {
        fragmentVisitor.visit(this);
    }
}