package com.chaosopher.tigerlang.compiler.tree;

import java.io.IOException;

class Parser {

    private final Lexer lexer;
    private Token look;

    Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    void move() throws IOException {
        this.look = this.lexer.scan();
    }

    void match(TokenType t) throws IOException {
        if(look.getTokenType() == t) {
            move();
        } else {
            throw new Error();
        }
    }
}