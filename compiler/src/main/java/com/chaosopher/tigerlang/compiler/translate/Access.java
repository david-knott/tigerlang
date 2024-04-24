package com.chaosopher.tigerlang.compiler.translate;

public class Access {
    Level home;
    com.chaosopher.tigerlang.compiler.frame.Access acc;

    public Access(Level l, com.chaosopher.tigerlang.compiler.frame.Access a) {
        home = l;
        acc = a;
    }
}
