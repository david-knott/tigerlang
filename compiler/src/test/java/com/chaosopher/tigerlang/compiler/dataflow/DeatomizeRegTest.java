package com.chaosopher.tigerlang.compiler.dataflow;

import com.chaosopher.tigerlang.compiler.e2e.RegressionTestBase;

import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class DeatomizeRegTest extends RegressionTestBase{

    @Override
    public String[] getArgs() {
        //return new String[]{"--desugar",  "--lir-compute", "--reg-alloc"};
        return new String[]{"--escapes-compute", "--optimize", "--deatomize",  "--reg-alloc"};
        //return new String[]{"--reg-alloc"};
    }
}