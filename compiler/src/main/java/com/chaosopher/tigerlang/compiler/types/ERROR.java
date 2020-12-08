package com.chaosopher.tigerlang.compiler.types;

public class ERROR extends Type {

    public static ERROR instance = new ERROR();

    private ERROR() {
    }

    @Override
    public void accept(GenVisitor genVisitor) {
    }
}