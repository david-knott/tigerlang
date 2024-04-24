package com.chaosopher.tigerlang.compiler.symbol;

class GenericBinder<T> {
    T value;
    Symbol prevtop;
    GenericBinder<T> tail;

    GenericBinder(final T v, final Symbol p, final GenericBinder<T> t) {
        value = v;
        prevtop = p;
        tail = t;
    }
}

/**
 * The Table class is similar to java.util.Dictionary, except that each key must
 * be a Symbol and there is a scope mechanism.
 */
public class GenericTable<T> implements SymbolTable<T> {

    private final java.util.Dictionary<Symbol, GenericBinder<T>> dict = new java.util.Hashtable<Symbol, GenericBinder<T>>();
    private Symbol top;
    private GenericBinder<T> marks;

    /**
     * Gets the object associated with the specified symbol in the Table.
     */
    public T get(final Symbol key) {
        final GenericBinder<T> e = dict.get(key);
        return e == null ? null : e.value;
    }

    /**
     * Puts the specified value into the Table, bound to the specified Symbol.
     */
    public void put(final Symbol key, final T value) {
        dict.put(key, new GenericBinder<T>(value, top, (GenericBinder<T>) dict.get(key)));
        top = key;
    }

    /**
     * Remembers the current state of the Table.
     */
    public void beginScope() {
        marks = new GenericBinder<T>(null, top, marks);
        top = null;
    }

    /**
     * Restores the table to what it was at the most recent beginScope that has not
     * already been ended.
     */
    public void endScope() {
        while (top != null) {
            final GenericBinder<T> e = dict.get(top);
            if (e.tail != null)
                dict.put(top, e.tail);
            else
                dict.remove(top);
            top = e.prevtop;
        }
        top = marks.prevtop;
        marks = marks.tail;
    }

    /**
     * Returns an enumeration of the Table's symbols.
     */
    public java.util.Enumeration<Symbol> keys() {
        return dict.keys();
    }
}