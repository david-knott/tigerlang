package com.chaosopher.tigerlang.compiler.dataflow;

import com.chaosopher.tigerlang.compiler.e2e.RegressionTestBase;

import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class DataFlowRegTest extends RegressionTestBase{

    @Override
    public String[] getArgs() {
        return new String[]{"--escapes-compute", "--deatomize", "--reg-alloc"};
    }
}