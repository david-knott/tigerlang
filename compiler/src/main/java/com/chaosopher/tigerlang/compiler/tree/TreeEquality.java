package com.chaosopher.tigerlang.compiler.tree;

public class TreeEquality extends DefaultTreeVisitor {

    public static boolean areEqual(IR expected, IR actual) {
        return false;
    }

    private final IR expected;

    TreeEquality(IR expected) {
        this.expected = expected;
    }

}