package com.chaosopher.tigerlang.compiler.tree;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class LexerTest {

    @Test
    public void createInstance() {
        String code = "MEM(t1)";
        Lexer lexer = new Lexer(new ByteArrayInputStream(code.getBytes()));
        Assert.assertNotNull(lexer);
    }

    @Test
    public void digit() throws Exception {
        String code = "123";
        Lexer lexer = new Lexer(new ByteArrayInputStream(code.getBytes()));
        Token token;
        token = lexer.scan();
        Assert.assertEquals(TokenType.DIGIT, token.getTokenType());
        Assert.assertEquals("123", token.getLexeme());
    }

    @Test
    public void identifier() throws Exception {
        String code = "MOVE123";
        Lexer lexer = new Lexer(new ByteArrayInputStream(code.getBytes()));
        Token token;
        token = lexer.scan();
        Assert.assertEquals(TokenType.ID, token.getTokenType());
        Assert.assertEquals("MOVE123", token.getLexeme());
    }

    @Test
    public void eof() throws Exception {
        String code = "MOVE123";
        Lexer lexer = new Lexer(new ByteArrayInputStream(code.getBytes()));
        Token token;
        lexer.scan();
        token = lexer.scan();
        Assert.assertEquals(TokenType.EOF, token.getTokenType());
    }

    @Test(expected = java.lang.Error.class)
    public void unknownLexicalTokenStart() throws Exception {
        String code = "!MOVE123";
        Lexer lexer = new Lexer(new ByteArrayInputStream(code.getBytes()));
        Token token;
        token = lexer.scan();
        Assert.assertEquals(TokenType.EOF, token.getTokenType());
    }

    @Test(expected = java.lang.Error.class)
    public void unknownLexicalTokenEnd() throws Exception {
        String code = "MOVE123;!";
        Lexer lexer = new Lexer(new ByteArrayInputStream(code.getBytes()));
        Token token;
        token = lexer.scan(); //return move123
        token = lexer.scan();
        Assert.assertEquals(TokenType.EOF, token.getTokenType());
    }

    @Test
    public void stream() throws Exception {
        String code = "move(temp(t2), temp(t3))";
        Lexer lexer = new Lexer(new ByteArrayInputStream(code.getBytes()));
        Token token;
        token = lexer.scan();
        Assert.assertEquals(TokenType.MOVE, token.getTokenType());

        token = lexer.scan();
        Assert.assertEquals(TokenType.LEFT_PAREN, token.getTokenType());

        token = lexer.scan();
        Assert.assertEquals(TokenType.TEMP, token.getTokenType());

        token = lexer.scan();
        Assert.assertEquals(TokenType.LEFT_PAREN, token.getTokenType());

        token = lexer.scan();
        Assert.assertEquals(TokenType.ID, token.getTokenType());
        Assert.assertEquals("t2", token.getLexeme());

        token = lexer.scan();
        Assert.assertEquals(TokenType.RIGHT_PAREN, token.getTokenType());

        token = lexer.scan();
        Assert.assertEquals(TokenType.COMMA, token.getTokenType());

        token = lexer.scan();
        Assert.assertEquals(TokenType.TEMP, token.getTokenType());

        token = lexer.scan();
        Assert.assertEquals(TokenType.LEFT_PAREN, token.getTokenType());

        token = lexer.scan();
        Assert.assertEquals(TokenType.ID, token.getTokenType());
        Assert.assertEquals("t3", token.getLexeme());

        token = lexer.scan();
        Assert.assertEquals(TokenType.RIGHT_PAREN, token.getTokenType());

        token = lexer.scan();
        Assert.assertEquals(TokenType.RIGHT_PAREN, token.getTokenType());

        token = lexer.scan();
        Assert.assertEquals(TokenType.EOF, token.getTokenType());

        token = lexer.scan();
        Assert.assertEquals(TokenType.EOF, token.getTokenType());



    }
}