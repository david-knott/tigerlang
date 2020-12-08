package com.chaosopher.tigerlang.compiler.regalloc;

import com.chaosopher.tigerlang.compiler.assem.InstrList;
import com.chaosopher.tigerlang.compiler.liveness.Liveness;
import com.chaosopher.tigerlang.compiler.temp.DefaultMap;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.temp.TempList;

class RegAllocInfor {

	public static void dumpUsesAndDefs(InstrList instrList) {
		System.out.println("### Uses and Defs");
		for (; instrList != null; instrList = instrList.tail) {
			System.out.println(instrList.head.format(new DefaultMap()) + " : def => " + instrList.head.def()
					+ ", use => " + instrList.head.use());
		}
	}

	public static void dumpLiveness(InstrList il, int iterations, Liveness liveness) {
		System.out.println("### Liveness");
		for (InstrList instrList = il; instrList != null; instrList = instrList.tail) {
			System.out.println(instrList.head.format(new DefaultMap()) + " => " + liveness.liveMap(instrList.head));
		}

		System.out.println("Iteration:" + iterations + ")");
		System.out.print("Assembly");
		int maxChars = 0;
		for (InstrList instrList = il; instrList != null; instrList = instrList.tail) {
			String assem = instrList.head.format(new DefaultMap());
			maxChars = Math.max(maxChars, assem.length());
		}
		for (int i = 0; i < maxChars - 8; i++)
			System.out.print(" ");
		for (TempList tl = Temp.all(); tl != null; tl = tl.tail) {
			System.out.print(tl.head);
			System.out.print(" ");
		}
		System.out.print("rc");
		System.out.print(" ");
		System.out.print("ic");
		System.out.print(" ");
		System.out.print("def");
		System.out.println();
		/*
		 * System.out.print("ic"); for(int i = 0; i < maxChars - 2; i++)
		 * System.out.print(" "); for(TempList tl = Temp.all(); tl != null; tl =
		 * tl.tail) { Node n = this.baig.tnode(tl.head); int d = n != null ? n.degree()
		 * : 0; //System.out.print(Integer.toString(d)); System.out.print(" "); }
		 * System.out.println();
		 */
		for (InstrList instrList = il; instrList != null; instrList = instrList.tail) {
			String assem = instrList.head.format(new DefaultMap());
			System.out.print(assem);
			TempList live = liveness.liveMap(instrList.head);
			for (int i = 0; i < maxChars - assem.length(); i++)
				System.out.print(" ");
			int regCount = 0;
			for (TempList tl = Temp.all(); tl != null; tl = tl.tail) {
				for (int i = 0; i < tl.head.toString().length() - 1; i++)
					System.out.print(" ");
				System.out.print((TempList.contains(live, tl.head)) ? "|" : " ");
				if (TempList.contains(live, tl.head))
					regCount++;
				System.out.print(" ");
			}
			System.out.print(" ");
			System.out.print(Integer.toString(regCount));
			System.out.print(" ");
			System.out.print(instrList.head.def());
			System.out.println();
		}
	}

}