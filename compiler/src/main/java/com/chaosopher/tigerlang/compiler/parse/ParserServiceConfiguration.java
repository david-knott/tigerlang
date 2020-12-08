package com.chaosopher.tigerlang.compiler.parse;

/**
 * ParserServiceConfiguration encapsulates the various
 * different options available when parsing a tiger program.
 */
public class ParserServiceConfiguration {

    private boolean parserTrace = false;
    private boolean noPrelude = false;

    public boolean isParserTrace() {
        return parserTrace;
    }

    public void setParserTrace(boolean parserTrace) {
        this.parserTrace = parserTrace;
    }

    public boolean isNoPrelude() {
        return noPrelude;
    }

    public void setNoPrelude(boolean noPrelude) {
        this.noPrelude = noPrelude;
    }
}