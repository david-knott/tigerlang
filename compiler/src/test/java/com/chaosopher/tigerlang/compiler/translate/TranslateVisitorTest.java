package com.chaosopher.tigerlang.compiler.translate;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.bind.Binder;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.findescape.EscapeVisitor;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.XmlPrinter;
import com.chaosopher.tigerlang.compiler.types.TypeChecker;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import junit.framework.AssertionFailedError;

public class TranslateVisitorTest {

    @FunctionalInterface
    interface QueryTreeTest {
        public void verify(NodeList nodeList);
    }

    private void assertContains(Stm translation, String string) {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        XmlPrinter xmlPrinter;
        try {
            xmlPrinter = new XmlPrinter(arrayOutputStream);
            translation.accept(xmlPrinter);
            assertTrue(contains(new String(arrayOutputStream.toByteArray()), string));
        } catch (XMLStreamException e) {
            throw new AssertionFailedError();
        } catch (FactoryConfigurationError e) {
            throw new AssertionFailedError();
        }
    }

    private static boolean contains(String actual, String test) {
        System.out.println(actual);
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document actualDocument = builder.parse(new InputSource(new StringReader(actual)));
            Document testDocument = builder.parse(new InputSource(new StringReader(test)));
            return contains(actualDocument.getDocumentElement(), testDocument.getDocumentElement());
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean subtree(Node actual, Node test) {
        NodeList actualNodeList = actual.getChildNodes();
        NodeList testNodeList = test.getChildNodes();
        if (actual.getNodeName().equals(test.getNodeName())) {
            if (testNodeList.getLength() == actualNodeList.getLength()) {
                System.out.println(">> Potential Match:" + actual.getNodeName());
                boolean res = true;
                for (int i = 0; i < testNodeList.getLength(); i++) {
                    if (!subtree(actualNodeList.item(i), testNodeList.item(i))) {
                        res |= false;
                    }
                }
                return res;
            } else {
                System.out.println(">> Wrong argument count:" + actual.getNodeName());
            }
        }
        return false;
    }

    private static boolean contains(Node actual, Node test) {
        NodeList actualNodeList = actual.getChildNodes();
        for(int i = 0; i < actualNodeList.getLength(); i++) {
            if(subtree(actualNodeList.item(i), test)) {
                System.out.println("Subtree is true, finished.");
                return true;
            } else {
                System.out.println("No match, next node:" + actualNodeList.item(i).getNodeName());
                if(contains(actualNodeList.item(i), test)) 
                    return true;
            }
        }
        return false;
    }

    private ParserService parserService;

    public TranslateVisitorTest() {
        parserService = new ParserService(new ParserFactory());
    }

    @Test
    public void canCreateInstance() {
        TranslatorVisitor translator = new TranslatorVisitor();
        assertNotNull(translator);
    }

    @Test
    public void translateEmpty() {
        TranslatorVisitor translator = new TranslatorVisitor();
        assertNotNull(translator);
        Absyn program = parserService.parse("/* just a comment */", new ErrorMsg("", System.out));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        assertNull(fragList);
    }

    @Test
    public void translateInt() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("3", errorMsg);
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        assertNotNull(fragList);
    }

