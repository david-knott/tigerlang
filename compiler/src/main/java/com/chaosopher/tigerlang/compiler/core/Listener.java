package com.chaosopher.tigerlang.compiler.core;

public interface Listener<T> {
    public void handle(T message);
}