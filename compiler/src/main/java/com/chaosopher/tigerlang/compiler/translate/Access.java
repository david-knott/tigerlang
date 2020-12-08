package com.chaosopher.tigerlang.compiler.translate;

public class Access {
    Level home;
    Frame.Access acc;

    public Access(Level l, Frame.Access a) {
        home = l;
        acc = a;
    }
}
