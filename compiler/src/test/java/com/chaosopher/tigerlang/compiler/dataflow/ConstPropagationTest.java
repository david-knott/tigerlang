package com.chaosopher.tigerlang.compiler.dataflow;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.bind.Binder;
import com.chaosopher.tigerlang.compiler.canon.CanonVisitor;
import com.chaosopher.tigerlang.compiler.canon.CanonicalizationImpl;
import com.chaosopher.tigerlang.compiler.e2e.RegressionTestBase;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.findescape.EscapeVisitor;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;
import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.translate.TranslatorVisitor;
import com.chaosopher.tigerlang.compiler.tree.BINOP;
import com.chaosopher.tigerlang.compiler.tree.CONST;
import com.chaosopher.tigerlang.compiler.tree.LABEL;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.tree.TEMP;
import com.chaosopher.tigerlang.compiler.types.TypeChecker;
import com.chaosopher.tigerlang.compiler.translate.ProcFrag;

import org.junit.Test;

public class ConstPropagationTest {

    @Test
    public void propagates() {
        StmList stmList = new StmList(
            new LABEL(Label.create("start")),
            new StmList(
                new MOVE(
                    new TEMP(Temp.create("a")), 
                    new CONST(3)
                ),
                new StmList(
                    new MOVE(
                        new TEMP(Temp.create("b")), 
                        new CONST(0)
                    ),
                    new StmList(
                        new MOVE(
                            new TEMP(Temp.create("c")), 
                            new BINOP(0, new TEMP(Temp.create("a")), new TEMP(Temp.create("b")))
                        )
                    )
                )
            )
        );
        CFG cfg = new CFG(stmList);
        GenKillSets genKillSets = new GenKillSets(cfg);
        // move into constructor
        genKillSets.generate();
//        CycleDetector cd = new CycleDetector(cfg);


        ConstPropagation constPropagation = new ConstPropagation(genKillSets);
        stmList.accept(constPropagation);
        new GenKillSetsXmlSerializer(genKillSets).serialize(System.out);
        constPropagation.getStmList().accept(new PrettyPrinter(System.out));
    }

    @Test
    public void constProp2() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ParserService parserService = new ParserService(new ParserFactory());
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let var a:= 3 var b:= 4 in a + b end", new ErrorMsg("", System.out));
        // need binder to bind types to expressions.
        program.accept(new Binder(errorMsg));
        program.accept(new TypeChecker(errorMsg));
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(translator);
        TreeAtomizer treeAtomizer = new TreeAtomizer(new CanonicalizationImpl());
    }
}