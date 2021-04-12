package com.chaosopher.tigerlang.compiler.dataflow;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.chaosopher.tigerlang.compiler.dataflow.GenKillSets;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.translate.FragList;
import com.chaosopher.tigerlang.compiler.translate.FragmentPrinter;
import com.chaosopher.tigerlang.compiler.translate.FragmentVisitor;
import com.chaosopher.tigerlang.compiler.translate.ProcFrag;
import com.chaosopher.tigerlang.compiler.tree.Lexer;
import com.chaosopher.tigerlang.compiler.tree.Parser;
import com.chaosopher.tigerlang.compiler.tree.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.tree.StmList;

import org.junit.Test;

public class TreeDeatomizerTest {

    @Test
    public void twoNestedBinops() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(t1), binop(PLUS, temp(a), temp(b))) " +  
            "move(temp(t2), binop(MUL, temp(t1), temp(c))) " +  
            "label(end)";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        FragList fragList = new FragList(new ProcFrag(stmList, null));
        TreeDeatomizer deatomizer = TreeDeatomizer.apply(fragList);
        FragmentPrinter.apply(deatomizer.fragList, System.out);
    }


    @Test
    public void threeNestedBinops() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(t1), binop(PLUS, temp(a), temp(b))) " +  
            "move(temp(t2), binop(MUL, temp(t1), temp(c))) " +  
            "move(temp(t3), binop(MUL, temp(t2), temp(d))) " +  
            "label(end)";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        FragList fragList = new FragList(new ProcFrag(stmList, null));
        TreeDeatomizer deatomizer = TreeDeatomizer.apply(fragList);
        FragmentPrinter.apply(deatomizer.fragList, System.out);
    }


    @Test
    public void cannotthreeNestedBinops() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(t1), binop(PLUS, temp(a), temp(b))) " +  
           // "move(temp(t1), const(1)) " +  
            "move(temp(t2), binop(MUL, temp(t1), temp(c))) " +  
            "move(temp(t3), binop(MUL, temp(t2), temp(d))) " +  
            "label(end)";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        FragList fragList = new FragList(new ProcFrag(stmList, null));
        TreeDeatomizer deatomizer = TreeDeatomizer.apply(fragList);
        FragmentPrinter.apply(deatomizer.fragList, System.out);
    }



}