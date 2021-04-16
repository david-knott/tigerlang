package com.chaosopher.tigerlang.compiler.dataflow;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.chaosopher.tigerlang.compiler.canon.CanonicalizationImpl;
import com.chaosopher.tigerlang.compiler.translate.FragList;
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
      //  TreeDeatomizer deatomizer = TreeDeatomizer.apply(fragList);
      //  FragmentPrinter.apply(deatomizer.getDeatomizedFragList(), System.out);
    }

    @Test
    public void cjumpReplaceLeftNestedBinop() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(t1), binop(PLUS, temp(a), temp(b))) " +  
            "move(temp(t2), binop(PLUS, temp(c), temp(t1))) " +  
            "cjump(LE, temp(t2), temp(a), l2, end) " + 
            "label(l2) " + 
            "label(end)";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        FragList fragList = new FragList(new ProcFrag(stmList, null));
//        TreeDeatomizer deatomizer = TreeDeatomizer.apply(fragList);
  //      FragmentPrinter.apply(deatomizer.getDeatomizedFragList(), System.out);
    }

    @Test
    public void cjumpReplaceLeft() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(t1), binop(PLUS, temp(a), temp(b))) " +  
            "cjump(LE, temp(t1), temp(a), l2, end) " + 
            "label(l2) " + 
            "label(end)";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        FragList fragList = new FragList(new ProcFrag(stmList, null));
//        TreeDeatomizer deatomizer = TreeDeatomizer.apply(fragList);
  //      FragmentPrinter.apply(deatomizer.getDeatomizedFragList(), System.out);
    }

    @Test
    public void cjumpReplaceRight() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(t1), binop(PLUS, temp(a), temp(b))) " +  
            "cjump(LE, temp(a), temp(t1), l2, end) " + 
            "label(l2) " + 
            "label(end)";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        FragList fragList = new FragList(new ProcFrag(stmList, null));
//        TreeDeatomizer deatomizer = TreeDeatomizer.apply(fragList);
  //      FragmentPrinter.apply(deatomizer.getDeatomizedFragList(), System.out);
    }

    @Test
    public void cjumpReplaceLeftAndRight() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(t1), binop(PLUS, temp(a), temp(b))) " +  
            "move(temp(t2), binop(PLUS, temp(c), temp(d))) " +  
            "cjump(LE, temp(t1), temp(t2), l2, end) " + 
            "label(l2) " + 
            "label(end)";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        FragList fragList = new FragList(new ProcFrag(stmList, null));
//        TreeDeatomizer deatomizer = TreeDeatomizer.apply(fragList);
  //      FragmentPrinter.apply(deatomizer.getDeatomizedFragList(), System.out);
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
//        TreeDeatomizer deatomizer = TreeDeatomizer.apply(fragList);
  //      FragmentPrinter.apply(deatomizer.getDeatomizedFragList(), System.out);
    }

    @Test
    public void cannotthreeNestedBinops() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(t1), binop(PLUS, temp(a), temp(b))) " +  
           // "move(temp(t1), const(1)) " +  
            "move(temp(t2), binop(MUL, temp(t1), temp(c))) " +  
            "move(temp(t3), binop(MUL, temp(t2), temp(d))) " +  
            "sxp(call(name(f), temp(t3))) " +
            "label(end)";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        FragList fragList = new FragList(new ProcFrag(stmList, null));
//        TreeDeatomizer deatomizer = TreeDeatomizer.apply(fragList);
  //      FragmentPrinter.apply(deatomizer.getDeatomizedFragList(), System.out);
    }

    @Test
    public void memDst() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(t1), binop(PLUS, temp(a), temp(b))) " +  
           // "move(temp(t1), const(1)) " +  
            "move(mem(temp(t3)), binop(MUL, temp(t2), temp(d))) " +  
            "label(end)";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        FragList fragList = new FragList(new ProcFrag(stmList, null));
//        TreeDeatomizer deatomizer = TreeDeatomizer.apply(fragList);
  //      FragmentPrinter.apply(deatomizer.getDeatomizedFragList(), System.out);
    }

    @Test
    public void memSrc() throws IOException {
        String code = 
            "label(start) " + 
            "move(temp(t1), binop(PLUS, temp(a), temp(b))) " +  
            "move(temp(t2),mem(temp(t1)) " +  
            "sxp(call(name(f), temp(t2))) " +
            "label(end)";
        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        FragList fragList = new FragList(new ProcFrag(stmList, null));
//        TreeDeatomizer deatomizer = TreeDeatomizer.apply(fragList);
//        FragmentPrinter.apply(deatomizer.getDeatomizedFragList(), System.out);
    }


    @Test
    public void atomizeFUnctin() throws IOException {

        String code = "sxp(" +
            "call(" +
                "name(printi),"+
                "call("+
                "name(L0),"+
                "mem("+
                    "binop("+
                    "PLUS,"+
                    "temp(rbp),"+
                    "const(8)"+
                    ")"+
                "),"+
                "const(56)"+
                ")"+
            ")" +
        ")";
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        TreeAtomizer treeAtomizer = TreeAtomizer.apply(new CanonicalizationImpl(), stmList);
        StmList atoms = treeAtomizer.getCanonicalisedAtoms();
        atoms.accept(new PrettyPrinter(System.out));

    }

    @Test
    public void function() throws IOException {
        String code = 

        "label(L1)" +
"move(" +
  "temp(t1)," +
  "binop(" +
    "PLUS," +
    "temp(rbp)," +
    "const(8)" +
  ")" +
")" +
"move(" +
  "temp(t2)," +
  "mem(" +
    "temp(t1)" +
  ")" +
")" +
"move(" +
  "temp(t3)," +
  "call(" +
    "name(L0)," +
    "temp(t2)," +
    "const(56)" +
  ")" +
")" +
"sxp(" +
  "call(" +
    "name(printi)," +
    "temp(t3)" +
  ")" +
")" +
"jump(" +
  "name(L0)" +
")"+
"label(L0)"

        ;
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        StmList stmList = (StmList)parser.parse();
        FragList fragList = new FragList(new ProcFrag(stmList, null));
//        TreeDeatomizer deatomizer = TreeDeatomizer.apply(fragList);
  //      FragmentPrinter.apply(deatomizer.getDeatomizedFragList(), System.out);
    }
}