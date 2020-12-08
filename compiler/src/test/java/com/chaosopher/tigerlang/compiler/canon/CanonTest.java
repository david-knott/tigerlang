package com.chaosopher.tigerlang.compiler.bind;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.chaosopher.tigerlang.compiler.translate.Access;
import Frame.Frame;
import com.chaosopher.tigerlang.compiler.intel.IntelFrame;
import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.translate.Exp;
import com.chaosopher.tigerlang.compiler.translate.FragList;
import com.chaosopher.tigerlang.compiler.translate.FragmentPrinter;
import com.chaosopher.tigerlang.compiler.translate.Level;
import com.chaosopher.tigerlang.compiler.translate.Translator;
import com.chaosopher.tigerlang.compiler.translate.TranslatorVisitor;
import com.chaosopher.tigerlang.compiler.tree.TEMP;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.bind.Binder;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import FindEscape.EscapeVisitor;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;
import com.chaosopher.tigerlang.compiler.tree.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.XmlPrinter;
import junit.framework.AssertionFailedError;


public class CanonTest {

    private ParserService parserService;
    private CanonVisitor canonVisitor;

    public CanonTest() {
        parserService = new ParserService(new ParserFactory());
    }

    @Before
    public void setup() {
        this.canonVisitor = new CanonVisitor(new CanonicalizationImpl());
    }

    @Test
    public void liftEseqs() {
        TranslatorVisitor translator = new TranslatorVisitor();
        assertNotNull(translator);
        Absyn program = parserService.parse("3 + 5", new ErrorMsg("", System.out));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        assertNotNull(fragList);
        fragList.accept(this.canonVisitor);
        this.canonVisitor.fragList.accept(new FragmentPrinter(System.out));
        assertTrue(false);
    }

    @Test
    public void liftSeqs() {
        TranslatorVisitor translator = new TranslatorVisitor();
        assertNotNull(translator);
        Absyn program = parserService.parse("3 + 5", new ErrorMsg("", System.out));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        assertNotNull(fragList);
        fragList.accept(this.canonVisitor);
        this.canonVisitor.fragList.accept(new FragmentPrinter(System.out));
        assertTrue(false);
    }

    @Test
    public void extractCalls() {
        TranslatorVisitor translator = new TranslatorVisitor();
        assertNotNull(translator);
        Absyn program = parserService.parse("3 + 5", new ErrorMsg("", System.out));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        assertNotNull(fragList);
        fragList.accept(this.canonVisitor);
        this.canonVisitor.fragList.accept(new FragmentPrinter(System.out));
        assertTrue(false);
    }
}