    @Test
    public void translateAdd() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        assertNotNull(translator);
        Absyn program = parserService.parse("3 + 5", new ErrorMsg("", System.out));
        // need binder to bind types to expressions.
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        assertNotNull(fragList);
        ProcFrag procFrag = (ProcFrag)fragList.head;
        assertContains(procFrag.body, 
            "<binop op=\"0\">" +
            "<const value=\"3\" />" +
            "<const value=\"5\" />" +
            "</binop>"
        );
    }

    @Test
    public void translateEscapingVarDec() {
        TranslatorVisitor translator = new TranslatorVisitor();
        assertNotNull(translator);
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("var a:int := 3", errorMsg);
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        assertNotNull(fragList);
        fragList.accept(new FragmentPrinter(System.out));
        ProcFrag procFrag = (ProcFrag)fragList.head;
        assertContains(procFrag.body, 
            "<move>" +
            "<mem>" +
            "<binop op=\"0\">" +
            "<temp />" +
            "<const value=\"-8\" />" +
            "</binop>" +
            "</mem>" +
            "<const value=\"3\" />" +
            "</move>"
        );
    }

    @Test
    public void translateNonEscapingVarDec() {
        TranslatorVisitor translator = new TranslatorVisitor();
        assertNotNull(translator);
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("var a:int := 3", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        assertNotNull(fragList);
        fragList.accept(new FragmentPrinter(System.out));
        ProcFrag procFrag = (ProcFrag)fragList.head;
        assertContains(procFrag.body, 
            "<move>" +
            "<temp />" +
            "<const value=\"3\" />" +
            "</move>"
        );
    }

    @Test
    public void translateNonEscapingVarDecUsage() {
        TranslatorVisitor translator = new TranslatorVisitor();
        assertNotNull(translator);
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("var a:int := 3 var b:int := a", errorMsg);
    //    Absyn program = parserService.parse("var a:int := 3", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        assertNotNull(fragList);
        ProcFrag procFrag = (ProcFrag)fragList.head;
        assertContains(procFrag.body, 
            "<seq>" +
            "<move>" +
            "<temp />" +
            "<const value=\"3\" />" +
            "</move>" +
            "<move>" +
            "<temp />" +
            "<temp />" +
            "</move>" +
            "</seq>"
        );
    }
    
    @Test
    public void primitiveUsage() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("printi(3)", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        ProcFrag procFrag = (ProcFrag)fragList.head;
        assertContains(procFrag.body, 
            "<sxp>" +
            "<call>" +
            "<name value=\"printi\" />" +
            "<const value=\"3\" />" +
            "</call>" +
            "</sxp>"
        );
    }

    @Test
    public void arrayDecTest() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let type intArray = array of int var a := intArray[3] of 5  in end", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
        ProcFrag procFrag = (ProcFrag)fragList.head;
        // Expect a call to initArray with const(3), for size and const(5) for init, with returned temp
        // ESEQ(MOVE(temp, call(initArray)), temp)
        assertContains(procFrag.body, 
            "<eseq>" +
            "<move>" +
            "<temp />" +
            "<call>" +
            "<name value=\"initArray\" />" +
            "<const value=\"3\" />" +
            "<const value=\"5\" />" +
            "</call>" +
            "</move>" +
            "<temp />" +
            "</eseq>"
        );
    }
     
    @Test
    public void arraySubscriptTest() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let type intArray = array of int var a := intArray[13] of 5  in a[7] end", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
        ProcFrag procFrag = (ProcFrag)fragList.head;
        // expect a MEM( BINOP(PLUS, base, BINOP(MULT, index, wordSize ))
        assertContains(procFrag.body, 
            "<mem>" +
            "<binop op=\"0\">" +
            "<temp />" +
            "<binop op=\"2\">" +
            "<const value=\"7\" />" +
            "<const value=\"8\" />" +
            "</binop>" +
            "</binop>" +
            "</mem>"
        );
    }


     
    @Test
    public void recordExpZeroFieldTest() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let type rec = { } var v := rec {  } in end", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
        ProcFrag procFrag = (ProcFrag)fragList.head;
        /**
         * Expect a type declaration to generate a noop stmt ( sxp(const(0))
         * Followed by a nil variable assignment ( move(tmp, 0))
         */
        assertContains(procFrag.body, 
            "<seq>" +
            "<sxp>" +
            "<const value=\"0\" />" +
            "</sxp>" +
            "<move>" +
            "<temp />" +
            "<const value=\"0\" />" +
            "</move>" +
            "</seq>"
        );
    }

    @Test
    public void recordExpOneFieldTest() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let type rec = { a : int } var v := rec { a = 3 } in end", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
        ProcFrag procFrag = (ProcFrag)fragList.head;
        // call initRecord with size 8 and move result into temp
        assertContains(procFrag.body, 
            "<move>" +
            "<temp />" +
            "<call>" +
            "<name value=\"initRecord\" />" +
            "<const value=\"8\" />" +
            "</call>" +
            "</move>"
        );
        // move 3 into record pointer temp + 0
        assertContains(procFrag.body, 
            "<move>" +
            "<mem>" +
            "<binop op=\"0\">" +
            "<temp />" +
            "<const value=\"0\" />" +
            "</binop>" +
            "</mem>" +
            "<const value=\"3\" />" +
            "</move>"
        );
    }

    @Test
    public void recordExpTwoFieldsTest() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let type rec = { a : int, b : int } var v := rec { a = 3, b = 5} in end", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        ProcFrag procFrag = (ProcFrag)fragList.head;
        // expect 1 fragment, with 1 SEQ node, 
        // with left containing MOVE with offset 0 from record pointe.
        // right containing MOVE with offset 8 from record pointer.
        // call initRecord with size 16 and move result into temp
        /*
        assertContains(procFrag.body, 
            "<move>" +
            "<temp />" +
            "<call>" +
            "<name value=\"initRecord\" />" +
            "<const value=\"16\" />" +
            "</call>" +
            "</move>"
        );*/
        // move 3 into record pointer temp + 0
        // move 5 into record pointer temp + 8
        assertContains(procFrag.body, 
            "<seq>" +
            "<move>" +
            "<mem>" +
            "<binop op=\"0\">" +
            "<temp />" +
            "<const value=\"0\" />" +
            "</binop>" +
            "</mem>" +
            "<const value=\"3\" />" +
            "</move>" +
            "<move>" +
            "<mem>" +
            "<binop op=\"0\">" +
            "<temp />" +
            "<const value=\"8\" />" +
            "</binop>" +
            "</mem>" +
            "<const value=\"5\" />" +
            "</move>" +
            "</seq>"
        );
    }

    @Test
    public void recordExpThreeFieldsTest() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let type rec = { a : int, b : int, c: int } var v := rec { a = 3, b = 5, c = 7} in end", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
        ProcFrag procFrag = (ProcFrag)fragList.head;
        // call initRecord with size 24 and move result into temp
        assertContains(procFrag.body, 
            "<move>" +
            "<temp />" +
            "<call>" +
            "<name value=\"initRecord\" />" +
            "<const value=\"24\" />" +
            "</call>" +
            "</move>"
        );
        // move 3 into record pointer temp + 0
        // move 5 into record pointer temp + 8
        // move 7 into record pointer temp + 16
        
        assertContains(procFrag.body, 
            "<seq>" +
            "<move>" +
            "<mem>" +
            "<binop op=\"0\">" +
            "<temp />" +
            "<const value=\"0\" />" +
            "</binop>" +
            "</mem>" +
            "<const value=\"3\" />" +
            "</move>" +
            "<seq>" +
            "<move>" +
            "<mem>" +
            "<binop op=\"0\">" +
            "<temp />" +
            "<const value=\"8\" />" +
            "</binop>" +
            "</mem>" +
            "<const value=\"5\" />" +
            "</move>" +
            "<move>" +
            "<mem>" +
            "<binop op=\"0\">" +
            "<temp />" +
            "<const value=\"16\" />" +
            "</binop>" +
            "</mem>" +
            "<const value=\"7\" />" +
            "</move>" +
            "</seq>" +
            "</seq>"
        );
    }

    @Test
    public void expSeq() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let in 3 end", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
        ProcFrag procFrag = (ProcFrag)fragList.head;
        assertContains(procFrag.body, 
            "<eseq>" +
            "<sxp>" +
            "<const value=\"0\" />" +
            "</sxp>" +
            "<const value=\"3\" />" +
            "</eseq>"
        );
    }

    @Test
    public void expExpSeq() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let in 3; 5 end", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
        ProcFrag procFrag = (ProcFrag)fragList.head;
        assertContains(procFrag.body, 
            "<eseq>" +
            "<sxp>" +
            "<const value=\"3\" />" +
            "</sxp>" +
            "<const value=\"5\" />" +
            "</eseq>"
        );
    }

    @Test
    public void expExpExpSeq() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let in 3; 5; 7 end", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
        ProcFrag procFrag = (ProcFrag)fragList.head;
        assertContains(procFrag.body, 
            "<eseq>" +
            "<seq>" +
            "<sxp>" +
            "<const value=\"3\" />" +
            "</sxp>" +
            "<sxp>" +
            "<const value=\"5\" />" +
            "</sxp>" +
            "</seq>" +
            "<const value=\"7\" />" +
            "</eseq>"
        );
    }

    @Test
    public void stmSeq() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let in printi(3)end", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
        ProcFrag procFrag = (ProcFrag)fragList.head;
        // expect sxp(call(print, 3))
        assertContains(procFrag.body, 
            "<sxp>" +
            "<call>" +
            "<name value=\"printi\" />" +
            "<const value=\"3\" />" +
            "</call>" +
            "</sxp>"
        );
    }

    @Test
    public void stmSmtSeq() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let in printi(3); printi(5) end", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
        ProcFrag procFrag = (ProcFrag)fragList.head;
        // expect seq(sxp(call(print, 3)), seq(sxp(call(print, 5),
        assertContains(procFrag.body, 
            "<seq>" +
            "<sxp>" +
            "<call>" +
            "<name value=\"printi\" />" +
            "<const value=\"3\" />" +
            "</call>" +
            "</sxp>" +
            "<sxp>" +
            "<call>" +
            "<name value=\"printi\" />" +
            "<const value=\"5\" />" +
            "</call>" +
            "</sxp>" +
            "</seq>"
        );
    }

    @Test
    public void stmStmStmSeq() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let in printi(3); printi(5); printi(7) end", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
        ProcFrag procFrag = (ProcFrag)fragList.head;
        // expect seq(sxp(call(print, 3)), seq(sxp(call(print, 5)), sxp(call(print, 7))))
        assertContains(procFrag.body, 
            "<seq>" +
            "<seq>" +
            "<sxp>" +
            "<call>" +
            "<name value=\"printi\" />" +
            "<const value=\"3\" />" +
            "</call>" +
            "</sxp>" +
            "<sxp>" +
            "<call>" +
            "<name value=\"printi\" />" +
            "<const value=\"5\" />" +
            "</call>" +
            "</sxp>" +
            "</seq>" +
            "<sxp>" +
            "<call>" +
            "<name value=\"printi\" />" +
            "<const value=\"7\" />" +
            "</call>" +
            "</sxp>" +
            "</seq>"
        );
    }


    @Test
    public void sequenceOfTwo() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let in (3; 7) end", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
       // ProcFrag procFrag = (ProcFrag)fragList.head;
    }

    @Test
    public void sequenceOfThree() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let in (3; 7; 11) end", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
       // ProcFrag procFrag = (ProcFrag)fragList.head;
    }

    @Test
    public void sequenceOfFour() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let in (3; 7; 11; 13) end", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
       // ProcFrag procFrag = (ProcFrag)fragList.head;
    }



    @Test
    public void recordExpThreeUsageFirstField() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let type rec = { a : int, b : int, c: int } var v := rec { a = 3, b = 5, c = 7} in v.a end", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
        ProcFrag procFrag = (ProcFrag)fragList.head;
    }

    @Test
    public void letNoDecNoBody() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let in end", errorMsg);
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
        // expect SEQ with
        // expect noop declist as empty ( sxp(const(0)) )
        // expect noop body as empty ( sxp(const(0)))
        ProcFrag procFrag = (ProcFrag)fragList.head;
        assertContains(procFrag.body, 
            "<seq>" +
            "<sxp>" +
            "<const value=\"0\" />" +
            "</sxp>" +
            "<sxp>" +
            "<const value=\"0\" />" +
            "</sxp>" +
            "</seq>"
        );
    }
     
    @Test
    public void letNoDecIntBody() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let in 3 end", errorMsg);
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
        // expect a ESEQ with
        // expect noop declist as empty ( sxp(const(0)) )
        // expect const(3)
        ProcFrag procFrag = (ProcFrag)fragList.head;
        assertContains(procFrag.body, 
            "<eseq>" +
            "<sxp>" +
            "<const value=\"0\" />" +
            "</sxp>" +
            "<const value=\"3\" />" +
            "</eseq>"
        );
    }
     
    @Test
    public void letNoEscapeDecNoBody() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let var a:int := 3 in () end", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
        // expect a SEQ with
        // expect vardec (MOVE(3, temp))
        // expect noop body as empty ( sxp(const(0)))
        ProcFrag procFrag = (ProcFrag)fragList.head;
        assertContains(procFrag.body, 
            "<seq>" +
            "<move>" +
            "<temp />" +
            "<const value=\"3\" />" +
            "</move>" +
            "<sxp>" +
            "<const value=\"0\" />" +
            "</sxp>" +
            "</seq>"
        );
    }
     
    @Test
    public void ifThe() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("if 3 = 3 then 5", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
        ProcFrag procFrag = (ProcFrag)fragList.head;
        assertContains(procFrag.body, 
            "<cjump value=\"0\">" +
            "<const value=\"3\" />" +
            "<const value=\"3\" />" +
            "<label value=\"L0\" />" +
            "<label value=\"L1\" />" +
            "</cjump>"
        );
    }

    @Test
    public void ifThenElse() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("if 3 = 3 then 5 else 7", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        ProcFrag procFrag = (ProcFrag)fragList.head;
        assertContains(procFrag.body, 
            "<cjump value=\"0\">" +
            "<const value=\"3\" />" +
            "<const value=\"3\" />" +
            "<label value=\"L0\" />" +
            "<label value=\"L1\" />" +
            "</cjump>"
        );
    }

    @Test
    public void functionCallOneArgument() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let function fa(a: int): int = 5 + a in printi(fa(7)) end", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
        // expect let to return void
        // expect printi to return void
        // expect fa(7) to return int
    }

    @Test
    public void forTest() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("for a := 3 to 13 do ()", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
    }

    @Test
    public void whileTest() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("while -2 > 3  do ()", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
    }

    @Test
    public void whileBugTest() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let var i:int := 1 function printb() = ( while 1 do ( i := i + 5 ) ) in ( printb() ) end", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        TypeChecker.create(program, errorMsg);
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
    }
}