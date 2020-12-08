package com.chaosopher.tigerlang.compiler.translate;

public class FragList {

    public static FragList append(FragList me, Frag t) {
        if(me == null) {
            return new FragList(t);
        }
        if (me.tail == null) {
            return new FragList(me.head, new FragList(t));
        }
        return new FragList(me.head, FragList.append(me.tail, t));
    }

    /**
     * Return this set in reverse.
     * 
     * @return a new linked list with this lists elements in reverse.
     */
    public static FragList reverse(FragList me) {
        if(me == null){
            return null;
        }
        if (me.tail == null) {
            return new FragList(me.head);
        }
        // return me.tail.reverse().append(me.head);
        return FragList.append(FragList.reverse(me.tail), me.head);
    }

    public Frag head;
    public FragList tail;

    public FragList(Frag frag) {
        this.head = frag;
        this.tail = null;
    }

    public FragList(Frag frag, FragList tail) {
        this.head = frag;
        this.tail = tail;
    }

    public void accept(FragmentVisitor fragmentVisitor) {
        for(FragList fl = this; fl != null; fl = fl.tail) {
            fl.head.accept(fragmentVisitor);
        }
    }
}