package com.chaosopher.tigerlang.compiler.dataflow;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.bind.Binder;
import com.chaosopher.tigerlang.compiler.canon.CanonVisitor;
import com.chaosopher.tigerlang.compiler.canon.CanonicalizationImpl;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.findescape.EscapeVisitor;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;
import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.translate.DataFrag;
import com.chaosopher.tigerlang.compiler.translate.FragmentVisitor;
import com.chaosopher.tigerlang.compiler.translate.ProcFrag;
import com.chaosopher.tigerlang.compiler.translate.TranslatorVisitor;
import com.chaosopher.tigerlang.compiler.tree.JUMP;
import com.chaosopher.tigerlang.compiler.tree.LABEL;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.tree.TEMP;
import com.chaosopher.tigerlang.compiler.types.TypeChecker;

import org.junit.Test;

public class GenKillTest {

    @Test
    public void inlineBlock() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ParserService parserService = new ParserService(new ParserFactory());
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let var a:int := 1 in a := a + 1 end", new ErrorMsg("", System.out));
        // need binder to bind types to expressions.
        program.accept(new Binder(errorMsg));
        program.accept(new TypeChecker(errorMsg));
        program.accept(translator);
        TreeAtomizer treeAtomizer = new TreeAtomizer(new CanonicalizationImpl());
        translator.getFragList().accept(new FragmentVisitor(){
			@Override
			public void visit(ProcFrag procFrag) {
                procFrag.body.accept(treeAtomizer);
                procFrag.body = treeAtomizer.getCanonicalisedAtoms();
			}
			@Override
			public void visit(DataFrag dataFrag) {
				// TODO Auto-generated method stub
			}
            
        });
        StmList stmList = (StmList)((ProcFrag)translator.getFragList().head).body;
        CFG cfg = new CFG(stmList);
        GenKillSets genKillSets = new  GenKillSets(cfg);
        genKillSets.displayGenKill(System.out);
    }


    @Test
    public void simpleLoopBlock() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ParserService parserService = new ParserService(new ParserFactory());
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let var a:int := 5 var c:int := 1 in (while c <= a do (c := c + c ); a := c - a; c := 0) end", new ErrorMsg("", System.out));
        // need binder to bind types to expressions.
        program.accept(new Binder(errorMsg));
        program.accept(new TypeChecker(errorMsg));
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(translator);
        CanonVisitor canonVisitor = new CanonVisitor(new CanonicalizationImpl());
        translator.getFragList().accept(canonVisitor);
        StmList stmList = (StmList)((ProcFrag)canonVisitor.fragList.head).body;
        CFG cfg = new CFG(stmList);
        GenKillSets genKillSets = new  GenKillSets(cfg);
        genKillSets.displayGenKill(System.out);
    }

    @Test
    public void simpleLoop() {
        Temp a = Temp.create();
        Temp b = Temp.create();
        Temp c = Temp.create();
        Label label = Label.create();
        StmList test = new StmList(
            new LABEL(
                label
            ),
            new StmList(
                new MOVE(
                    new TEMP(a),
                    new TEMP(b)
                ),
                new StmList(
                    new MOVE(
                        new TEMP(c),
                        new TEMP(a)
                    ),
                    new StmList(
                        new JUMP(label)
                    )
                )
            )
        );
        CFG cfg = new CFG(test);
        cfg.show(System.out);
    }
}