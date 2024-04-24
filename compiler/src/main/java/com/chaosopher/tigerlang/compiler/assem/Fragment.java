package com.chaosopher.tigerlang.compiler.assem;

import com.chaosopher.tigerlang.compiler.frame.Frame;

public abstract class Fragment {

    public Fragment next;

    public abstract Frame getFrame();

    public abstract void accept(FragmentVisitor fragmentVisitor);
}