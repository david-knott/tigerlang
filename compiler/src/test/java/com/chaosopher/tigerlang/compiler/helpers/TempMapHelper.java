package com.chaosopher.tigerlang.compiler.helpers;

import java.util.Hashtable;

import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.temp.TempList;
import com.chaosopher.tigerlang.compiler.temp.TempMap;

public class TempMapHelper {

    public Hashtable<Temp, String> colours = new Hashtable<Temp, String>();

    public TempMapHelper(TempMap tempMap, TempList registers) {
        for (; registers != null; registers = registers.tail) {
            if (tempMap.tempMap(registers.head) != null)
                this.colours.put(registers.head, tempMap.tempMap(registers.head));
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("## Temp Map ##");
        builder.append(System.lineSeparator());
        for(Temp t : colours.keySet()) {
            builder.append(t + " -> " + colours.get(t));
            builder.append(System.lineSeparator());
        }
        if(builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append(System.lineSeparator());
        builder.append("##############");
        return builder.toString();
    }
}