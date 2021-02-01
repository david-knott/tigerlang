package com.chaosopher.tigerlang.compiler.dataflow;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.JUMP;
import com.chaosopher.tigerlang.compiler.tree.LABEL;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.tree.TEMP;



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
import com.chaosopher.tigerlang.compiler.main.Main;
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

public class GenKillTest {

    @Test
    public void simpleBlock() {
        Temp a = Temp.create();
        Temp b = Temp.create();
        Temp c = Temp.create();
        Temp d = Temp.create();
        StmList test = new StmList(
            new LABEL(
                Label.create()
            ),
            new StmList(
                new MOVE(
                    new TEMP(d),
                    new TEMP(c)
                ),
                new StmList(
                    new MOVE(
                        new TEMP(c),
                        new TEMP(b)
                    ),
                    new StmList(
                        new MOVE(
                            new TEMP(b),
                            new TEMP(a))
                    )
                )
            )
        );
        CFG cfg = new CFG(test);
        GenKillSets genKillSets = new  GenKillSets(cfg);


        genKillSets.generate();
        cfg.show(System.out);
        // expect one block, with 3 statements.
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
        // expect one block, with 3 statements.
    }




}