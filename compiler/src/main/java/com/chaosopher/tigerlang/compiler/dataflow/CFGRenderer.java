package com.chaosopher.tigerlang.compiler.dataflow;

import java.io.PrintStream;

class CFGRenderer {

    protected final CFG cfg;

    public CFGRenderer(CFG cfg) {
        this.cfg = cfg;
    }

    public void write(PrintStream out) {
        this.cfg.show(out);
    }
}