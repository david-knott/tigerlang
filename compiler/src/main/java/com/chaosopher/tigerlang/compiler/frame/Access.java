package com.chaosopher.tigerlang.compiler.frame;

public abstract class Access {

    public abstract Tree.Exp exp(Tree.Exp framePtr);
}