package com.chaosopher.tigerlang.compiler.callgraph;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.PrintStream;

import org.junit.Test;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.bind.Binder;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.parse.CupParser;
import com.chaosopher.tigerlang.compiler.parse.Parser;

public class CallGraphCycleTest {

    @Test
    public void noCycle() {
        Parser parser = new CupParser("let function a() : int = b() function b() : int = c() function c() : int = 1 in a() end", new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        CallGraphVisitor  callGraphVisitor = new CallGraphVisitor();
        program.accept(new Binder(new ErrorMsg("f", System.out)));
        program.accept(callGraphVisitor);
        assertFalse(callGraphVisitor.functionCallGraph.isCyclic());
    }

    @Test
    public void simpleCycle() {
        Parser parser = new CupParser("let function a() = a() in a() end", new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        CallGraphVisitor  callGraphVisitor = new CallGraphVisitor();
        program.accept(new Binder(new ErrorMsg("f", System.out)));
        program.accept(callGraphVisitor);
        assertTrue(callGraphVisitor.functionCallGraph.isCyclic());
    }

    @Test
    public void complexCycle() {
        Parser parser = new CupParser("function _main() = ( let function a() = (b(); c()) function b() = c() function c() = c() in a() end )", new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        CallGraphVisitor  callGraphVisitor = new CallGraphVisitor();
        program.accept(new Binder(new ErrorMsg("f", System.out)));
        program.accept(callGraphVisitor);
        assertTrue(callGraphVisitor.functionCallGraph.isCyclic());
        callGraphVisitor.functionCallGraph.show(System.out);
    }
}