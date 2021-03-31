package com.chaosopher.tigerlang.compiler.temp;

import java.util.Hashtable;
import java.util.LinkedHashMap;

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
        Temp temp = new Temp(name);
        temps.put(name, temp);
        revTemps.put(temp, name);
        return temp;
    }

	public static String name(Temp head) {
		return revTemps.get(head);
	}

    private int num;
    private String name;

    Temp() {
        this.num = count++;
        this.name = "t" + num;
    }

    Temp(String name) {
        this.num = count++;
        this.name = name;
    }

    @Override
    public String toString() {
       return this.name;
    }

    @Override
    public int hashCode() {
        return num;
    }

    /**
     * Two temps are equal if either they are the same reference object
     * or they have the same name. Where two objects are the same reference
     * they will have the same hash.
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Temp)) {
            return false;
        }
        Temp other = (Temp)obj;
        return 
            other.name == this.name ||
            other == this;
    }

    @Override
    public int compareTo(Temp o) {
        return o.num == this.num ? 0 : (o.num < this.num ? -1 : 1);
    }

}
