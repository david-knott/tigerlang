package com.chaosopher.tigerlang.compiler.tree;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class XmlPrinter implements TreeVisitor {

    private XMLStreamWriter xmlStreamWriter;

    public XmlPrinter(OutputStream arrayOutputStream) throws XMLStreamException, FactoryConfigurationError {
        this.xmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(arrayOutputStream);
        this.xmlStreamWriter.writeStartDocument();
        this.xmlStreamWriter.writeStartElement("tree");
    }
    
    public void end() throws XMLStreamException {
        this.xmlStreamWriter.writeEndElement();
        this.xmlStreamWriter.flush();
    }

    private void writeStartElement(String name) {
        try {
            this.xmlStreamWriter.writeStartElement(name);
        } catch (XMLStreamException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void writeEndElement() {
        try {
            this.xmlStreamWriter.writeEndElement();
        } catch (XMLStreamException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void writeAttribute(String string, int value) {
        try {
            this.xmlStreamWriter.writeAttribute(string, Integer.toString(value));
        } catch (XMLStreamException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void writeAttribute(String string, String string2) {
        try {
            this.xmlStreamWriter.writeAttribute(string, string2);
        } catch (XMLStreamException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

	@Override
    public void visit(BINOP op) {
        this.writeStartElement("binop");
        this.writeAttribute("op", op.binop);
        op.left.accept(this);
        op.right.accept(this);
        this.writeEndElement();
    }

    @Override
    public void visit(CALL op) {
        this.writeStartElement("call");
        op.func.accept(this);
        for(ExpList args = op.args; args != null; args = args.tail ) {
            args.head.accept(this);
        }
        this.writeEndElement();
    }

    @Override
    public void visit(CONST op) {
        this.writeStartElement("const");
        this.writeAttribute("value", op.value);
        this.writeEndElement();
    }

    @Override
    public void visit(ESEQ op) {
        this.writeStartElement("eseq");
        op.stm.accept(this);
        op.exp.accept(this);
        this.writeEndElement();
    }

    @Override
    public void visit(EXP op) {
        this.writeStartElement("sxp");
        op.exp.accept(this);
        this.writeEndElement();
    }

    @Override
    public void visit(JUMP op) {
        this.writeStartElement("jump");
        op.exp.accept(this);
        this.writeEndElement();
    }

    @Override
    public void visit(LABEL op) {
        this.writeStartElement("label");
        this.writeAttribute("value", op.label.toString());
        this.writeEndElement();
    }

    @Override
    public void visit(MEM op) {
        this.writeStartElement("mem");
        op.exp.accept(this);
        this.writeEndElement();
    }

    @Override
    public void visit(MOVE op) {
        this.writeStartElement("move");
        op.dst.accept(this);
        op.src.accept(this);
        this.writeEndElement();
    }

    @Override
    public void visit(NAME op) {
        this.writeStartElement("name");
        this.writeAttribute("value", op.label.toString());
        this.writeEndElement();
    }

    @Override
    public void visit(SEQ op) {
        this.writeStartElement("seq");
        op.left.accept(this);
        op.right.accept(this);
        this.writeEndElement();
    }

    @Override
    public void visit(TEMP op) {
        this.writeStartElement("temp");
     //   this.writeAttribute("value", op.toString());
        this.writeEndElement();
    }

    @Override
    public void visit(CJUMP cjump) {
        this.writeStartElement("cjump");
        this.writeAttribute("value", Integer.toString(cjump.relop));
        cjump.left.accept(this);
        cjump.right.accept(this);
        this.writeEndElement();
    }
}