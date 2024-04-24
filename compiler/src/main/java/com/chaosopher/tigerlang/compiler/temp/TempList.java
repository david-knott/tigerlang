package com.chaosopher.tigerlang.compiler.temp;

import java.util.Hashtable;

public class TempList {

   public static void checkOrder(TempList test) {
      int hc = -1;
      TempList tempList = test;
      for(;tempList != null; tempList = tempList.tail) {
         if(hc < tempList.head.hashCode()) {
            hc = tempList.head.hashCode();
         } else {
            throw new Error("TempList is not ordered: " + hc + ">=" + tempList.head.hashCode() + " " + test);
         }
      }
   }

   public static boolean contains(TempList s, Temp n) {
      for (; s != null; s = s.tail) {
         if (s.head == n) {
            return true;
         }
      }
      return false;
   }

   public static TempList merge(TempList one, TempList two) {
             if(one == null) {
                 return two;
             }
             if(two == null) {
                 return one;
             }
             TempList result = null;
             if(one.head.hashCode()  < two.head.hashCode()) {
                 result = one;
                 result.tail = merge(one.tail, two);
             } else {
                 result = two;
                 result.tail = merge(one, two.tail);
             }
             return result;
         }
         
         public TempList sort() {
             return TempList.sort(this);
         }
         
         public static TempList sort(TempList tl) {
            if(tl == null) {
               return null;
            }
            if(tl.tail == null) {
                return new TempList(tl.head);
            }
            TempList[] split = tl.split();
            TempList first = sort(split[0]); //new list
            TempList second = sort(split[1]); //new list
            return merge(first, second);
         }
         
         public TempList[] split() {
            if (this.tail == null) {
               return new TempList[] { new TempList(this.head), null };
            }
            TempList slow = this, fast = this.tail;
            while (fast != null && (fast = fast.tail) != null) {
               // advance fast by 1 + 1 from check above
               fast = fast.tail;
               // advance slow by 1
               slow = slow.tail;
            }
            // slow is last item in first list
            // advance slow to first item in last list
            slow = slow.tail;
            // build new lists
            TempList first = null;
            TempList second = null;
            TempList tl = this;
            for (; tl != slow; tl = tl.tail) {
               first = new TempList(tl.head, first);
            }

            for (; tl != null; tl = tl.tail) {
               second = new TempList(tl.head, second);
            }

            return new TempList[] { first, second };
         }

   /**
    * Return this set in reverse.
    * 
    * @return a new linked list with this lists elements in reverse.
    */
   public static TempList reverse(TempList me) {
      if (me.tail == null) {
         return new TempList(me.head);
      }
      return TempList.append(TempList.reverse(me.tail), me.head);
   }

   /**
    * Appends Temp t onto the end of TempList me. If TempList me is null and Temp t
    * is not, a new TempList with t as head is created a returned to the caller.
    * 
    * @param me
    * @param t
    * @return
    */
   public static TempList append(TempList me, Temp t) {
      if (me == null && t == null) {
         return null;
      }
      if (me == null && t != null) {
         return new TempList(t);
      }
      if (me.tail == null) {
         return new TempList(me.head, new TempList(t));
      }
      return new TempList(me.head, TempList.append(me.tail, t));
   }

   /**
    * Append two two tists.
    * @param me
    * @param end
    * @return
    */
   public static TempList append(TempList me, TempList end) {
      if (me == null && end == null) {
         return null;
      }
      if (me == null && end != null) {
         return end;
      }
      if (me != null && end == null) {
         return me;
      }
      TempList res = me;
      for (; end != null; end = end.tail) {
         res = TempList.append(res, end.head);
      }
      return res;
   }

