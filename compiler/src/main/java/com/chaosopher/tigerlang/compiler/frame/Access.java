package com.chaosopher.tigerlang.compiler.frame;

public abstract class Access {

    public abstract com.chaosopher.tigerlang.compiler.tree.Exp exp(com.chaosopher.tigerlang.compiler.tree.Exp framePtr);
}