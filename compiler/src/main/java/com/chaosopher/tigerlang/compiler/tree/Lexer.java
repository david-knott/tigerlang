package com.chaosopher.tigerlang.compiler.tree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Simple Lexer for Tree language.
 * 
 */
public class Lexer {

    private static final HashMap<String, TokenType> hashMap = new HashMap<>();
    static {
        hashMap.put("seq", TokenType.SEQ);
        hashMap.put("eseq", TokenType.ESEQ);
        hashMap.put("sxp", TokenType.SXP);
        hashMap.put("move", TokenType.MOVE);
        hashMap.put("temp", TokenType.TEMP);
        hashMap.put("const", TokenType.CONST);
        hashMap.put("mem", TokenType.MEM);
        hashMap.put("jump", TokenType.JUMP);
        hashMap.put("cjump", TokenType.CJUMP);
        hashMap.put("label", TokenType.LABEL);
        hashMap.put("name", TokenType.NAME);
        hashMap.put("call", TokenType.CALL);
    }
	public static final int EOF = 129;
    private final BufferedReader bufferedReader;

    public Lexer(InputStream inputStream) {
        this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    }

    // peek is set to ' ' to prime the scanner.
    private char peek = ' ';

    void next() throws IOException {
        int read = bufferedReader.read();
        this.peek = read == -1 ? EOF : (char)read;
    }

    boolean isLetter(char c) {
        return (
            (c >= 'a' && c <= 'z')
            || (c >= 'A' && c <= 'Z')
        );
    }

    boolean isDigit(char c) {
        return (
            (c >= '0' && c <= '9')
        );
    }

    boolean isLetterOrDigit(char c) {
        return this.isLetter(c)
        || this.isDigit(c);
    }

    public Token scan() throws IOException {
        // note the next() at end of for
        // this means execute next after
        // first iteration before starting
        // next iteration. The peek variable
        // is primed to ' '. The block is entered
        // Character.isWhitespace returns true
        // and the loop continues, which calls
        // the update statment this.next()
        // this basically starts the scan.
        // if peek was empty we would pass through
        // the loop and none of the remaining
        // code blocks would be hit.
        for(;;this.next()) {
            if(Character.isWhitespace(peek)) continue;
            else break;
        }
        // find digits
        if(this.isDigit(this.peek)) {
            int y = 0;
            do {
               y = 10 * y + Character.digit(this.peek, 10);
               // advance to next character
               this.next();
            }while(this.isDigit(this.peek));
            return new Token(TokenType.DIGIT, Integer.toString(y));
        }
        // find identifier
        if(this.isLetter(this.peek)) {
            StringBuilder stringBuilder = new StringBuilder();
            do {
                stringBuilder.append(peek);
               // advance to next character
                this.next();
            }while(this.isLetterOrDigit(this.peek));
            String lexeme =  stringBuilder.toString();
            if(hashMap.containsKey(lexeme)) {
                return new Token(hashMap.get(lexeme));
            } else {
                return new Token(TokenType.ID, stringBuilder.toString());
            }
        }
        // other special characters.
        // note the this.next call here.
        // this is so we have moved to the next
        // character the next time scan is called.
        switch (peek) {
            case '(':
                this.next();
                return new Token(TokenType.LEFT_PAREN);
            case ')':
                this.next();
                return new Token(TokenType.RIGHT_PAREN);
            case ',':
                this.next();
                return new Token(TokenType.COMMA);
            case EOF:
                return new Token(TokenType.EOF);
        }
        throw new Error(String.format("Unknown lexical token :[" +  (int)peek + "]", peek));
    }
}