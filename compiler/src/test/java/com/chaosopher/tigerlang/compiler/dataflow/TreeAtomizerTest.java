package com.chaosopher.tigerlang.compiler.dataflow;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import com.chaosopher.tigerlang.compiler.canon.CanonicalizationImpl;
import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.tree.BINOP;
import com.chaosopher.tigerlang.compiler.tree.CJUMP;
import com.chaosopher.tigerlang.compiler.tree.CONST;
import com.chaosopher.tigerlang.compiler.tree.MEM;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.XmlPrinter;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import junit.framework.AssertionFailedError;

public class TreeAtomizerTest {

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
        if(subtree(actual, test)) {
            System.out.println("Subtree is true, finished.");
            return true;
        } else {
            NodeList actualNodeList = actual.getChildNodes();
            for (int i = 0; i < actualNodeList.getLength(); i++) {
                if (contains(actualNodeList.item(i), test)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Test
    public void createInstance() throws FileNotFoundException {
        TreeAtomizer treeAtomizer = new TreeAtomizer(new CanonicalizationImpl());
        assertNotNull(treeAtomizer);
    }

    @Test
    public void binopBinop() throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        TreeAtomizer treeAtomizer = new TreeAtomizer(new CanonicalizationImpl());
        BINOP binop = new BINOP(
            BINOP.PLUS,
            new CONST(1),
            new BINOP(
                BINOP.PLUS,
                new CONST(2),
                new CONST(3)
            )
        );
        binop.accept(treeAtomizer);
        XmlPrinter printer = new XmlPrinter(System.out);
        treeAtomizer.getAtoms().accept(printer);
        assertNotNull(treeAtomizer.getAtoms());
        // expect the above exprssion to atomise to
        // an eseq containing the inner binop, which
        // returns a temp to the other binop.
        assertContains(
            treeAtomizer.getAtoms(), 
            "<binop op=\"2\">" +
            "<const value=\"1\" />" +
            "<eseq>" +
            "<move>" + 
            "<temp />" +
            "<binop op=\"2\">" +
            "<const value=\"2\" />" +
            "<const value=\"3\" />" +
            "</binop>" +
            "</move>" +
            "<temp />" +
            "</eseq>" +
            "</binop>"
        );
    }

    @Test
    public void memMem() throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        TreeAtomizer treeAtomizer = new TreeAtomizer(new CanonicalizationImpl());
        MEM mem = new MEM(
            new MEM(
                new CONST(1)
            )
        );
        mem.accept(treeAtomizer);
        // expect the inner mem to be moved into an eseq where it 
        // is moved into a temp, which is then returned to the outer
        // mem 
        assertContains(
            treeAtomizer.getAtoms(), 
            "<mem>" +
            "<eseq>" +
            "<move>" + 
            "<temp />" +
            "<mem>" +
            "<const value=\"1\" />" +
            "</mem>" +
            "</move>" +
            "<temp />" +
            "</eseq>" +
            "</mem>"
        );
    }

    @Test
    public void jumpMem() throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        TreeAtomizer treeAtomizer = new TreeAtomizer(new CanonicalizationImpl());
        CJUMP cjump = new CJUMP(
            CJUMP.EQ,
            new MEM(
                new CONST(1)
            ),
            new CONST(1),
            Label.create(),
            Label.create()
        );
        cjump.accept(treeAtomizer);
        // we expect the cjump left expression to be moved into an eseq that
        // moves its result into a temp, which is returned to the cjump.
        assertContains(
            treeAtomizer.getAtoms(), 
            "<cjump value=\"0\">" +
            "<eseq>" +
            "<move>" + 
            "<temp />" +
            "<mem>" +
            "<const value=\"1\" />" +
            "</mem>" +
            "</move>" +
            "<temp />" +
            "</eseq>" +
            "<const value=\"1\" />" +
            "<label />" +
            "<label />" +
            "</cjump>"
        );
    }
}