package com.chaosopher.tigerlang.compiler.regalloc;

import com.chaosopher.tigerlang.compiler.assem.InstrList;
import com.chaosopher.tigerlang.compiler.frame.Frame;

public class RegAllocFactory {
    
    public RegAlloc getRegAlloc(String name, Frame frame, InstrList instrList) {
        return new IterativeCoalescing(frame, instrList);
    }
}