   /**
    * Performs a set intersection operation and returns a new set. This assumes
    * boths sets are ordered using the same ordering. Uses merge sort.
    * 
    * @param me
    * @param tempList
    * @return
    */
   public static TempList and(TempList me, TempList tempList) {
      if (me == null || tempList == null)
         return null;
      me = TempList.sort(me);
      tempList = TempList.sort(tempList);
      TempList.checkOrder(me);
      TempList.checkOrder(tempList);
      TempList and = null;
      do {
         if(me.head.compareTo(tempList.head) > 0) {
            me = me.tail;
         } else if(me.head.compareTo(tempList.head) < 0) {
            tempList = tempList.tail;
         } else {
            and = TempList.append(and, me.head);
            me = me.tail;
            tempList = tempList.tail;
         }
      } while(me != null && tempList != null);
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
   public static TempList or(TempList me, TempList tempList) {
      if (me == null && tempList == null)
         return null;
      if (me == null)
         return tempList;
      if (tempList == null)
         return me;
      me = TempList.sort(me);
      tempList = TempList.sort(tempList);
      TempList.checkOrder(me);
      TempList.checkOrder(tempList);
      TempList or = null;
      do{
         if(me.head.compareTo(tempList.head) > 0) {
            or = TempList.append(or, me.head);
            me = me.tail;
         } else if(me.head.compareTo(tempList.head) < 0) {
            or = TempList.append(or, tempList.head);
            tempList = tempList.tail;
         } else {
            or = TempList.append(or, me.head);
            me = me.tail;
            tempList = tempList.tail;
         }
      }while(me != null & tempList != null);
      while (me != null) {
         or = TempList.append(or, me.head);
         me = me.tail;
      }
      while (tempList != null) {
         or = TempList.append(or, tempList.head);
         tempList = tempList.tail;
      }
      return or;
   }

   /**
    * Performs a set subtraction. Returns a new list that contains
    * everything in first set this is present in the second set.
    * @param me
    * @param tempList
    * @return
    */
   public static TempList andNot(TempList me, TempList tempList) {
      if (me == null)
         return null;
      if (tempList == null) {
         return me;
      }
      me = TempList.sort(me);
      tempList = TempList.sort(tempList);
      TempList.checkOrder(me);
      TempList.checkOrder(tempList);
      TempList andNot = null;
      for (; me != null; me = me.tail) {
         if (!tempList.contains(me.head)) {
            andNot = TempList.append(andNot, me.head);
         }
      }
      return andNot;
   }

   public static TempList create(Temp h) {
      return new TempList(h, null);
   }

   public static TempList create(Temp[] h) {
      TempList tl = null;
      for (int i = 1; i < h.length; i++) {
         tl = TempList.append(tl, h[i]);
      }
      return tl;
   }

   public Temp head;
   public TempList tail;

   public TempList(Temp h) {
      if (h == null)
         throw new Error("h cannot be null");
      head = h;
      tail = null;
   }

   public TempList(Temp h, TempList t) {
      if (h == null)
         throw new Error("h cannot be null");
      head = h;
      tail = t;
   }

   public boolean contains(Temp n) {
      for (TempList s = this; s != null; s = s.tail) {
         if (s.head == n) {
            return true;
         }
      }
      return false;
   }

   public boolean contains(TempList spills) {
      for (TempList s = spills; s != null; s = s.tail) {
         if (this.contains(s.head)) {
            return true;
         }
      }
      return false;
   }

   public int size() {
      int size = 0;
      for (TempList s = this; s != null; s = s.tail) {
         size++;
      }
      return size;
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("[");
      for (TempList s = this; s != null; s = s.tail) {
         builder.append(Temp.name(s.head));
         if (s.tail != null) {
            builder.append(",");
         }
      }
      builder.append("]");
      return builder.toString();
   }

   public String getInfo() {
      StringBuilder builder = new StringBuilder();
      builder.append("[");
      for (TempList s = this; s != null; s = s.tail) {
         builder.append(s.head + "=>" + Temp.name(s.head));
         if (s.tail != null) {
            builder.append(",");
         }
      }
      builder.append("]");
      return builder.toString();
   }


   public Hashtable<Temp, String> toTempMap() {
      Hashtable<Temp, String> ht = new Hashtable<Temp, String>();
      TempList start = this;
      for (; start != null; start = start.tail) {
         ht.put(start.head, Temp.name(start.head));
      }
      return ht;
   }

}