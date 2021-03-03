package com.chaosopher.tigerlang.compiler.dataflow;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.bind.Binder;
import com.chaosopher.tigerlang.compiler.canon.CanonicalizationImpl;
import com.chaosopher.tigerlang.compiler.canon.StmListList;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.findescape.EscapeVisitor;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;
import com.chaosopher.tigerlang.compiler.translate.FragmentPrinter;
import com.chaosopher.tigerlang.compiler.translate.ProcFrag;
import com.chaosopher.tigerlang.compiler.translate.TranslatorVisitor;
import com.chaosopher.tigerlang.compiler.tree.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.types.TypeChecker;

import org.junit.Test;

public class CopyPropagationTest {

    @Test
    public void copyPropagation1() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ParserService parserService = new ParserService(new ParserFactory());
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse(" let var a:int := 5 var b:int := 4 function test() : int = if a=1 then 1 else if b=2 then 2 else if a < b then 3 else 4 in printi(test()) end", new ErrorMsg("", System.out));
        // need binder to bind types to expressions.
        program.accept(new Binder(errorMsg));
        program.accept(new TypeChecker(errorMsg));
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(translator);
        ProcFrag procFrag = (ProcFrag)translator.getFragList().head;
        // tree atomizer
        TreeAtomizer treeAtomizer = new TreeAtomizer(new CanonicalizationImpl());
        procFrag.body.accept(treeAtomizer);
        StmList stmList =  treeAtomizer.getCanonicalisedAtoms();
        // 
        GenKillSets genKillSets = new GenKillSets(new CFG(stmList));
        CopyPropagation copyPropagation = new CopyPropagation(genKillSets);
        stmList.accept(copyPropagation);
        //
        copyPropagation.getStmList().accept(new PrettyPrinter(System.out));
        genKillSets.displayGenKill(System.out);

    }

    @Test
    public void copyPropagation() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ParserService parserService = new ParserService(new ParserFactory());
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let var a:= 3 var b:= a in a + b end", new ErrorMsg("", System.out));
        // need binder to bind types to expressions.
        program.accept(new Binder(errorMsg));
        program.accept(new TypeChecker(errorMsg));
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(translator);
        ProcFrag procFrag = (ProcFrag)translator.getFragList().head;
        // tree atomizer
        TreeAtomizer treeAtomizer = new TreeAtomizer(new CanonicalizationImpl());
        procFrag.body.accept(treeAtomizer);
        StmList stmList =  treeAtomizer.getCanonicalisedAtoms();
        // 
        GenKillSets genKillSets = new GenKillSets(new CFG(stmList));
        CopyPropagation copyPropagation = new CopyPropagation(genKillSets);
        stmList.accept(copyPropagation);
        //
        copyPropagation.getStmList().accept(new PrettyPrinter(System.out));
        genKillSets.displayGenKill(System.out);

    }
}