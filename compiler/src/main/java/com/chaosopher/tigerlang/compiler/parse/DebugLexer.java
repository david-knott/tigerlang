package com.chaosopher.tigerlang.compiler.parse;

import java.io.IOException;
import java.io.PrintStream;

import java_cup.runtime.Symbol;

class DebugLexer implements Lexer {

    final Yylex yylex;
    final PrintStream printStream;

    public DebugLexer(Yylex yylex, PrintStream printStream) {
        this.yylex = yylex;
        this.printStream = printStream;
    }

    @Override
    public Symbol nextToken() throws IOException {
        return yylex.nextToken();
    }
}