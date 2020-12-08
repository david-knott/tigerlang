package com.chaosopher.tigerlang.compiler.main;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;

import com.chaosopher.tigerlang.compiler.canon.CanonicalizationImpl;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;
import com.chaosopher.tigerlang.compiler.regalloc.RegAllocFactory;
import com.chaosopher.tigerlang.compiler.util.TaskRegister;
import com.chaosopher.tigerlang.compiler.util.Timer;

/**
 * Main class that executes the compiler.
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Timer.instance.start();
        Timer.instance.push("rest");
        PrintStream out = System.out;
        if (args.length == 1) {
           args = new String[] {"--reg-alloc", "--escapes-compute", "--demove", args[0] };
        }
        InputStream in = new java.io.FileInputStream(args[args.length - 1]);
        PrintStream err = System.err;
        ErrorMsg errorMsg = new ErrorMsg(args[args.length - 1], err);
        new TaskRegister()
                .register(new Tasks())
                .register(new com.chaosopher.tigerlang.compiler.parse.Tasks(new ParserService(new ParserFactory())))
                .register(new com.chaosopher.tigerlang.compiler.cloner.Tasks())
                .register(new com.chaosopher.tigerlang.compiler.callgraph.Tasks())
                .register(new com.chaosopher.tigerlang.compiler.liveness.Tasks())
                .register(new com.chaosopher.tigerlang.compiler.inlining.Tasks())
                .register(new com.chaosopher.tigerlang.compiler.sugar.Tasks())
                .register(new com.chaosopher.tigerlang.compiler.bind.Tasks())
                .register(new com.chaosopher.tigerlang.compiler.findescape.Tasks())
                .register(new com.chaosopher.tigerlang.compiler.absyn.Tasks())
                .register(new com.chaosopher.tigerlang.compiler.types.Tasks())
                .register(new com.chaosopher.tigerlang.compiler.translate.Tasks())
                .register(new com.chaosopher.tigerlang.compiler.canon.Tasks(new CanonicalizationImpl()))
                .register(new com.chaosopher.tigerlang.compiler.intel.Tasks(null, null))
                .register(new com.chaosopher.tigerlang.compiler.regalloc.Tasks(new RegAllocFactory()))
        /*
                .register(new Cloner.Tasks())
                .register(new CallGraph.Tasks())
                .register(new Liveness.Tasks())
                .register(new Inlining.Tasks())
                .register(new Sugar.Tasks())
                .register(new Bind.Tasks())
                .register(new FindEscape.Tasks())
                .register(new com.chaosopher.tigerlang.compiler.absyn.Tasks())
                .register(new Types.Tasks())
                .register(new Translate.Tasks())
                .register(new Canon.Tasks(new CanonicalizationImpl()))
                .register(new Intel.Tasks(null, null))
                .register(new RegAlloc.Tasks(new RegAllocFactory()))
                */
                .parseArgs(args)
                .execute(in, out, err, errorMsg);
        Timer.instance.stop();
        Timer.instance.dump(err);
    }
}
