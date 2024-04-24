package com.chaosopher.tigerlang.compiler.util;

import java.io.InputStream;
import java.io.OutputStream;

import com.chaosopher.tigerlang.compiler.absyn.DecList;
import com.chaosopher.tigerlang.compiler.assem.InstrList;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.translate.FragList;

public class TaskContext {
   // public Program program;
    public boolean escapesDisplay = false;
    public boolean bindingsDisplay = false;
    public FragList hirFragList;
    public FragList lirFragList;
    public com.chaosopher.tigerlang.compiler.assem.FragList assemFragList;
    public InputStream in = null;
    public OutputStream out = null;
    public OutputStream log = null;
    public ErrorMsg errorMsg = null;
    public DecList decList;

    public TaskContext(InputStream in, OutputStream out, OutputStream log, ErrorMsg errorMsg) {
        this.in = in;
        this.out = out;
        this.log = log;
        this.errorMsg = errorMsg;
    }

    /*
    public void setAst(final Program value) {
        this.program = value;
    }
*/
    public void setDecList(final DecList value) {
        Assert.assertNotNull(value);
        this.decList = value;
    }

    public void setEscapesDisplay(final boolean value) {
        this.escapesDisplay = value;
    }

    public void setBindingsDisplay(final boolean value) {
        this.bindingsDisplay = value;
    }

    public void setFragList(final FragList frags) {
        Assert.assertNotNull(frags);
        this.hirFragList = frags;
    }

    public void setLIR(final FragList lirFragList) {
        Assert.assertNotNull(lirFragList);
        this.lirFragList = lirFragList;
	}

	public void setInstrList(InstrList instrList) {
    //    this.instrList = instrList;
	}

	public void setAssemFragList(com.chaosopher.tigerlang.compiler.assem.FragList frags) {
        Assert.assertNotNull(frags);
        this.assemFragList = frags;
	}
}