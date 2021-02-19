package com.chaosopher.tigerlang.compiler.staticlink;

import com.chaosopher.tigerlang.compiler.e2e.RegressionTestBase;

import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class StaticLinkEscapesOptimiseRegTest extends RegressionTestBase{

    @Override
    public String[] getArgs() {
        return new String[] {"--optimise-staticlinks-escapes", "--escapes-compute", "--lir-compute", "--reg-alloc"};
    }
}