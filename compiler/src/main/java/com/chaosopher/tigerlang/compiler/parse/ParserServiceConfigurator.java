package com.chaosopher.tigerlang.compiler.parse;

@FunctionalInterface
public interface ParserServiceConfigurator {
    public void configure(ParserServiceConfiguration parserServiceConfiguration);
}