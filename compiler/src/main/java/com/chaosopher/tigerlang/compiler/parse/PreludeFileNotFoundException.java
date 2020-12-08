package com.chaosopher.tigerlang.compiler.parse;

public class PreludeFileNotFoundException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -3790312442182593055L;

    public PreludeFileNotFoundException() {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
    }

}