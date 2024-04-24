package com.chaosopher.tigerlang.compiler.assem;

import com.chaosopher.tigerlang.compiler.temp.DefaultMap;

public class InstrList {
    
	public static InstrList or(InstrList instrList, Instr instr) {
		return null;
	}
    /**
     * Return this set in reverse.
     * 
     * @return a new linked list with this lists elements in reverse.
     */
    public static InstrList reverse(InstrList me) {
        if (me.tail == null) {
            return new InstrList(me.head);
        }
        return InstrList.append(InstrList.reverse(me.tail), me.head);
    }

    /**
     * @param me
     * @param trueLabel
     * @return
     */
    public static InstrList append(InstrList me, InstrList end) {
        if (me == null && end == null) {
            return null;
        }
        if (me == null && end != null) {
            return end;
        }
        if (me != null && end == null) {
            return me; 
        }
        InstrList res = me;
        for(; end != null; end = end.tail) {
            res = InstrList.append(res, end.head);
        }
        return res;
    }


    /**
     * Appends Instr t onto the end of InstrList me. If InstrList me is null and
     * Instr t is not, a new InstrList with t as head is created a returned to the
     * caller.
     * 
     * @param me
     * @param t
     * @return
     */
    public static InstrList append(InstrList me, Instr t) {
        if (me == null && t == null) {
            return null;
        }
        if (me == null && t != null) {
            return new InstrList(t);
        }
        if (me.tail == null) {
            return new InstrList(me.head, new InstrList(t));
        }
        return new InstrList(me.head, InstrList.append(me.tail, t));
    }

    public Instr head;
    public InstrList tail;

    public InstrList(Instr h) {
        head = h;
    }

    public InstrList(Instr h, InstrList t) {
        head = h;
        tail = t;
    }

    public int size() {
        int i = 0;
        InstrList end = this;
        for (; end != null; end = end.tail)
            i++;
        return i;

    }

    public void dump() {
        InstrList instrList = this;
        System.out.println("### Instruction List ###");
        for (; instrList != null; instrList = instrList.tail) {
            System.out.println(
                    instrList.head.getClass().getSimpleName() + "=>" + instrList.head.format(new DefaultMap()));
        }
        System.out.println("########################");
    }


}
