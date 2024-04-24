package com.chaosopher.tigerlang.compiler.assem;

public abstract class Instr implements Comparable<Instr> {
    public String assem;

    public abstract com.chaosopher.tigerlang.compiler.temp.TempList use();

    public abstract com.chaosopher.tigerlang.compiler.temp.TempList def();

    public abstract Targets jumps();

    private com.chaosopher.tigerlang.compiler.temp.Temp nthTemp(com.chaosopher.tigerlang.compiler.temp.TempList l, int i) {
        if (i == 0) {

            if (l == null) {
                throw new Error("L is null:" + assem);
            }
            return l.head;
        } else
            return nthTemp(l.tail, i - 1);
    }

    private com.chaosopher.tigerlang.compiler.temp.Label nthLabel(com.chaosopher.tigerlang.compiler.temp.LabelList l, int i) {
        if (i == 0)
            return l.head;
        else
            return nthLabel(l.tail, i - 1);
    }

    /**
     * Special formatting function that writes out assembly
     * 
     * @param m
     * @return
     */
    public String format(com.chaosopher.tigerlang.compiler.temp.TempMap m) {
        com.chaosopher.tigerlang.compiler.temp.TempList dst = def();
        com.chaosopher.tigerlang.compiler.temp.TempList src = use();
        Targets j = jumps();
        com.chaosopher.tigerlang.compiler.temp.LabelList jump = (j == null) ? null : j.labels;
        StringBuffer s = new StringBuffer();
        int len = assem.length();
        for (int i = 0; i < len; i++)
            if (assem.charAt(i) == '`')
                switch (assem.charAt(++i)) {
                    case 's': {
                        int n = Character.digit(assem.charAt(++i), 10);
                        s.append(m.tempMap(nthTemp(src, n)));
                    }
                        break;
                    case 'd': {
                        int n = Character.digit(assem.charAt(++i), 10);
                        s.append(m.tempMap(nthTemp(dst, n)));
                    }
                        break;
                    case 'j': {
                        int n = Character.digit(assem.charAt(++i), 10);
                        s.append(nthLabel(jump, n).toString());
                    }
                        break;
                    case '`':
                        s.append('`');
                        break;
                    default:
                        throw new Error("bad Assem format");
                }
            else
                s.append(assem.charAt(i));

        return s.toString();
    }

    @Override
    public int compareTo(Instr o) {
        return this.hashCode() == o.hashCode() ? 0 : ( this.hashCode() < o.hashCode() ? -1 : 1);
    }

    @Override
    public String toString() {
        return hashCode() +  ":" + this.assem;
    }

}
