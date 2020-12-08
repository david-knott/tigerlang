package com.chaosopher.tigerlang.compiler.core;

public abstract class Component {

    public java.util.Hashtable<CompilerEventType, ListenerList<?>> listenerLists = new java.util.Hashtable<CompilerEventType, ListenerList<?>>(); 
  
    public <T> void trigger(CompilerEventType type, T message) {
        ListenerList listenerList = listenerLists.get(type);
        for(ListenerList ll = listenerList ;ll != null; ll = ll.tail) {
            ll.listener.handle(message);
        }
    }
    
    public <T> void on(CompilerEventType type, Listener<T> listener) {
        ListenerList listenerList = listenerLists.get(type);
        listenerList = new ListenerList(listener, listenerList);
        listenerLists.put(type, listenerList);    
    }
    
    public void off(CompilerEventType type) {
      ListenerList listenerList = listenerLists.remove(type);
      listenerList = null;
    }
    
}