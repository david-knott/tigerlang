package com.chaosopher.tigerlang.compiler.dataflow;

import com.chaosopher.tigerlang.compiler.e2e.RegressionTestBase;

import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class DeatomizeNoEscapeComputeRegTest extends RegressionTestBase{

    @Override
    public String[] getArgs() {
        return new String[]{"--optimize", "--deatomize",  "--reg-alloc"};
    }
}