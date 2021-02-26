package com.chaosopher.tigerlang.compiler.tree;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void createInstance() {
        String code = "mem(temp(t1))";
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        Assert.assertNotNull(parser);
    }

    @Test
    public void parseMoveTemp() throws IOException {
        String code = "move(temp(t1), temp(t2))";
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        IR ir = parser.parse();
    }

    @Test
    public void parseMoveConst() throws IOException {
        String code = "move(temp(t1), const(10))";
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        IR ir = parser.parse();
    }

    @Test
    public void parseMoveMem() throws IOException {
        String code = "move(temp(t1), mem(temp(t2)))";
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        IR ir = parser.parse();
    }

    @Test
    public void parseSequence() throws IOException {
        String code = "seq(move(temp(t1), mem(temp(t2))), sxp(temp(t1)))";
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        IR ir = parser.parse();
    }


    @Test
    public void parseESequence() throws IOException {
        String code = "move(temp(t3), eseq(move(temp(t1), mem(temp(t2))), temp(t1)))";
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        IR ir = parser.parse();
    }

    @Test
    public void parseJump() throws IOException {
        String code = "jump(name(l234))";
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        IR ir = parser.parse();
    }


    @Test
    public void parseEmptyCall() throws IOException {
        String code = "sxp(call(name(l1)))";
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        IR ir = parser.parse();
    }

    @Test
    public void parseOneArgCall() throws IOException {
        String code = "sxp(call(name(l1), temp(t1)))";
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        IR ir = parser.parse();
    }

    @Test
    public void parseTwoArgCall() throws IOException {
        String code = "sxp(call(name(l1), temp(t1), temp(t2)))";
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        IR ir = parser.parse();
    }

    @Test
    public void parseThreeArgCall() throws IOException {
        String code = "sxp(call(name(l1), temp(t1), temp(t2), temp(t3)))";
        Parser parser = new Parser(new Lexer(new ByteArrayInputStream(code.getBytes())));
        IR ir = parser.parse();
    }

}
