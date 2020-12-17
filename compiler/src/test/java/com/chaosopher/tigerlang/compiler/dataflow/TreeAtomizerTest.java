package com.chaosopher.tigerlang.compiler.dataflow;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.FileNotFoundException;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import com.chaosopher.tigerlang.compiler.canon.CanonVisitor;
import com.chaosopher.tigerlang.compiler.canon.Canonicalization;
import com.chaosopher.tigerlang.compiler.canon.CanonicalizationImpl;
import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.tree.BINOP;
import com.chaosopher.tigerlang.compiler.tree.CJUMP;
import com.chaosopher.tigerlang.compiler.tree.CONST;
import com.chaosopher.tigerlang.compiler.tree.LABEL;
import com.chaosopher.tigerlang.compiler.tree.MEM;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.tree.XmlPrinter;

import org.junit.Test;
public class TreeAtomizerTest {

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

        TempReplacer blah = new TempReplacer(treeAtomizer);
        treeAtomizer.getAtoms().accept(blah);
        assertNotNull(blah.getStmList());
//        blah.getStmList().accept(printer);
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
        XmlPrinter printer = new XmlPrinter(System.out);
        treeAtomizer.getAtoms().accept(printer);
        assertNotNull(treeAtomizer.getAtoms());
    }

    @Test
    public void moveMemMem() throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        TreeAtomizer treeAtomizer = new TreeAtomizer(new CanonicalizationImpl());
        MOVE mem = new MOVE(
            new MEM(
                new MEM(
                    new CONST(1)
                )
            ),
            new CONST(2)
        );
        mem.accept(treeAtomizer);
        XmlPrinter printer = new XmlPrinter(System.out);
        treeAtomizer.getAtoms().accept(printer);
        assertNotNull(treeAtomizer.getAtoms());
    }

    @Test
    public void jumpMem() throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        TreeAtomizer treeAtomizer = new TreeAtomizer(new CanonicalizationImpl());
        CJUMP cjump = new CJUMP(
            CJUMP.EQ,
            new MEM(
                new BINOP(
                    BINOP.AND,
                    new CONST(1),
                    new CONST(1)
                )
            ),
            new CONST(1),
            Label.create(),
            Label.create()
        );
        cjump.accept(treeAtomizer);
        XmlPrinter printer = new XmlPrinter(System.out);
        StmList stmlList = treeAtomizer.getAtoms();
        stmlList.accept(printer);
        assertNotNull(stmlList);
    }
}