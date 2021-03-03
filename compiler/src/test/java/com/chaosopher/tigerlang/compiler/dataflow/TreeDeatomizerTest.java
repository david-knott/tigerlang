package com.chaosopher.tigerlang.compiler.dataflow;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import com.chaosopher.tigerlang.compiler.canon.CanonicalizationImpl;
import com.chaosopher.tigerlang.compiler.tree.Parser;
import com.chaosopher.tigerlang.compiler.tree.Lexer;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.BINOP;
import com.chaosopher.tigerlang.compiler.tree.CONST;
import com.chaosopher.tigerlang.compiler.tree.IR;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.tree.TEMP;
import com.chaosopher.tigerlang.compiler.tree.XmlPrinter;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TreeDeatomizerTest {

    private IR buildTree(String code) throws IOException {
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        return parser.parse();
    }
    
    @Test
    public void move() throws IOException {
        buildTree(
           "move(temp(t1), binop(PLUS, temp(t2), temp(t3)))" 
          //  "move(temp(t4), binop(PLUS, temp(t1), temp(t4)))"
        );
    }
}
