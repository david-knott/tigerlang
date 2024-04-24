package com.chaosopher.tigerlang.compiler.core;

import com.chaosopher.tigerlang.compiler.util.Assert;
public class LL<T> {

    public static <S extends Comparable<S>> int size(LL<S> l) {
        if(l == null)
            return 0; 
        return 1 + LL.<S>size(l.tail);
    }

    public static <S extends Comparable<S>> LL<S> insertRear(LL<S> l, S s) {
        Assert.assertNotNull(s);
        if (l == null && s == null)
            return null;
        if (l == null && s != null)
            return new LL<S>(s);
        if (l.tail == null) {
            return new LL<S>(l.head, new LL<S>(s));
        }
        return new LL<S>(l.head, LL.<S>insertRear(l.tail, s));
    }

    public static <S extends Comparable<S>> LL<S> insertOrdered(LL<S> l, S s) {
        Assert.assertNotNull(s);
        if (l == null || s.compareTo(l.head) < 0) {
            return new LL<S>(s, l);
        }
        l.tail = LL.<S>insertOrdered(l.tail, s);
        return l;
    }

    public static <S extends Comparable<S>> LL<S> sort(LL<S> l) {
        LL<S> sorted = null;
        for(; l != null; l = l.tail) {
            sorted = LL.<S>insertOrdered(sorted, l.head);
        }
        return sorted;
    }

    public static <S extends Comparable<S>> S last(LL<S> l) {
        for(; l.tail != null; l = l.tail);
        return l.head;
    }

    public static <S extends Comparable<S>> LL<S> removeLast(LL<S> l) {
        if(l == null || l.tail == null)
            return null;
        else
            return new LL<S>(l.head, removeLast(l.tail));
    }

    public static <S extends Comparable<S>> LL<S> search(LL<S> me, S s) {
        Assert.assertNotNull(s);
        if (me == null) {
            return null;
        }
        if (me.head == s) {
            return me;
        } else {
            return search(me.tail, s);
        }
    }

    public static <S extends Comparable<S>> boolean contains(LL<S> me, S s) {
        Assert.assertNotNull(s);
        return LL.<S>search(me, s) != null;
    }

    public static <S extends Comparable<S>> LL<S> reverse(LL<S> me) {
        if (me.tail == null) {
            return new LL<S>(me.head);
        }
        return LL.<S>insertRear(LL.<S>reverse(me.tail), me.head);
    }

    /**
     * Performs a set intersection operation and returns a new set. This assumes
     * boths sets are ordered using the same ordering. Uses merge sort.
     * 
     * @param first
     * @param tempList
     * @return
     */
    public static <S extends Comparable<S>> LL<S> and(LL<S> first, LL<S> tempList) {
        if (first == null || tempList == null)
            return null;
        LL<S> and = null;
        do {
            if (first.head.compareTo(tempList.head) < 0) {
                first = first.tail;
            } else if (first.head.compareTo(tempList.head) > 0) {
                tempList = tempList.tail;
            } else {
                and = LL.<S>insertRear(and, first.head);
                first = first.tail;
                tempList = tempList.tail;
            }
        } while (first != null && tempList != null);
        return and;
    }

    /**
     * Performs a set union on me and templist, returns a new set. This assumes
     * boths sets are ordered using the same ordering. Uses merge sort.
     * 
     * @param me
     * @param tempList
     * @return
     */
    public static <S extends Comparable<S>> LL<S> or(LL<S> me, LL<S> tempList) {
        if (me == null && tempList == null)
            return null;
        if (me == null)
            return tempList;
        if (tempList == null)
            return me;
        LL<S> or = null;
        do {
            if (me.head.compareTo(tempList.head) < 0) {
                or = LL.<S>insertRear(or, me.head);
                me = me.tail;
            } else if (me.head.compareTo(tempList.head) > 0) {
                or = LL.<S>insertRear(or, tempList.head);
                tempList = tempList.tail;
            } else {
                or = LL.<S>insertRear(or, me.head);
                me = me.tail;
                tempList = tempList.tail;
            }
        } while (me != null & tempList != null);
        while (me != null) {
            or = LL.<S>insertRear(or, me.head);
            me = me.tail;
        }
        while (tempList != null) {
            or = LL.<S>insertRear(or, tempList.head);
            tempList = tempList.tail;
        }
        return or;
    }

    /**
     * Performs a set subtraction. Returns a new list that contains everything in
     * first set this is present in the second set.
     * 
     * @param me
     * @param tempList
     * @return
     */
    public static <S extends Comparable<S>> LL<S> andNot(LL<S> me, LL<S> tempList) {
        if (me == null)
            return null;
        if (tempList == null) {
            return me;
        }
        LL<S> andNot = null;
        for (; me != null; me = me.tail) {
            if (LL.<S>search(tempList, me.head) == null) {
                andNot = LL.<S>insertRear(andNot, me.head);
            }
        }
        return andNot;
    }


    /**
     * Method recursively returns element at position i.
     * @param <S> the type of element.
     * @param list the list.
     * @param i the position of the element;
     * @return the element at i or throws an exception if i is greater than size of list.
     */
	public static <S extends Comparable<S>> S get(LL<S> list, int i) {
        if (i >= size(list)) {
            throw new Error("out of bounds error.");
        }
        if( i == 0 )
            return list.head;
		return get(list.tail, i - 1);
	}

    public final T head;
    public LL<T> tail;

    public LL(T h) {
        Assert.assertNotNull(h);
        this.head = h;
        this.tail = null;
    }

    LL(T h, LL<T> t) {
        Assert.assertNotNull(h);
        this.head = h;
        this.tail = t;
    }

    public String toString() {
        return head + "," + (tail != null ? tail.toString() : "");
    }
}