package com.chaosopher.tigerlang.compiler.e2e;

import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;
@RunWith(Theories.class)
public class RegTest extends RegressionTestBase{

    @Override
    public String[] getArgs() {
        return new String[]{"--reg-alloc"};
    }
}