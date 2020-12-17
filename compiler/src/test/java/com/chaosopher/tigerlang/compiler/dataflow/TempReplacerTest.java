package com.chaosopher.tigerlang.compiler.dataflow;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.FileNotFoundException;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import com.chaosopher.tigerlang.compiler.canon.CanonicalizationImpl;
import com.chaosopher.tigerlang.compiler.tree.BINOP;
import com.chaosopher.tigerlang.compiler.tree.CONST;
import com.chaosopher.tigerlang.compiler.tree.XmlPrinter;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;



public class TempReplacerTest {
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
        for (int i = 0; i < actualNodeList.getLength(); i++) {
            if (subtree(actualNodeList.item(i), test)) {
                System.out.println("Subtree is true, finished.");
                return true;
            } else {
                System.out.println("No match, next node:" + actualNodeList.item(i).getNodeName());
                if (contains(actualNodeList.item(i), test))
                    return true;
            }
        }
        return false;
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
       // treeAtomizer.getAtoms().accept(printer);
        assertNotNull(treeAtomizer.getAtoms());

        TempReplacer blah = new TempReplacer(treeAtomizer);
        treeAtomizer.getAtoms().accept(blah);
        assertNotNull(blah.getStmList());
        blah.getStmList().accept(printer);
    }
}