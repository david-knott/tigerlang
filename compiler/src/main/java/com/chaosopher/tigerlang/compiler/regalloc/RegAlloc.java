package com.chaosopher.tigerlang.compiler.regalloc;

import com.chaosopher.tigerlang.compiler.assem.InstrList;
import com.chaosopher.tigerlang.compiler.temp.TempMap;

public interface RegAlloc extends TempMap {

    InstrList getInstrList();

}