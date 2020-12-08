package com.chaosopher.tigerlang.compiler.assem;

public class FragList {

    public static FragList append(FragList me, Fragment t) {
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

    public Fragment head;
    public FragList tail;

    public FragList(Fragment frag) {
        this.head = frag;
        this.tail = null;
    }

    public FragList(Fragment frag, FragList tail) {
        this.head = frag;
        this.tail = tail;
    }

    public void accept(FragmentVisitor fragmentVisitor) {
        for(FragList fl = this; fl != null; fl = fl.tail) {
            fl.head.accept(fragmentVisitor);
        }
    }
}