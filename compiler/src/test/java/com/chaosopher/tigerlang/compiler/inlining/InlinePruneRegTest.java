package com.chaosopher.tigerlang.compiler.inlining;

import com.chaosopher.tigerlang.compiler.e2e.RegressionTestBase;

import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class InlinePruneRegTest extends RegressionTestBase{

    @Override
    public String[] getArgs() {
      return new String[] {"--reg-alloc", "--inline", "--prune" };
    }
}