package com.chaosopher.tigerlang.compiler.translate;

import java.io.OutputStream;
import java.io.PrintStream;

import com.chaosopher.tigerlang.compiler.tree.PrettyPrinter;

public class FragmentPrinter implements FragmentVisitor {

    public static void apply(FragList fragList, PrintStream out) {
        FragmentPrinter fragmentPrinter = new FragmentPrinter(out);
        fragList.accept(fragmentPrinter);
    }

    private PrintStream out;

    public FragmentPrinter(PrintStream out) {
        this.out = out;
    }

    public FragmentPrinter(OutputStream log) {
        this.out = new PrintStream(log);
	}

	@Override
    public void visit(ProcFrag procFrag) {
        this.out.println("# Fragment " + (procFrag.frame == null ? "no frame" : procFrag.frame.name));
        procFrag.body.accept(new PrettyPrinter(this.out));
       // this.out.println("Frame:");
       // this.out.println("Wordsize:" + procFrag.frame.wordSize());
        //show layout, locals, formals, arguments
    }

    @Override
    public void visit(DataFrag dataFrag) {
        this.out.println("# Data:");
        this.out.println(dataFrag.getData());
    }

}