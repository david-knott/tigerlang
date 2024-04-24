package com.chaosopher.tigerlang.compiler.tree;
class Token {

    private final String lexeme;
    private final TokenType tokenType;

    public Token(TokenType tokenType) {
        this(tokenType, null);
    }

    public Token(TokenType tokenType, String lexeme) {
        this.lexeme = lexeme;
        this.tokenType = tokenType;
    }

    public String getLexeme() {
        return this.lexeme;
    }

    public TokenType getTokenType() {
        return this.tokenType;
    }

    @Override
    public String toString() {
        return String.format("{ lexeme=\"%s\", tokenType=\"%s\" }", this.lexeme, this.tokenType);
    }
}