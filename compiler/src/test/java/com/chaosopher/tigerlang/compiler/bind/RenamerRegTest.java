package com.chaosopher.tigerlang.compiler.bind;

import com.chaosopher.tigerlang.compiler.e2e.RegressionTestBase;

import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class RenamerRegTest extends RegressionTestBase{

    @Override
    public String[] getArgs() {
        return new String[] {"--reg-alloc", "--rename", "--ast-display", "--escapes-compute", "--demove" };
    }
}