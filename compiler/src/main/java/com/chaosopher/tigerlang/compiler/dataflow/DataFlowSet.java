package com.chaosopher.tigerlang.compiler.dataflow;
import java.util.HashMap;
import java.util.HashSet;

public class DataFlowSet<T> {

    private final HashSet<T> hashSet;

    public DataFlowSet() {
        this.hashSet = new HashSet<T>();
    }

    public void and(DataFlowSet<T> set) {
        this.hashSet.retainAll(set.hashSet);
    }

    public void andNot(DataFlowSet<T> set) {
        this.hashSet.removeAll(set.hashSet);
    }

    public void or(DataFlowSet<T> set) {
        this.hashSet.addAll(set.hashSet);
    }
    
    public void add(T item) {
        this.hashSet.add(item);
    }

    public void remove(T item) {
        this.hashSet.remove(item);
    }

    @Override
    public String toString() {
        return this.hashSet.toString();
    }

	public boolean contains(T item) {
        return this.hashSet.contains(item);
	}

	public int size() {
        return this.hashSet.size();
	}

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DataFlowSet) {
            HashSet<T> flowSet = ((DataFlowSet<T>)obj).hashSet;
            return this.hashSet.equals(flowSet);
        }
        return false;
    }
}