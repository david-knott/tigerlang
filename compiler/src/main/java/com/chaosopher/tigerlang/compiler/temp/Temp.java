package com.chaosopher.tigerlang.compiler.temp;

import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.SortedMap;
import java.util.TreeMap;

public class Temp implements Comparable<Temp> {

   public static TempList all() {
       TempList all = null;
       for(String key : temps.keySet()) {
            all = TempList.append(all, temps.get(key));
       }
       return all;
   }

    private static LinkedHashMap<String, Temp> temps = new LinkedHashMap<String, Temp>();
    private static Hashtable<Temp, String> revTemps = new Hashtable<Temp, String>();
    private static int count;

    public static Temp create() {
        Temp temp = new Temp();
        temps.put(temp.toString(), temp);
        revTemps.put(temp, temp.toString());
        return temp;
    }

    public static Temp create(String name) {
        if (temps.containsKey(name)) {
            return temps.get(name);
        }
        Temp temp = new Temp();
        temps.put(name, temp);
        revTemps.put(temp, name);
        return temp;
    }

	public static String name(Temp head) {
		return revTemps.get(head);
	}

    private int num;

    private Temp() {
        num = count++;
    }

    @Override
    public String toString() {
        return "t" + num;
    }

    @Override
    public int hashCode() {
        return num;
    }

    @Override
    public int compareTo(Temp o) {
        return o.num == this.num ? 0 : (o.num < this.num ? -1 : 1);
    }

}
