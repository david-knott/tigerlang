package com.chaosopher.tigerlang.compiler.core;

public class ListenerList<T> {
    public ListenerList<T> tail;
    public Listener<T> listener;
    
    public ListenerList(Listener<T> l, ListenerList<T> t) {
        this.listener = l;
        this.tail = t;
    }

}